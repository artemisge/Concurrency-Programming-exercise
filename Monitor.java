import java.io.IOException;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.awt.image.MemoryImageSource;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.util.ArrayList;

class Monitor {
  private boolean rowPrinted[];
  private boolean rowWaiting[];

  public Monitor() {
    rowPrinted = new boolean[Main.pixels2d.length];
    Arrays.fill(rowPrinted, Boolean.FALSE);

    rowWaiting = new boolean[Main.pixels2d.length];
    Arrays.fill(rowWaiting, Boolean.FALSE);
  }

  public synchronized void writeRow(int[] row, int id) {
    System.out.println("Row #" + id + " is done processing.");

    // wait if previous processes are not done yet
    while (id > 0 && !rowPrinted[id - 1]) {
      if (!rowWaiting[id]) {
        // if waiting for the first time
        rowWaiting[id] = true;
        System.out.println("Row #" + id + " is waiting to be printed.");
      }
      try {
        wait();
      } catch (InterruptedException e) {
      }
    }

    // PRINT ROW IN ORDER
    // System.out.println("Row " + id + ": ");
    // for (int i = 0; i < Main.pixels2d[0].length; i++) {
    // System.out.printf(row[i] + " ");
    // }
    // System.out.println("");

    // write row to final smooth-image array
    for (int i = 0; i < Main.NEWpixels2d[0].length; i++) {
      Main.NEWpixels2d[id][i] = row[i];
    }
    rowPrinted[id] = true;

    // notify all threads
    notifyAll();

    // now that is done, make final array
    if (id == Main.NEWpixels2d.length - 1) {
      outputImage();
    }
  }

  public void outputImage() {
    int[] array = Stream.of(Main.NEWpixels2d).flatMapToInt(IntStream::of).toArray();
    int width = Main.NEWpixels2d[0].length;
    int height = Main.NEWpixels2d.length;
    try {
      textToImage("output.jpeg", width, height, array);
    } catch (Exception e) {
      // TODO: handle exception
    }
    System.out.println("SIZE: w" + width + " x h" + height);
  }

  private static void textToImage(String path, int width, int height, int[] data) throws IOException {
    MemoryImageSource mis = new MemoryImageSource(width, height, data, 0, width);
    Image im = Toolkit.getDefaultToolkit().createImage(mis);

    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    bufferedImage.getGraphics().drawImage(im, 0, 0, null);
    ImageIO.write(bufferedImage, "jpg", new File(path));
  }
}
