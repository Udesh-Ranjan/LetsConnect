package img;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

/**
 * @dev :   devpar
 * @date :   06-Jun-2021
 */
public class LCImage implements Serializable {
    public transient BufferedImage img;

    public LCImage(BufferedImage img) {
        this.img = img;
    }

    public void writeObject(ObjectOutputStream out) {
        try {
            out.defaultWriteObject();
            System.out.println("writing image");
//            out.writeChars("BufferedImage");
            ImageIO.write(img, "png", out);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void readObject(ObjectInputStream in) {
        try {
            in.defaultReadObject();
            img = ImageIO.read(in);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }
    }
}
