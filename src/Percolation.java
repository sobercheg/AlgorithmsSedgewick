import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Programming Assignment 1: Percolation.
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 */
public class Percolation {

  private final WeightedQuickUnionUF union;
  private final int n;
  private final boolean[] openSites;

  public Percolation(int size) {
    this.n = size;
    // add 2 more openSites connecting all upper and lower row openSites
    this.union = new WeightedQuickUnionUF(n * n + 2);
    this.openSites = new boolean[n * n];
  }

  public void open(int i, int j) {
    validate(i, j);
    openSites[xyTo1D(i, j)] = true;
  }          // open site (row i, column j) if it is not open already

  public boolean isOpen(int i, int j) {
    validate(i, j);
    return openSites[xyTo1D(i, j)];
  }     // is site (row i, column j) open?

  public boolean isFull(int i, int j) {
    return !isOpen(i, j);
  }   // is site (row i, column j) full?

  public boolean percolates() {
    // Step 1. Union all upper row open sites to site (n * n)
    // Step 2. Union all lower row open sites to site (n * n + 1)
    int topSite = n * n;
    int bottomSite = n * n + 1;
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
    for (int i = 1; i <= n; i++) {
      for (int j = 1; j <= n; j++) {
        int currentIndex = xyTo1D(i, j);
        if (i > 1) {
          int topIndex = xyTo1D(i - 1, j);
          // Union if both the current and top sites are open
          if (isOpen(i, j) && isOpen(i - 1, j)) {
            union.union(currentIndex, topIndex);
          }
        }
        if (j > 1) {
          int leftIndex = xyTo1D(i, j - 1);
          // Union if both the current and left sites are open
          if (isOpen(i, j) && isOpen(i, j - 1)) {
            union.union(currentIndex, leftIndex);
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
    testFullyOpen(2);
    testFullyBlocked(2);
  }  // test client (optional)

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

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
