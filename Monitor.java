import java.util.Arrays;

class Monitor {
  private boolean rowPrinted[];
  private boolean rowWaiting[];

  public Monitor() {
    rowPrinted = new boolean[Main.image.length];
    Arrays.fill(rowPrinted, Boolean.FALSE);

    rowWaiting = new boolean[Main.image.length];
    Arrays.fill(rowWaiting, Boolean.FALSE);
  }

  public synchronized void writeRow(float[] row, int id) {
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

    // PRINT
    System.out.println("Row " + id + ": ");
    for (int i = 0; i < Main.image.length; i++) {
      System.out.printf(row[i] + " ");
    }
    System.out.println("");
    rowPrinted[id] = true;

    // notify next thread, if ready
    notifyAll();
  }

}
