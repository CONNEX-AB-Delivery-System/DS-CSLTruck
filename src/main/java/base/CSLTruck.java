package base;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.utility.Delay;

public class CSLTruck {

    static boolean isRunning = true;

    public static void main(final String[] args) throws IOException {


        //final EV3LargeRegulatedMotor leftMottor = new EV3LargeRegulatedMotor(MotorPort.B);
        //final EV3LargeRegulatedMotor rightMottor = new EV3LargeRegulatedMotor(MotorPort.C);
        //final EV3LargeRegulatedMotor motorDrive = new EV3LargeRegulatedMotor(MotorPort.D);
        //final EV3MediumRegulatedMotor motorSteer = new EV3MediumRegulatedMotor(MotorPort.C);
        final EV3LargeRegulatedMotor craneLift = new EV3LargeRegulatedMotor(MotorPort.B);
        final EV3MediumRegulatedMotor craneGrabber = new EV3MediumRegulatedMotor(MotorPort.A);

        System.out.println("Motors initialized");

        //https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
        ServerSocket serv = new ServerSocket(19231);

        Socket socket = serv.accept();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));

        String outputValue = socket.getLocalSocketAddress().toString();

        writer.write(outputValue+"\n");writer.flush();

        System.out.println("Checking Battery");
        System.out.println("Votage: " + Battery.getInstance().getVoltage());

        // the listener with the while readline
        String line;
        while ((line = reader.readLine()) != "STOP" && isRunning) {
                System.out.println("RECIEVED " + line);
                switch (line) {
                    case "UP-PRESS":
                        craneLift.setSpeed(500);
                        // leftMottor.setAcceleration(150);
                        craneLift.forward();
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.forward();
                        break;
                    case "UP-RELEASE":
                        craneLift.stop(true);
                        //rightMottor.stop(true);
                        break;
                    case "DOWN-PRESS":
                        craneLift.setSpeed(500);
                        // leftMottor.setAcceleration(150);
                        craneLift.backward();
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.backward();
                        break;
                    case "DOWN-RELEASE":
                        craneLift.stop(true);
                        //rightMottor.stop(true);
                        break;
                    case "LEFT-PRESS":
                        craneGrabber.setSpeed(400);
                        // rightMottor.setAcceleration(150);
                        craneGrabber.backward();
                        //leftMottor.setSpeed(1500);
                        // leftMottor.setAcceleration(150);
                        //leftMottor.forward();
                        break;
                    case "LEFT-RELEASE":
                        craneGrabber.stop(true);
                        //leftMottor.stop(true);
                        break;
                    case "RIGHT-PRESS":
                        craneGrabber.setSpeed(400);
                        // rightMottor.setAcceleration(150);
                        craneGrabber.forward();
                        //leftMottor.setSpeed(1500);
                        // leftMottor.setAcceleration(150);
                        //leftMottor.backward();
                        break;
                    case "RIGHT-RELEASE":
                        craneGrabber.stop(true);
                        //leftMottor.stop(true);
                        break;
                    case "STOP":
                        CSLTruck.isRunning = false;
                        break;
                }
            }


        //System.out.println("Creating Motor A & B");
        //final EV3LargeRegulatedMotor motorDrive = new EV3LargeRegulatedMotor(MotorPort.D);
        //final EV3MediumRegulatedMotor motorSteer = new EV3MediumRegulatedMotor(MotorPort.C);
        //final EV3LargeRegulatedMotor craneLift = new EV3LargeRegulatedMotor(MotorPort.B);
        //final EV3MediumRegulatedMotor craneGrabber = new EV3MediumRegulatedMotor(MotorPort.A);






        //To Stop the motor in case of pkill java for example
        /*Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Emergency Stop");
                motorDrive.stop();
                motorSteer.stop();
                craneLift.stop();
                craneGrabber.stop();
            }
        }));

        System.out.println("Defining the Stop mode");
        motorDrive.brake();
        motorSteer.brake();

        System.out.println("Defining motor speed");
        final int motorSpeed = 200;
        motorDrive.setSpeed(motorSpeed);
        //motorSteer.setSpeed(motorSpeed);

        System.out.println("Go Forward with the motors");
        motorDrive.forward();
        //motorSteer.forward();

        Delay.msDelay(2000);

        System.out.println("Stop motors");
        motorDrive.stop();
        //motorSteer.stop();

        System.out.println("Go Backward with the motors");
        motorDrive.backward();
        //motorSteer.backward();

        Delay.msDelay(2000);

        System.out.println("Stop motors");
        motorDrive.stop();
        //motorSteer.stop();

        System.out.println("Checking Battery");
        System.out.println("Votage: " + Battery.getInstance().getVoltage());

        System.exit(0);

        */
    }
}
