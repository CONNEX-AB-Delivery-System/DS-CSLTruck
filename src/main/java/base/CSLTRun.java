package base;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import lejos.robotics.SampleProvider;
import ev3dev.sensors.BaseSensor;
import lejos.hardware.port.Port;
import lejos.utility.Delay;

//import static base.DeliveryTruck.motorSteer;
//import static base.DeliveryTruck.motorDrive;

/**
 *  Title: CSLTRun thread
 *
 *  This is thread where all truck logic for task execution should be implemented.
 *  Use function method to do that (it can be extended with other functions).
 */

class CSLTRun extends Thread {
    private Thread t;
    private String threadName;


    CSLTRun ( String threadName) {
        this.threadName = threadName;
        System.out.println("Creating " +  this.threadName );
    }


    private boolean safeRotateTo(EV3MediumRegulatedMotor motor, int rotationAngle, int rotationSpeed, String name) throws InterruptedException {
        int count = 0;
        int tachocount = 0;
        int prevtachocount;
        boolean rotationDirectionForward;

        //constant for deciding if there enter speedy or precision rotation
        int minRotationConstant = 200;
        int precisionAdjustmentSpeed = 100;

        boolean stopFlag = false;
        int stopLiftConstant = rotationAngle/(rotationSpeed/20)+50;


        //check direction of rotation
        if (tachocount < rotationAngle) {
            rotationDirectionForward = true;
        }
        else {
            rotationDirectionForward = false;
        }

        count = 0;
        System.out.println(name + "$ -rotate motor -angle: " + rotationAngle);
        motor.setSpeed(rotationSpeed);
        motor.rotate(rotationAngle, true);
        prevtachocount = 0;
        tachocount = motor.getTachoCount();

        //enter main loop only if there is enough angle to rotate, else go for adjust mode
        if (rotationAngle > minRotationConstant) {

            //rotationAngle-50 ensures that we start to stop before reaching rotation angle, to allow motor time to stop
            //Fail safe check - motor.isMoving() checks if motor is rotating, if not, exit
            //Fail safe check - stopLiftConstant - for whatever reason motor doesn't stop and while is looping, stopLiftConstant ensures that there will be timeout

            while (tachocount < (rotationAngle-(minRotationConstant/2)) && (motor.isMoving() && count <= stopLiftConstant ) ) {

                prevtachocount = tachocount;
                tachocount = motor.getTachoCount();

                //Fail safe check - check if we are going in right direction, based on rotationDirectionForward variable
                if (rotationDirectionForward) {
                    if (prevtachocount > tachocount) {
                        count = stopLiftConstant;
                        stopFlag = true;
                    }
                }
                else {
                    if (prevtachocount < tachocount) {
                        count = stopLiftConstant;
                        stopFlag = true;
                    }
                }

                count++;
                Thread.sleep(20);
            }

            motor.stop();
            Thread.sleep(100); //thread sleep to allow motor time to coast to stop.

            tachocount = motor.getTachoCount();
            System.out.println(name + "$ -motor rotated -angle: " + rotationAngle + " -tacos: " + tachocount);
        }

        //check if previous operation was successful, if not, don't do adjustment
        if (!stopFlag) {
            //adjust mode, does final adjustment of rotation in slower speed for more precision
            if (tachocount < rotationAngle) {
                System.out.println(name + "$ -adjust motor position -for angle: " + (rotationAngle-tachocount));
                motor.setSpeed(precisionAdjustmentSpeed);
                motor.rotate((rotationAngle-tachocount), true);
                while ((tachocount < rotationAngle) && motor.isMoving()) {
                    Thread.sleep(10);
                }
            }
            motor.stop();
            Thread.sleep(100);
            System.out.println(name + "$ -motor rotated -final tacos: " + motor.getTachoCount());
        }

        if (stopFlag) {
            return false;
        }
        else {
            return true;
        }
    }

