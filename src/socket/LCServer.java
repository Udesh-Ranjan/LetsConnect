package socket;

import GUI.MainFrame;
import enums.CONNECTION;
import enums.TRANSFER;
import img.LCImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
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
    private static Robot robot;
    private static final Rectangle screenSize;
    final static int size = 4096000;

    static {
        out = System.out;
        br = new BufferedReader(new InputStreamReader(System.in));
        try {
            robot = new Robot();
        } catch (AWTException awtException) {
            awtException.printStackTrace();
        }
        screenSize = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }

    private static final int port = 52535;
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream din;
    private DataOutputStream dout;
    private MainFrame mainFrame;
    private boolean isRunning;

    public LCServer(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        isRunning = false;
//        SwingUtilities.invokeLater(()->{initializeServer();});
        (new Thread(() -> initializeServer())).start();
//        initializeServer();
    }

    public void initializeServer() {
        try {
            serverSocket = new ServerSocket(port);
            out.println("Server started");
            socket = serverSocket.accept();
            out.println("Client requested");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
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
        } catch (final IOException ioException) {
            ioException.printStackTrace();
            closeSession();
        }
    }

    public void startSession() {
        out.println("Start Session");
        isRunning = true;
        String msg;
        try {
            while (true) {
                msg = din.readUTF();
                if (msg.equals(CONNECTION.CLOSE_SESSION.toString())) {
                    closeSession();
                    break;
                }
                if (msg.equals(TRANSFER.FILE_TRANSFER.toString())) {
                    final BufferedImage img = createScreenCapture();
                    serialize(dout, img);
//                    img.flush();
                }
               /* out.println("client>"+msg);
                out.print("please input : ");
                msg=br.readLine();
                dout.writeUTF(msg);
                if(msg.equals(CONNECTION.CLOSE_SESSION.toString())){
                    closeSession();
                    break;
                }*/
            }
        } catch (final IOException ioException) {
            ioException.printStackTrace();
            isRunning = false;
            closeSession();
        }
    }

    public void serialize(final DataOutputStream out, final BufferedImage img) throws IOException {
        /*out.writeUTF(TRANSFER.FILE_TRANSFER.toString());
        out.flush();*/
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        baos.flush();
        ImageIO.write(img, "png", baos);
        //baos.flush();
        byte by[] = baos.toByteArray();
        System.out.println("image : ");
//        printBytes(by,0,by.length);
        out.writeUTF(String.valueOf(by.length));
        out.flush();
//        ByteArrayInputStream bin=new ByteArrayInputStream(by);
//        int read;
        System.out.println("length : " + by.length);
        for (int i = 0; by.length > i * size; i++) {
            out.write(by, i * size, Math.min(by.length - i * size, size));
            out.flush();
//            printBytes(by,i*size,Math.min(by.length-i*size,size));
        }
        /*byte[]eof=TRANSFER.EOF.toString().getBytes();
        System.out.println("Writing : "+TRANSFER.EOF.toString());
        printBytes(eof,0,eof.length);
        out.write(eof);
        out.flush();*/
    }

    public static BufferedImage createScreenCapture() {
        final BufferedImage img = robot.createScreenCapture(screenSize);
        return img;
    }

    public void closeSession() {
        out.println("Closing Session");
        try {
//            dout.writeUTF("close session");
            if (din != null)
                din.close();
            if (dout != null)
                dout.close();
            if (socket != null && !socket.isClosed())
                socket.close();
            if (serverSocket != null && !serverSocket.isClosed())
                serverSocket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }
}
