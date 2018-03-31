package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
        final EV3LargeRegulatedMotor motorDrive = new EV3LargeRegulatedMotor(MotorPort.D);
        final EV3MediumRegulatedMotor motorSteer = new EV3MediumRegulatedMotor(MotorPort.C);
        //final EV3LargeRegulatedMotor craneLift = new EV3LargeRegulatedMotor(MotorPort.B);
        //final EV3MediumRegulatedMotor craneGrabber = new EV3MediumRegulatedMotor(MotorPort.A);
        ServerSocket serv = new ServerSocket(19231);

        Socket socket = serv.accept();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));

        System.out.println("Checking Battery");
        System.out.println("Votage: " + Battery.getInstance().getVoltage());

        // the listener with the while readline
        String line;
        while ((line = reader.readLine()) != "STOP" && isRunning) {
                System.out.println("RECIEVED " + line);
                switch (line) {
                    case "UP-PRESS":
                        motorDrive.setSpeed(500);
                        // leftMottor.setAcceleration(150);
                        motorDrive.forward();
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.forward();
                        break;
                    case "UP-RELEASE":
                        motorDrive.stop(true);
                        //rightMottor.stop(true);
                        break;
                    case "DOWN-PRESS":
                        motorDrive.setSpeed(5500);
                        // leftMottor.setAcceleration(150);
                        motorDrive.backward();
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.backward();
                        break;
                    case "DOWN-RELEASE":
                        motorDrive.stop(true);
                        //rightMottor.stop(true);
                        break;
                    case "LEFT-PRESS":
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.backward();
                        //leftMottor.setSpeed(1500);
                        // leftMottor.setAcceleration(150);
                        //leftMottor.forward();
                        break;
                    case "LEFT-RELEASE":
                        //rightMottor.stop(true);
                        //leftMottor.stop(true);
                        break;
                    case "RIGHT-PRESS":
                        //rightMottor.setSpeed(1500);
                        // rightMottor.setAcceleration(150);
                        //rightMottor.forward();
                        //leftMottor.setSpeed(1500);
                        // leftMottor.setAcceleration(150);
                        //leftMottor.backward();
                        break;
                    case "RIGHT-RELEASE":
                        //rightMottor.stop(true);
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
