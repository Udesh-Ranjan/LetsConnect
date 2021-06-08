package socket;

import GUI.MainFrame;
import enums.CONNECTION;
import enums.TRANSFER;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
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
    final static byte eof[] = TRANSFER.EOF.toString().getBytes();
    final static int delay = 10;

    static {
        out = System.out;
        br = new BufferedReader(new InputStreamReader(System.in));
    }

    private int port = 52535;
    private Socket socket;
    private DataInputStream din;
    private DataOutputStream dout;
    private MainFrame mainFrame;
    private boolean isRunning;
    private String address;
    final int size = 4096000;
    final byte[] b = new byte[size];

    public LCClient(final MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        isRunning = false;
        (new Thread(() -> sessionStarted())).start();
    }

    public void sessionStarted() {
        address = JOptionPane.showInputDialog(mainFrame, "Please input IP-Address", "127.0.0.1");
        out.println("address : " + address);
        try {
            socket = new Socket(address, port);
            out.println("Client requested");
            din = new DataInputStream(socket.getInputStream());
            dout = new DataOutputStream(socket.getOutputStream());
//            sessionStarted();
        } catch (final IOException ioException) {
            ioException.printStackTrace();
            closeSession();
        }
        isRunning = true;
        String msg;
        long prevTime = 0, currentTime = 0;
        try {
            while (true) {
                /*out.print("Please enter some string : ");
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
                }*/
                dout.writeUTF(TRANSFER.FILE_TRANSFER.toString());
                final BufferedImage img = deserializeImage(din);
                if (img != null) {
                    mainFrame.setImage(img);
                    mainFrame.repaint();
                } else out.println("error image is null");
                currentTime = System.currentTimeMillis();
                if (!(currentTime - prevTime > delay)) {
                    System.out.println("delay of : " + (delay - (currentTime - prevTime)));
                    Thread.sleep(delay - (currentTime - prevTime));
                }
                prevTime = currentTime;
            }
        } catch (final IOException | InterruptedException exception) {
            exception.printStackTrace();
            closeSession();
        }
    }

    public BufferedImage deserializeImage(final DataInputStream din) throws IOException {
        BufferedImage img = null;
        int read;
        final int length = Integer.parseInt(din.readUTF());
        System.out.println(length);
        byte[] _img = new byte[length];
        int index = 0;
        while (index < length && (read = din.read(b)) != -1) {
//            printBytes(b,read);
            if (read == eof.length) {
                boolean EOF = true;
                for (int i = 0; i < eof.length; i++)
                    if (eof[i] != b[i]) {
                        EOF = false;
                        break;
                    }
                if (EOF) {
                    System.out.println(TRANSFER.EOF);
                    break;
                }
            }
            for (int i = 0; i < read && index < _img.length; i++)
                _img[index++] = b[i];
        }
        img = ImageIO.read(new ByteArrayInputStream(_img));
        return img;
    }

    public void closeSession() {
        out.println("Closing Session");
        isRunning = false;
        try {
            if (din != null)
                din.close();
            if (dout != null)
                dout.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public boolean isRunning() {
        return isRunning;
    }
}
