package base;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  Title: CSLThreadPooledServer
 *
 *  Implements Pooled Server as in:
 *
 *  //https://docs.oracle.com/javase/7/docs/api/java/net/ServerSocket.html
 *  //http://tutorials.jenkov.com/java-multithreaded-servers/thread-pooled-server.html
 *
 *  NOTE: Nothing should be changed in this class.
 */

public class CSLTThreadPooledServer extends Thread {

    protected int             serverPort    = 8000;
    protected ServerSocket    serverSocket  = null;
    protected boolean         isStopped     = false;
    protected Thread          runningThread = null;
    protected ExecutorService threadPool    = Executors.newFixedThreadPool(10);
    protected SocketThread socket;
    private String          threadName;

    public CSLTThreadPooledServer (String name){
        this.threadName = name;
    }

    public CSLTThreadPooledServer (String name, int port){
        this.threadName = name;
        this.serverPort = port;
    }

    public void run(){
        int i = 0;

        synchronized(this){
            this.runningThread = Thread.currentThread();
        }

        openServerSocket();
        System.out.println("Server Started.") ;
        while(! isStopped()){

            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            System.out.println("WorkerRunnable" + i);
            i++;

            socket = new SocketThread(clientSocket, "DT-SCS socket");
            socket.start();

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println("Server Stopped.") ;
    }

    public void isRunning() {
        if (CSLTruck.outputCommandSCS.equals("FINISHED")) {
            socket.isRunningWR();
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public void stopServerSocket(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8000", e);
        }
    }

}