    private boolean safeRotateTo(EV3LargeRegulatedMotor motor, int rotationAngle, int rotationSpeed, String name)
            throws InterruptedException {

        int count = 0;
        int tachocount = motor.getTachoCount();
        int prevtachocount;
        boolean rotationDirectionForward;

        //constant for deciding if there enter speedy or precision rotation
        int minRotationConstant = 200;
        int precisionAdjustmentSpeed = 50;

        boolean stopFlag = false;
        int stopLiftConstant = rotationAngle/(rotationSpeed/40)+50;


        //check direction of rotation
        if (rotationAngle > tachocount) {
            rotationDirectionForward = true;
        }
        else {
            rotationDirectionForward = false;
        }

        //enter main loop only if there is enough angle to rotate, else go for adjust mode
        if (Math.abs(rotationAngle) > minRotationConstant) {

            count = 0;
            System.out.println(name + "$ -rotate motor -angle: " + rotationAngle + " -direction: " + rotationDirectionForward);
            motor.setSpeed(rotationSpeed);
            motor.rotateTo(rotationAngle, true);
            tachocount = motor.getTachoCount();

            //rotationAngle-50 ensures that we start to stop before reaching rotation angle, to allow motor time to stop
            //Fail safe check - motor.isMoving() checks if motor is rotating, if not, exit
            //Fail safe check - stopLiftConstant - for whatever reason motor doesn't stop and while is looping, stopLiftConstant ensures that there will be timeout

            if (rotationDirectionForward) {
                while (tachocount < (rotationAngle-(minRotationConstant/2)) && (motor.isMoving() && count <= stopLiftConstant ) ) {
                    prevtachocount = tachocount;
                    //Fail safe check - check if we are going in right direction, based on rotationDirectionForward variable
                    if (prevtachocount > tachocount) {
                        count = stopLiftConstant;
                        stopFlag = true;
                    }
                    count++;
                    Thread.sleep(10);
                    tachocount = motor.getTachoCount();
                }
            } else {
                while (tachocount > (rotationAngle+(minRotationConstant/2)) && (motor.isMoving() && count <= stopLiftConstant ) ) {

                    prevtachocount = tachocount;
                    //Fail safe check - check if we are going in right direction, based on rotationDirectionForward variable

                    if (prevtachocount < tachocount) {
                        count = stopLiftConstant;
                        stopFlag = true;
                    }
                    count++;
                    Thread.sleep(10);
                    tachocount = motor.getTachoCount();
                }
            }

            motor.stop();
            Thread.sleep(100); //thread sleep to allow motor time to coast to stop.

            tachocount = motor.getTachoCount();
            System.out.println(name + "$ -crane motor -angle: " + rotationAngle + " -tacos: " + tachocount);
        }

        //check if previous operation was successful, if not, don't do adjustment
        if (!stopFlag) {
            //adjust mode, does final adjustment of rotation in slower speed for more precision
            motor.setSpeed(precisionAdjustmentSpeed);
            System.out.println(name + "$ -adjust motor position -for angle: " + (rotationAngle - tachocount) + " -direction: " + rotationDirectionForward);
            motor.rotate((rotationAngle - tachocount), true);

            if (rotationDirectionForward) {
                if (tachocount < rotationAngle) {
                    while ((tachocount < rotationAngle) && motor.isMoving()) {
                        Thread.sleep(10);
                    }
                }
            }
            else {
                if (tachocount > rotationAngle) {
                    while ((tachocount > rotationAngle) && motor.isMoving()) {
                        Thread.sleep(10);
                    }
                }
            }
            motor.stop();
            Thread.sleep(100);
            System.out.println(name + "$ -crane rotated -final tacos: " + motor.getTachoCount());
        }

        if (stopFlag) {
            return false;
        }
        else {
            return true;
        }
    }


    private boolean rotateToZero(EV3MediumRegulatedMotor motor, int rotationSpeed, String name) throws InterruptedException {

        int tachocount;


        System.out.println(name + "$ -return motor to starting position -tacos-" + motor.getTachoCount());
        motor.setSpeed(rotationSpeed);
        motor.rotateTo(0, true);
        while (motor.isMoving()) {
            Thread.sleep(50);
        }
        motor.stop();
        Thread.sleep(100);

        tachocount = motor.getTachoCount();
        System.out.println(name + "$ -check for final adjustment -tacos-" + tachocount);
        motor.setSpeed(100);
        motor.rotate(0-tachocount, true);
        while (motor.isMoving()) {
            Thread.sleep(10);
        }
        System.out.println(name + "$ -motor returned to starting position -tacos-" + motor.getTachoCount());

        return true;
    }

