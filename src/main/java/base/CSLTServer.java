package base;

import oracle.jrockit.jfr.StringConstantPool;

import java.io.*;
import java.lang.Thread;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CSLTServer {

    public static void main(String[] args) {
        new CSLTServer().startServer();
    }

    public void startServer() {
        final ExecutorService clientProcessingPool = Executors.newFixedThreadPool(10);

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(8000);
                    System.out.println("Waiting for connection...");
                    while (true) {
                        Socket clientSocket = serverSocket.accept();
                        clientProcessingPool.submit(new ClientTask(clientSocket));
                    }
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

            //open input/output streams
            try {
                this.reader = new BufferedReader(new InputStreamReader(
                        socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
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
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}



/*


    private ServerSocket serv;
    private Socket socket;

    private BufferedReader reader;
    private BufferedWriter writer;

    private String readInputCSLT() throws IOException {

        return this.reader.readLine();
    }

    private void writeOutputCSLT(String outputValue) throws IOException {

        this.writer.write(outputValue+"\n"); this.writer.flush();
    }

    public void returnIPCSLT() throws IOException {

        String outputValue = this.socket.getLocalSocketAddress().toString();
        writeOutputCSLT(outputValue);
    }

    private void serverOpen() throws IOException {
        //https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html

        //create server and assign port number
        this.serv = new ServerSocket(19231);
        this.socket = this.serv.accept();

        //open input/output streams
        this.reader = new BufferedReader(new InputStreamReader(
                socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream()));

        returnIPCSLT();
    }

    public void stop() throws IOException {

        this.socket.close();
        this.serv.close();

    }

    public void run() {

        try {
            this.serverOpen();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

} */