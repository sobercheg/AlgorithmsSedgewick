import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Programming Assignment 1: Percolation.
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 */
public class Percolation {

  private final WeightedQuickUnionUF union;
  private final int n;
  private final boolean[] openSites;
  private boolean percolates;
  private final int topSite;
  private final int bottomSite;
  private int opened;


  public Percolation(int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("size should be greater than 0");
    }
    this.n = size;
    // add 2 more openSites connecting all upper and lower row openSites
    this.union = new WeightedQuickUnionUF(n * n + 2);
    this.openSites = new boolean[n * n];
    this.percolates = false;

    // Step 1. Union all upper row open sites to site (n * n)
    // Step 2. Union all lower row open sites to site (n * n + 1)
    this.topSite = n * n;
    this.bottomSite = n * n + 1;
  }

  public void open(int i, int j) {
    validate(i, j);
    openSites[xyTo1D(i, j)] = true;
    percolates = performExperiment();
    opened++;
  }

  public boolean isOpen(int i, int j) {
    validate(i, j);
    return openSites[xyTo1D(i, j)];
  }

  public boolean isFull(int i, int j) {
    validate(i, j);
    return union.connected(topSite, xyTo1D(i, j));
  }   // is site (row i, column j) full?

  public boolean percolates() {
    return percolates;
  }

  private boolean performExperiment() {
    for (int i = 1; i <= n; i++) {
      if (isOpen(1, i)) {
        union.union(xyTo1D(1, i), topSite);
      }
      if (isOpen(n, i)) {
        union.union(xyTo1D(n, i), bottomSite);
      }
    }

    // Step 3. Union all adjacent open sites
    // Traverse in a top-to-bottom left-to-right fashion with looking at the top and left neighbors.
    for (int times = 0; times < opened; times++) {
      for (int i = 1; i <= n; i++) {
        for (int j = 1; j <= n; j++) {
          int currentIndex = xyTo1D(i, j);
          int topIndex = xyTo1D(i - 1, j);
          // Union if both the current and top sites are open and connected to top
          if (i > 1 && isOpen(i, j) && isOpen(i - 1, j) && union.connected(topSite, topIndex)) {
            union.union(currentIndex, topIndex);
          }
          int leftIndex = xyTo1D(i, j - 1);
          // Union if both the current and left sites are open and connected to top
          if (j > 1 && isOpen(i, j) && isOpen(i, j - 1) && union.connected(topSite, leftIndex)) {
            union.union(currentIndex, leftIndex);
          }
          int bottomIndex = xyTo1D(i + 1, j);
          // Union if both the current and bottom sites are open and connected to top
          if (i < n && isOpen(i, j) && isOpen(i + 1, j) && union.connected(topSite, bottomIndex)) {
            union.union(currentIndex, bottomIndex);
          }
          int rightIndex = xyTo1D(i, j + 1);
          // Union if both the current and right sites are open and connected to top
          if (j < n && isOpen(i, j) && isOpen(i, j + 1) && union.connected(topSite, rightIndex)) {
            union.union(currentIndex, rightIndex);
          }
        }
      }
    }

    // Step 4. Check if the two additional sites connecting the top and bottom rows belong to the same component
    return union.connected(topSite, bottomSite);
  }

  private void validate(int i, int j) {
    if (i <= 0 || i > n || j <= 0 || j > n) {
      throw new IndexOutOfBoundsException(String.format("Invalid indices: %d, %d", i, j));
    }
  }

  private int xyTo1D(int i, int j) {
    return (i - 1) * n + j - 1;
  }

  public static void main(String[] args) {
    testIsFull();
    testFullyOpen(2);
    testFullyBlocked(2);
    testInput6();
  }

  private static void testIsFull() {
    Percolation percolation = new Percolation(2);
    assertTrue(!percolation.isFull(1, 1), "Not full");
    percolation.open(2, 2);
    assertTrue(!percolation.isFull(1, 1), "Not full");
    assertTrue(!percolation.isFull(2, 2), "Not full");
    percolation.open(1, 2);
    assertTrue(percolation.isFull(1, 2), "Full");
    assertTrue(percolation.isFull(2, 2), "Full");
  }

  private static void testFullyOpen(int size) {
    Percolation percolation = new Percolation(size);
    for (int i = 1; i <= size; i++) {
      for (int j = 1; j <= size; j++) {
        percolation.open(i, j);
      }
    }
    assertTrue(percolation.percolates(), "Fully open");
  }

  private static void testFullyBlocked(int size) {
    Percolation percolation = new Percolation(size);
    assertTrue(!percolation.percolates(), "Fully blocked");
  }

  private static void testInput6() {
    In in = new In("tests/percolation/input6.txt");
    int size = in.readInt();
    Percolation percolation = new Percolation(size);
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    // 6 opened
    assertTrue(percolation.isFull(5, 5), "(5, 5) should be full");

    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    percolation.open(in.readInt(), in.readInt());
    // 18 opened
    assertTrue(percolation.percolates(), "Percolates after 18 sites opened");
    assertTrue(percolation.isOpen(5, 4), "(5, 4) should be open");
    in.close();
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
