package testing;

import enums.TRANSFER;
import img.LCImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * @dev :   devpar
 * @date :   06-Jun-2021
 */
public class Deserializing extends JFrame{
    int port=54255;
    Socket socket;
    LCImage img;
    public Deserializing()throws Exception{
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        img=new LCImage(null);
        socket=new Socket("127.0.0.1",port);
        DataInputStream in=new DataInputStream(socket.getInputStream());
//        img.readObject(in);
//        in.defaultReadObject();
//        in.readUTF();
        /*String str=in.readUTF();
        int index=str.indexOf('-');
        int length=Integer.parseInt(str.substring(0,index));
        byte b[]=new byte[length];
        String data=str.substring(index+1,str.length());
        for(int i=0;i<data.length();i++){
            char c=data.charAt(i);
            b[i]=(byte)c;
        }*/
        if(in.readUTF().equals(TRANSFER.FILE_TRANSFER.toString())){
            BufferedImage _img=deserializeImage(in);
            img=new LCImage(_img);
            System.out.println(img);
            setSize(img.img.getWidth(),img.img.getHeight());
            setVisible(true);
            repaint();
        }
    }
    public static BufferedImage deserializeImage(DataInputStream din)throws Exception{
        BufferedImage img=null;
        int size=4096;
        byte []b=new byte[size];
        byte eof[]= TRANSFER.EOF.toString().getBytes();
        int read;
        int length=Integer.parseInt(din.readUTF());
        System.out.println(length);
        byte[]_img=new byte[length];
        int index=0;
        while(index<length && (read=din.read(b))!=-1){
            printBytes(b,read);
            if(read==eof.length){
                boolean EOF=true;
                for(int i=0;i<eof.length;i++)
                    if(eof[i]!=b[i]){
                        EOF=false;
                        break;
                    }
                if(EOF){
                    System.out.println(TRANSFER.EOF);
                    break;
                }
            }
            for(int i=0;i<read && index<_img.length;i++)
                _img[index++]=b[i];
        }
        img=ImageIO.read(new ByteArrayInputStream(_img));
        return img;
    }
    public static void main(String $[])throws Exception{
        new Deserializing();
    }
    public static void printBytes(byte []bytes,int size){
        for(int i=0;i<size;i++)
            System.out.print(bytes[i]);
        System.out.println();
    }
    @Override
    public void paint(Graphics g){
        if(img!=null && img.img!=null)
            g.drawImage(img.img,0,0,this);
    }
}
