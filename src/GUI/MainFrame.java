package GUI;

import enums.CONNECTION;
import socket.LCClient;
import socket.LCServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            if(connectOrDisconnect.getLabel().equals(CONNECTION.CONNECT.toString())){
                connectOrDisconnect.setLabel(CONNECTION.DISCONNECT.toString());
                SwingUtilities.invokeLater(()->{client=new LCClient(this);});
                out.println("client invoked");
//                client=new LCClient(this);
            }else if(connectOrDisconnect.getLabel().equals(CONNECTION.DISCONNECT.toString())){
                connectOrDisconnect.setLabel(CONNECTION.CONNECT.toString());
                client.closeSession();
            }
        }
        if(e.getSource()== startOrStopServer){
            if(startOrStopServer.getLabel().equals(CONNECTION.START_SERVER.toString())){
                startOrStopServer.setLabel(CONNECTION.STOP_SERVER.toString());
                SwingUtilities.invokeLater(()->{server=new LCServer(this);});
                out.println("server invoked");
//                server=new LCServer(this);
            }else if(startOrStopServer.getLabel().equals(CONNECTION.STOP_SERVER.toString())){
                connectOrDisconnect.setLabel(CONNECTION.START_SERVER.toString());
                client.closeSession();
            }
        }
    }
}
