package base;

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

    private boolean runMotors() {

        try {

            while (CSLTruck.isRunning && !CSLTruck.runThreadIsExecuted) {

                //DeliveryTruck.isRunning should be checked as often as possible
                //to allow stop from SCS

                //TODO: YOUR CODE HERE

                //System.out.println("Current value" + DeliveryTruck.lineReader.getPIDValue());


                CSLTruck.motorSteer.setSpeed(200);
                CSLTruck.motorSteer.rotateTo(20, true);

                Thread.sleep(500);

                CSLTruck.motorSteer.setSpeed(200);
                CSLTruck.motorSteer.rotateTo(-20, true);

                CSLTruck.runThreadIsExecuted = true;
                CSLTruck.outputCommandSCS = "FINISHED";
                System.out.println("Task Executed");

                /*DeliveryTruck.motorSteer.brake();
                DeliveryTruck.motorSteer.setSpeed(100);
                DeliveryTruck.motorSteer.forward();
                Thread.sleep(200);
                DeliveryTruck.motorSteer.stop();

                DeliveryTruck.motorSteer.setSpeed(100);
                DeliveryTruck.motorSteer.backward();
                Thread.sleep(200);
                DeliveryTruck.motorSteer.stop();

                DeliveryTruck.motorDrive.setSpeed(300);
                DeliveryTruck.motorDrive.backward();
                Thread.sleep(500);
                DeliveryTruck.motorDrive.stop();*/

                CSLTruck.runThreadIsExecuted = true;
                CSLTruck.outputCommandSCS = "FINISHED";
                System.out.println("Task executed");

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