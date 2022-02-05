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
    // Random rand = new Random();
    // try {
    //   int wt = rand.nextInt(5000);
    //   System.out.println(wt);
    //   Thread.sleep(wt);
    // } catch (Exception e) {
    //   // TODO: handle exception
    // }

    // process entire row
    int[] smoothRow = smoothImage();
    // only if previous rows are already printed, print this one as well
    monitor.writeRow(smoothRow, id);
  }

  private void addpixel(int pix3[], int pix1) {
    pix3[0] += pix1 % 256;
    pix3[1] += (pix1 / 256) % 256;
    pix3[2] += (pix1 / 256 / 256) % 256;
  }

  private void normalizepixel(int pix3[], int total) {
    pix3[0] /= total;
    pix3[1] /= total;
    pix3[2] /= total;
  }

  private int pix3topix1(int pix3[]) {
    return pix3[0] + pix3[1] * 256 + pix3[2] * 256 * 256;
  }

  private int[] smoothImage() {
    int[][] img = Main.pixels2d;
    int newRow[] = new int[img[0].length];
    for (int i = 0; i < img[0].length; i++) {
      int newpixel[] = { 0, 0, 0 };
      // calculate
      int totalNeighbours = 0;
      if (id + 1 < img.length) {
        addpixel(newpixel, img[id + 1][i]);
        totalNeighbours++;
      }
      if (id - 1 > -1) {
        addpixel(newpixel, img[id - 1][i]);
        totalNeighbours++;
      }

      if (i + 1 < img[0].length) {
        addpixel(newpixel, img[id][i + 1]);
        totalNeighbours++;
      }

      if (i - 1 > -1) {
        addpixel(newpixel, img[id][i - 1]);
        totalNeighbours++;
      }
      addpixel(newpixel, totalNeighbours * img[id][i]);
      normalizepixel(newpixel, totalNeighbours * 2);
      newRow[i] = pix3topix1(newpixel);
    }
    return newRow;
  }

}
