package base;

import java.io.*;
import java.net.Socket;

/**
 *  Title SocketThread
 *
 *  Implements single socket connection via thread that listens to SCS commands and sends commands to SCS.
 *
 *  NOTE: Nothing should be changed in this class.
 */

public class SocketThread extends Thread {

    private Thread t;

    protected Socket clientSocket = null;
    protected String serverText   = null;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    public SocketThread(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
    }


    private void openReaders() {
        try {
            reader = new BufferedReader(new InputStreamReader(
                    clientSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(
                    clientSocket.getOutputStream()));

            String outputValue = clientSocket.getLocalSocketAddress().toString();

            writer.write(outputValue+"\n");writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void isRunningWR() {
        if (CSLTruck.outputCommandSCS.equals("FINISHED")) {

            System.out.println("worker-FINISHED");

            try {
                writer.write(CSLTruck.outputCommandSCS+"\n");writer.flush();
                CSLTruck.outputCommandSCS = "none";
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void start () {
        System.out.println("Starting Worker Thread"  );
        if (t == null) {
            t = new Thread (this, "Worker Thread");
            t.start ();
        }
    }

    public void run() {

        this.openReaders();

        long time = System.currentTimeMillis();

        String line = null;
        while (CSLTruck.isRunning) {

            System.out.println("worker running");

            if (CSLTruck.outputCommandSCS.equals("FINISHED")) {

                System.out.println("worker-FINISHED");
                CSLTruck.outputCommandSCS = "none";
            }

            try {
                line = reader.readLine();
                System.out.println("RECEIVED " + line);
            } catch (IOException e) {
                e.printStackTrace();
            }

            switch (line) {
                case "RUN":
                    CSLTruck.inputCommandSCS = line;
                    break;
                case "LEFT-PRESS":
                    CSLTruck.inputCommandSCS = line;
                    break;
                case "STOP":
                    CSLTruck.inputCommandSCS = line;
                    CSLTruck.runThreadIsExecuted = true;
                    break;
                case "KILL":
                    CSLTruck.inputCommandSCS = line;
                    CSLTruck.isRunning = false;
                    break;
            }

            System.out.println("Request processed: " + time);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        try {
            System.out.println("Worker closed");
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}