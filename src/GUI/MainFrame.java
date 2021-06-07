package GUI;

import enums.CONNECTION;
import socket.LCClient;
import socket.LCServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.PrintStream;

/**
 * @dev :   devpar
 * @date :   05-Jun-2021
 */
public class MainFrame extends JFrame implements ActionListener {
    private static final PrintStream out;
    static{
        out=System.out;
    }
    private JMenuBar menuBar;
    private JMenu connection;
    private JMenuItem connectOrDisconnect;
    private JMenuItem startOrStopServer;
    private LCServer server;
    private LCClient client;
    private BufferedImage img;
    public MainFrame(){
        Toolkit toolkit=Toolkit.getDefaultToolkit();
        Dimension screenSize=toolkit.getScreenSize();
        setSize((int)screenSize.getWidth()/2,(int)screenSize.getHeight()/2);
        setLocation(((int)screenSize.getWidth()/2-getWidth()/2),(int)screenSize.getHeight()/2-getHeight()/2);
        setTitle("LetsConnect");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initializeMenus();
        setVisible(true);
    }
    private void initializeMenus(){
        menuBar=new JMenuBar();
        connection=new JMenu("Connection");
        connectOrDisconnect =new JMenuItem(CONNECTION.CONNECT.toString());
        connectOrDisconnect.addActionListener(this);
        startOrStopServer =new JMenuItem(CONNECTION.START_SERVER.toString());
        startOrStopServer.addActionListener(this);
        connection.add(connectOrDisconnect);
        connection.add(startOrStopServer);
        menuBar.add(connection);
        setJMenuBar(menuBar);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()== connectOrDisconnect){
            if(connectOrDisconnect.getText().equals(CONNECTION.CONNECT.toString())){
                connectOrDisconnect.setText(CONNECTION.DISCONNECT.toString());
//                SwingUtilities.invokeLater(()->{client=new LCClient(this);});
                client=new LCClient(this);
                out.println("client invoked");
            }else if(connectOrDisconnect.getText().equals(CONNECTION.DISCONNECT.toString())){
                connectOrDisconnect.setText(CONNECTION.CONNECT.toString());
                client.closeSession();
            }
        }
        if(e.getSource()== startOrStopServer){
            if(startOrStopServer.getText().equals(CONNECTION.START_SERVER.toString())){
                startOrStopServer.setText(CONNECTION.STOP_SERVER.toString());
//                SwingUtilities.invokeLater(()->{server=new LCServer(this);});
                server=new LCServer(this);
                out.println("server invoked");
//                server=new LCServer(this);
            }else if(startOrStopServer.getText().equals(CONNECTION.STOP_SERVER.toString())){
                startOrStopServer.setText(CONNECTION.START_SERVER.toString());
                server.closeSession();
            }
        }
    }
    @Override
    public void paint(Graphics g){
        if(img!=null){
            g.drawImage(img,0,0,this);
        }
    }
    public void setImage(BufferedImage img){
        this.img=img;
        repaint();
    }
}
