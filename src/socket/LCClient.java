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
public class LCClient {
    private static final PrintStream out;
    private static final BufferedReader br;
    static {
        out=System.out;
        br=new BufferedReader(new InputStreamReader(System.in));
    }
    private int port=52535;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private MainFrame mainFrame;
    private boolean isRunning;
    private String address;
    public LCClient(final MainFrame mainFrame){
        this.mainFrame=mainFrame;
        isRunning=false;
        address= JOptionPane.showInputDialog(mainFrame,"Please input IP-Address","127.0.0.1");
        out.println("address : "+address);
        try {
            socket = new Socket(address, port);
            out.println("Client requested");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
//            sessionStarted();
            (new Thread(()->sessionStarted())).start();
        }catch(IOException ioException){
            ioException.printStackTrace();
            closeSession();
        }
    }
    public void sessionStarted(){
        isRunning=true;
        String msg;
        try{
            while(true){
                out.print("Please enter some string : ");
                msg=br.readLine();
                dout.writeUTF(msg);
                if(msg.equals(CONNECTION.CLOSE_SESSION.toString())){
                    closeSession();
                    break;
                }
                msg=din.readUTF();
                out.println("server>"+msg);
                if(msg.equals(CONNECTION.CLOSE_SESSION.toString())){
                    closeSession();
                    break;
                }else if(msg.equals(CONNECTION.REQUEST_REJECTED.toString())){
                    out.println(CONNECTION.REQUEST_REJECTED);
                    closeSession();
                    break;
                }
            }
        }catch(final IOException ioException){
            ioException.printStackTrace();
            closeSession();
        }
    }
    public void closeSession(){
        out.println("Closing Session");
        isRunning=false;
        try{
            if(din!=null)
                din.close();
            if(dout!=null)
                dout.close();
            if(socket!=null && !socket.isClosed())
                socket.close();
        }catch(final IOException exception){
            exception.printStackTrace();
        }
    }
    public boolean isRunning(){return isRunning;}
}
