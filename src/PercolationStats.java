import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

/**
 * Programming Assignment 1: Percolation.
 * http://coursera.cs.princeton.edu/algs4/assignments/percolation.html
 */
public class PercolationStats {

  private final int size;
  private final int experiments;
  private final double[] openSitesFraction;

  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) {
      throw new IllegalArgumentException("N or T must be greater than 0");
    }
    this.size = N;
    this.experiments = T;
    this.openSitesFraction = performAllExperiments();
  }

  public double mean() {
    return StdStats.mean(openSitesFraction);
  }

  public double stddev() {
    return StdStats.stddev(openSitesFraction);
  }

  public double confidenceLo() {
    return mean() - 1.96 * stddev() / Math.sqrt(experiments);
  }

  public double confidenceHi() {
    return mean() + 1.96 * stddev() / Math.sqrt(experiments);
  }

  /**
   * Repeats the experiment with increasing number of randomly open sites. Returns the number of
   * open sites which results in percolation.
   */
  private int performExperiment() {
    Percolation percolation = new Percolation(size);
    int openSites = 0;
    for (int i = 0; i < size * size; i++) {
      int row;
      int col;
      // Find a site which is not open yet and open it
      do {
        row = StdRandom.uniform(size) + 1;
        col = StdRandom.uniform(size) + 1;
      } while (percolation.isOpen(row, col));
      percolation.open(row, col);
      openSites++;
      if (percolation.percolates()) {
        break;
      }
    }
    return openSites;
  }

  private double[] performAllExperiments() {
    double[] openSites = new double[experiments];
    for (int i = 0; i < experiments; i++) {
      openSites[i] = (double) performExperiment() / (size * size);
    }
    return openSites;
  }

  public static void main(String[] args) {
    int size = Integer.parseInt(args[0]);
    int experiments = Integer.parseInt(args[1]);
    PercolationStats stats = new PercolationStats(size, experiments);
    StdOut.printf("mean                    = %f%n", stats.mean());
    StdOut.printf("stddev                  = %f%n", stats.stddev());
    StdOut.printf("95%% confidence interval = %f, %f%n", stats.confidenceLo(), stats.confidenceHi());
  }
}
