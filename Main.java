import java.io.*;
import java.lang.Thread;
import java.util.Arrays;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.image.PixelGrabber;

public class Main {
  // example image
  public static float image[][] = {
      { 1, 2, 3, 1, 2, 3 },
      { 4, 5, 6, 9, 8, 7 },
      { 1, 2, 3, 1, 2, 3 },
      { 4, 5, 6, 9, 8, 7 },
      { 1, 2, 3, 1, 2, 3 },
      { 4, 5, 6, 9, 8, 7 }
  };
  public static int pixels2d[][];
  public static int NEWpixels2d[][];

  public static void loadImage() {
    try {
      BufferedImage image = ImageIO.read(new File("image2.jpg"));
      int width = image.getWidth();
      int height = image.getHeight();
      
      System.out.println(width + "x" + height);
      pixels2d = new int[height][width];

      for (int y = 0; y < height; y++)
        for (int x = 0; x < width; x++)
          pixels2d[y][x] = image.getRGB(x, y);

    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    loadImage();
    
    // System.out.println(Arrays.deepToString(pixels2d));
    NEWpixels2d = new int[pixels2d.length][pixels2d[0].length];

    Monitor monitor = new Monitor();

    // array of threads - subparts of image
    ImgThread[] imagethreads = new ImgThread[pixels2d.length];
    // create a thread for every row
    for (int i = 0; i < pixels2d.length; i++) {
      imagethreads[i] = new ImgThread(i, monitor);
      Thread t = new Thread(imagethreads[i]);
      t.start();
    }
  }
}
