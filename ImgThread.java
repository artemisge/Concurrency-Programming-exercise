import java.io.*;
import java.lang.Thread;
import java.util.Random;

public class ImgThread implements Runnable {

  public int[] imgarray;
  public int id;
  private Monitor monitor;

  public ImgThread(int id, Monitor monitor) {
    this.monitor = monitor;
    this.id = id;
  }

  @Override
  public void run() {
    Random rand = new Random();
    try {
      int wt = rand.nextInt(5000);
      //System.out.println(wt);
      Thread.sleep(wt);
    } catch (Exception e) {
      // TODO: handle exception
    }

    // process entire row
    float[] smoothRow = smoothImage();
    // only if previous rows are already printed, print this one as well
    monitor.writeRow(smoothRow, id);
  }

  private float[] smoothImage() {
    float[][] img = Main.image; // swallow-copy to be easier to write
    float newRow[] = new float[img.length];
    for (int i = 0; i < img.length; i++) {
      float newpixel = 0;
      // calculate
      int totalNeighbours = 0;
      if (id + 1 < img.length) {
        newpixel += img[id + 1][i];
        totalNeighbours++;
      }
      if (id - 1 > -1) {
        newpixel += img[id - 1][i];
        totalNeighbours++;
      }

      if (i + 1 < img.length) {
        newpixel += img[id][i + 1];
        totalNeighbours++;
      }

      if (i - 1 > -1) {
        newpixel += img[id][i - 1];
        totalNeighbours++;
      }

      newpixel += totalNeighbours * img[id][i];
      newpixel /= (float) totalNeighbours * 2;
      newRow[i] = newpixel;
    }
    return newRow;
  }

}