    private boolean rotateToZero(EV3LargeRegulatedMotor motor, int rotationSpeed, String name) throws InterruptedException {

        int tachocount;


        System.out.println(name + "$ -return motor to starting position -tacos-" + motor.getTachoCount());
        motor.setSpeed(rotationSpeed);
        motor.rotateTo(0, true);
        while (motor.isMoving()) {
            Thread.sleep(20);
        }
        motor.stop();
        Thread.sleep(100);

        tachocount = motor.getTachoCount();
        System.out.println(name + "$ -check for final adjustment -tacos-" + tachocount);
        motor.setSpeed(100);
        motor.rotate(0-tachocount, true);
        while (motor.isMoving()) {
            Thread.sleep(10);
        }
        System.out.println(name + "$ -motor returned to starting position -tacos-" + motor.getTachoCount());

        return true;
    }


    private boolean runMotors() {

        int[] values = new int[8];
        int pidPrev;
        int pidValue;
        int pidAverage;
        int stop;
        int pidflag;
        int count = 0;
        int tachocount = 0;
        int prevtachocount;
        int motorDriveSpeed = 0;

        int craneLiftSpeed = 500;
        int lowerCraneConstant = 9250;
        int grabberConstant = 6400;
        int craneLiftDriveConstant = 4500;
        int stopLiftConstant = 200;


        try {

            while (CSLTruck.isRunning && !CSLTruck.runThreadIsExecuted) {

                //DeliveryTruck.isRunning should be checked as often as possible
                //to allow stop from SCS

                //TODO: YOUR CODE HERE
                //TODO: CHECK THIS DOCUMENTATION TO UNDERSTAND HOW TO RUN THIS TRUCK
                //TODO: AND HOW TO WRITE CODE:
                //https://github.com/CONNEX-AB-Delivery-System/DS-CSLTruck/blob/master/README.md

                //TODO: To test in action
                safeRotateTo(CSLTruck.craneLift, lowerCraneConstant,500,"craneLift");

                safeRotateTo(CSLTruck.craneGrabber, grabberConstant,500,"craneGrabber");

                safeRotateTo(CSLTruck.craneLift, craneLiftDriveConstant,500,"craneLift");

                Thread.sleep(1000);


                Thread.sleep(1000);
                CSLTruck.motorSteer.setSpeed(300);
                CSLTruck.motorSteer.rotateTo(-80, true);
                Thread.sleep(200);

                safeRotateTo(CSLTruck.motorDrive, 1800,200,"motorDrive");

                Thread.sleep(500);

                CSLTruck.motorSteer.setSpeed(300);
                CSLTruck.motorSteer.rotateTo(80, true);
                Thread.sleep(200);
                safeRotateTo(CSLTruck.motorDrive, 0,200,"motorDrive");

                Thread.sleep(500);

                CSLTruck.motorSteer.setSpeed(300);
                CSLTruck.motorSteer.rotateTo(0, true);
                Thread.sleep(200);

                Thread.sleep(1000);


                values = CSLTruck.lineReader.getCALValues();
                Thread.sleep(200);
                values = CSLTruck.lineReader.getCALValues();
                pidValue = NewPID.calculatePID(values, 60);

                System.out.print(" V0: " + values[0]);
                System.out.print(" V1: " + values[1]);
                System.out.print(" V2: " + values[2]);
                System.out.print(" V3: " + values[3]);
                System.out.print(" V4: " + values[4]);
                System.out.print(" V5: " + values[5]);
                System.out.print(" V6: " + values[6]);
                System.out.print(" V7: " + values[7]);
                System.out.println(" pidValue: " + pidValue);

                //start moving only if truck is on line
                if (pidValue < 25 & pidValue > -25) {
                    System.out.println("move: " + pidValue);
                    motorDriveSpeed = 100;
                    CSLTruck.motorDrive.setSpeed(motorDriveSpeed);
                    CSLTruck.motorDrive.backward();
                    CSLTruck.motorSteer.setSpeed(500);
                }

                int stopnum = 200;
                pidAverage = 0;

                for(int i = 0; i < stopnum; i++){

                    pidPrev = pidAverage;
                    pidValue = 0;
                    pidAverage = 0;
                    pidflag = 0;

                    for (int j = 0; j <= 2; j++)  {
                        values = CSLTruck.lineReader.getCALValues();
                        System.out.print(" V0: " + values[0]);
                        System.out.print(" V1: " + values[1]);
                        System.out.print(" V2: " + values[2]);
                        System.out.print(" V3: " + values[3]);
                        System.out.print(" V4: " + values[4]);
                        System.out.print(" V5: " + values[5]);
                        System.out.print(" V6: " + values[6]);
                        System.out.print(" V7: " + values[7]);

                        pidValue = NewPID.calculatePID(values, 60);
                        System.out.println(" -i" + i +" pidValue: " + pidValue);

                        if (pidValue == -100) {
                            pidflag = 1;
                        }
                        else {
                            pidAverage = pidAverage + pidValue;
                        }
                    }

                    if (pidflag == 1) {
                        pidAverage = pidPrev;
                    }
                    else {
                        pidValue = pidAverage / 2;
                    }


                    stop = (values[0] + values[1] + values[2] + values [3] + values [4] + values[5] + values[6] + values[7]) / 8;



                    if ((stop < 35))
                    {
                        System.out.println("followTheLine$ Stop: " + stop);
                        CSLTruck.motorDrive.stop();
                        i = stopnum;
                    }
                    else {

                        if (pidValue > 69) {
                            CSLTruck.motorSteer.rotateTo(-69, true);
                        }
                        else if (pidValue < -69) {
                            CSLTruck.motorSteer.rotateTo(69, true);
                        }
                        else {
                            CSLTruck.motorSteer.rotateTo(-pidValue, true);
                        }

                        Thread.sleep(40);

                        if (((pidValue < -20) | (pidValue > 20)) && (motorDriveSpeed == 100)) {
                            motorDriveSpeed = 50;
                            CSLTruck.motorDrive.setSpeed(motorDriveSpeed);
                        }
                        if (((pidValue > -20) & (pidValue < 20)) && (motorDriveSpeed == 50)) {
                            motorDriveSpeed = 100;
                            CSLTruck.motorDrive.setSpeed(motorDriveSpeed);
                        }

                    }

                }

                CSLTruck.motorDrive.stop(true);
                CSLTruck.motorSteer.rotateTo(0, true);

                Thread.sleep(200);

                rotateToZero(CSLTruck.craneGrabber, craneLiftSpeed, "craneGrabber");

                rotateToZero(CSLTruck.craneLift, craneLiftSpeed, "craneLift");

                /*
                CSLTruck.craneGrabber.setSpeed(500);
                CSLTruck.craneGrabber.backward();
                Thread.sleep(8000);
                CSLTruck.craneGrabber.stop(true);
                */
                //Anna har antecknat graderna.

                /*
                Thread.sleep(1000);

                CSLTruck.craneLift.setSpeed(500);
                CSLTruck.craneLift.rotateTo(5000,false);
                CSLTruck.craneLift.stop(true);
                Thread.sleep(1000);
                */

                CSLTruck.runThreadIsExecuted = true;
                CSLTruck.outputCommandSCS = "FINISHED";
                System.out.println("Task Executed");

            }

        } catch (InterruptedException e) {
            System.out.println("Thread " +  this.threadName + " interrupted.");
        }

        return true;
    }

    public void run() {
        System.out.println("Running " +  this.threadName );

        this.runMotors();

        System.out.println("Thread " +  this.threadName + " exiting.");
    }

    public void start () {
        System.out.println("Starting " +  this.threadName );
        if (t == null) {
            t = new Thread (this, this.threadName);
            t.start ();
        }
    }
}