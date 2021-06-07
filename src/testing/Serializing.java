package testing;

import enums.TRANSFER;
import img.LCImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @dev :   devpar
 * @date :   06-Jun-2021
 */
public class Serializing {
    static final PrintStream out=System.out;
    public static void main(String $[])throws Exception{
        int port=54255;
        LCImage img=createScreenCapture();
        ServerSocket serverSocket=new ServerSocket(port);
        Socket socket=serverSocket.accept();
//        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
//        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(System.out));
        DataOutputStream out=new DataOutputStream(socket.getOutputStream());
        serialize(out,img.img);
        /*ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(img.img,"png",baos);
        baos.flush();
        byte by[]=baos.toByteArray();
        String len=String.valueOf(by.length);
        StringBuilder s=new StringBuilder("");
        for(byte b:by)
            s.append(b);
        out.writeUTF(len+"-"+s.toString());
        out.flush();*/
//        img.writeObject(out);
//        out.writeObject(img);
//        out.defaultWriteObject();
//        out.writeObject(img);
//        out.close();
    }
    public static void serialize(DataOutputStream out,BufferedImage img)throws Exception{
        out.writeUTF(TRANSFER.FILE_TRANSFER.toString());
        out.flush();
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(img,"png",baos);
        //baos.flush();
        byte by[]=baos.toByteArray();
        System.out.println("image : ");
        printBytes(by,0,by.length);
        out.writeUTF(String.valueOf(by.length));
        out.flush();
//        ByteArrayInputStream bin=new ByteArrayInputStream(by);
//        int read;
        int size=4096;
        System.out.println("length : "+by.length);
        for(int i=0;by.length>i*size;i++){
            out.write(by,i*size,Math.min(by.length-i*size,size));
            out.flush();
            printBytes(by,i*size,Math.min(by.length-i*size,size));
        }
        /*byte[]eof=TRANSFER.EOF.toString().getBytes();
        System.out.println("Writing : "+TRANSFER.EOF.toString());
        printBytes(eof,0,eof.length);
        out.write(eof);
        out.flush();*/
    }
    public static void printBytes(byte []bytes,int start,int count){
        System.out.println("start : "+start+" count : "+ count);
        for(int i=start;i<count+start;i++)
            System.out.print(bytes[i]);
        System.out.println();
    }
    public static LCImage createScreenCapture(){
        LCImage image=null;
        try{
            BufferedImage img=(new Robot()).createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            image=new LCImage(img);
        }catch(AWTException awtException){
            awtException.printStackTrace();
        }
        return image;
    }
}
