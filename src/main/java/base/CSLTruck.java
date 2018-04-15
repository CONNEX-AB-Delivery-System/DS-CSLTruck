package base;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ev3dev.actuators.lego.motors.EV3LargeRegulatedMotor;
import ev3dev.actuators.lego.motors.EV3MediumRegulatedMotor;
import ev3dev.sensors.Battery;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import ev3dev.hardware.EV3DevSensorDevice;
import ev3dev.sensors.BaseSensor;
import lejos.utility.Delay;

public class CSLTruck {

    static boolean isRunning = true;

    //private static ServerSocket serverSocket;

    public EV3LargeRegulatedMotor motorDrive;
    public EV3MediumRegulatedMotor motorSteer;

    public CSLTruckSensorSuite sensorSuite;

    public CSLTruck() {
        //motorDrive = new EV3LargeRegulatedMotor(MotorPort.D);
        //motorSteer = new EV3MediumRegulatedMotor(MotorPort.C);
        //final EV3LargeRegulatedMotor craneLift = new EV3LargeRegulatedMotor(MotorPort.B);
        //final EV3MediumRegulatedMotor craneGrabber = new EV3MediumRegulatedMotor(MotorPort.A);
    }


    public static void main(final String[] args) {


        System.out.print("jefferson: starting..." + "\n");

        final EV3LargeRegulatedMotor motorDrive = new EV3LargeRegulatedMotor(MotorPort.D);
        final EV3MediumRegulatedMotor motorSteer = new EV3MediumRegulatedMotor(MotorPort.C);

        new CSLTruck().startServer();


    }

    private boolean serverSocketRunning = true;

    public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8000);
                    System.out.println("Waiting for clients to connect...");
                    while (serverSocketRunning) {
                        Socket clientSocket = serverSocket.accept();
                        clientProcessingPool.submit(new ClientTask(clientSocket));

                    }

                    serverSocket.close();
                    System.out.println("Server closed");

                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
            }
        };

        Thread serverThread = new Thread(serverTask);
        serverThread.start();

    }

    private class ClientTask implements Runnable {
        private final Socket clientSocket;

        private boolean clientSocketRunning = true;

        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.println("Got connection from SCS!");

            // Do whatever required to process the client's request

            BufferedReader reader;
            BufferedWriter writer;

            try {
                reader = new BufferedReader(new InputStreamReader(
                        this.clientSocket.getInputStream()));
                writer = new BufferedWriter(new OutputStreamWriter(
                        this.clientSocket.getOutputStream()));

                String outputValue = this.clientSocket.getLocalSocketAddress().toString();
                writer.write(outputValue+"\n"); writer.flush();

                String line;
                while (this.clientSocketRunning) {
                    line = reader.readLine();
                    System.out.println("RECIEVED " + line);
                    switch (line) {
                        case "STOP":
                            this.clientSocketRunning = false;
                            serverSocketRunning = false;
                            break;
                        case "null":
                            this.clientSocketRunning = false;
                            serverSocketRunning = false;
                            break;
                        case "DELIVER":
                            break;
                    }
                }

                reader.close();
                writer.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Connection closed");
            System.exit(0);
        }
    }

}
        //server.returnIPCSLT();



        //System.out.println("Motors initialized");


        /*lr.wake();

        lr.calibrateWhite();

        int value = lr.getPIDValue();
        //String mode = lr.getStringAttribute("mode");

        System.out.println("Current value" + value);

        int [] rawValues = lr.getRAWValues();

        for (int i = 0; i < rawValues.length; i++) {
            System.out.print("value" + i + " " + rawValues[i]);
        }

        lr.sleep();*/



        //lr.setCurrentMode("RAW");

        /*ArrayList<String> modeName = lr.getAvailableModes();


        for (int i = 0; i < modeName.size(); i++) {
            System.out.println("Current Mode" + modeName.get(i));
        }*/



//






