package socket;

import GUI.MainFrame;
import enums.CONNECTION;

import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @dev :   devpar
 * @date :   05-Jun-2021
 */
public class LCServer {
    private static final PrintStream out;
    private static final BufferedReader br;
    static {
        out=System.out;
        br=new BufferedReader(new InputStreamReader(System.in));
    }
    private int port=52535;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream din;
    private DataOutputStream dout;
    private MainFrame mainFrame;
    private boolean isRunning;
    public LCServer(MainFrame mainFrame){
        this.mainFrame=mainFrame;
        isRunning=false;
//        SwingUtilities.invokeLater(()->{initializeServer();});
        (new Thread(()->initializeServer())).start();
//        initializeServer();
    }
    public void initializeServer(){
        try {
            serverSocket = new ServerSocket(port);
            out.println("Server started");
            socket = serverSocket.accept();
            out.println("Client requested");
            din=new DataInputStream(socket.getInputStream());
            dout=new DataOutputStream(socket.getOutputStream());
            int choice = JOptionPane.YES_NO_OPTION;
            int result = JOptionPane.showConfirmDialog(mainFrame,
                    "Do you with to connect to : " + socket.getInetAddress().getHostAddress(),
                    "Connection Request", choice);
            if (result == JOptionPane.YES_OPTION) {
                startSession();
            } else {
                out.println("Request rejected");
                dout.writeUTF(CONNECTION.REQUEST_REJECTED.toString());
                closeSession();
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
            closeSession();
        }
    }
    public void startSession(){
        out.println("Start Session");
        isRunning=true;
        String msg;
        try {
            while (true) {
                msg = din.readUTF();
                if(msg.equals(CONNECTION.CLOSE_SESSION.toString())){
                    closeSession();
                    break;
                }
                out.println("client>"+msg);
                out.print("please input : ");
                msg=br.readLine();
                dout.writeUTF(msg);
                if(msg.equals(CONNECTION.CLOSE_SESSION.toString())){
                    closeSession();
                    break;
                }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
            isRunning=false;
            closeSession();
        }
    }
    public void closeSession(){
        out.println("Closing Session");
        try{
//            dout.writeUTF("close session");
            if(din!=null)
                din.close();
            if(dout!=null)
                dout.close();
            if(socket!=null && !socket.isClosed())
                socket.close();
            if(serverSocket!=null && !serverSocket.isClosed())
                serverSocket.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        isRunning=false;
    }
    public boolean isRunning(){
        return isRunning;
    }
}
