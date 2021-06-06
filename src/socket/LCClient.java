package socket;

import GUI.MainFrame;

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
    private int port=55535;
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
        try {
            socket = new Socket(address, port);
            out.println("Client requested");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
            sessionStarted();
        }catch(IOException ioException){
            ioException.printStackTrace();
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
                msg=din.readUTF();
                out.println("server>"+msg);
            }
        }catch(final IOException ioException){
            ioException.printStackTrace();
            closeSession();
        }
    }
    public void closeSession(){
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