//        // the listener with the while readline
//        String line;
//        while ((line = reader.readLine()) != "STOP" && isRunning) {
//                System.out.println("RECIEVED " + line);
//                switch (line) {
//                    case "UP-PRESS":
//                        craneLift.setSpeed(500);
//                        // leftMottor.setAcceleration(150);
//                        craneLift.forward();
//                        //rightMottor.setSpeed(1500);
//                        // rightMottor.setAcceleration(150);
//                        //rightMottor.forward();
//                        break;
//                    case "UP-RELEASE":
//                        craneLift.stop(true);
//                        //rightMottor.stop(true);
//                        break;
//                    case "DOWN-PRESS":
//                        craneLift.setSpeed(500);
//                        // leftMottor.setAcceleration(150);
//                        craneLift.backward();
//                        //rightMottor.setSpeed(1500);
//                        // rightMottor.setAcceleration(150);
//                        //rightMottor.backward();
//                        break;
//                    case "DOWN-RELEASE":
//                        craneLift.stop(true);
//                        //rightMottor.stop(true);
//                        break;
//                    case "LEFT-PRESS":
//                        craneGrabber.setSpeed(400);
//                        // rightMottor.setAcceleration(150);
//                        craneGrabber.backward();
//                        //leftMottor.setSpeed(1500);
//                        // leftMottor.setAcceleration(150);
//                        //leftMottor.forward();
//                        break;
//                    case "LEFT-RELEASE":
//                        craneGrabber.stop(true);
//                        //leftMottor.stop(true);
//                        break;
//                    case "RIGHT-PRESS":
//                        craneGrabber.setSpeed(400);
//                        // rightMottor.setAcceleration(150);
//                        craneGrabber.forward();
//                        //leftMottor.setSpeed(1500);
//                        // leftMottor.setAcceleration(150);
//                        //leftMottor.backward();
//                        break;
//                    case "RIGHT-RELEASE":
//                        craneGrabber.stop(true);
//                        //leftMottor.stop(true);
//                        break;
//                    case "STOP":
//                        CSLTruck.isRunning = false;
//                        break;
//                }
//            }


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


    }*/

    //private final int serverSocketPort = 8000;

    //private ServerSocket serverSocket = null;

   /* public void startServer() {

        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                ServerSocket serverSocket = null;

                try {

                    //System.out.print("port" + serverSocketPort);
                    serverSocket = new ServerSocket(8000);
                    if (serverSocket != null) {
                        while (isRunning) {
                            Socket clientSocket = serverSocket.accept();
                            String serverIP = serverSocket.getInetAddress().toString();
                            System.out.println("Server: " + serverIP + " port: " + "8000");
                            clientProcessingPool.submit(new CSLTruck .ClientTask(clientSocket));
                        }
                    }
                    else {
                        System.err.println("Unable to process client request");
                        System.exit(0);
                    }

                } catch (IOException e) {
                    System.err.println("Unable to process client request");
                    e.printStackTrace();
                }
                finally {
                    if (serverSocket != null) {
                        try {
                            serverSocket.close();
                        } catch (IOException e) {
                            // log error just in case
                        }
                    }
                }
            }

        };

        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }




    private class ClientTask implements Runnable {

        private Socket socket;

        private BufferedReader reader;
        private BufferedWriter writer;

        private final Socket clientSocket;

        private ClientTask(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            System.out.println("Got a connection !");

            //motorDrive.rotate(90);

            CSLTruckSensorSuite sensorSuite = new CSLTruckSensorSuite();

            sensorSuite.printLineValues();

            //open input/output streams

            try {
                this.reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));

                String line;
                while (isRunning) {
                    line = reader.readLine();
                    System.out.println("RECEIVED: " + line);
                    switch (line) {
                        case "exit":
                            System.exit(0);
                    }
                }


            } catch(Exception e) {
                // Error handling code...
                throw new RuntimeException(e.getMessage());
            }

            try {
                this.writer = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));

                String outputValue = this.socket.getLocalSocketAddress().toString();
                this.writer.write(outputValue+"\n"); this.writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            }
            // Do whatever required to process the client's request

            try {
                this.reader.close();
                this.writer.close();
                this.clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } */

