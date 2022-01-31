import java.io.*;
import java.lang.Thread;

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

  public static void main(String[] args) {

    // System.out.println(image.length);

    float newimage[][] = new float[image.length][image[0].length];

    Monitor monitor = new Monitor();

    // array of threads - subparts of image
    ImgThread[] imagethreads = new ImgThread[image.length];
    // create a thread for every row
    for (int i = 0; i < image.length; i++) {
      imagethreads[i] = new ImgThread(i, monitor);
      Thread t = new Thread(imagethreads[i]);
      t.start();
    }
  }
}
