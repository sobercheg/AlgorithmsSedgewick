/**
 * Programming Assignment 4.
 * 8 Puzzle. http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 */
public class Board {

  // (where blocks[i][j] = block in row i, column j)
  private final int[][] blocks;

  public Board(int[][] blocks) {
    this.blocks = blocks;
  }

  public int dimension() {
    return blocks.length;
  }

  /**
   * Number of blocks out of place
   */
  public int hamming() {
    return 0;
  }

  /**
   * Sum of Manhattan distances between blocks and goal
   */
  public int manhattan() {
    return 0;
  }

  public boolean isGoal() {
    return false;
  }

  /**
   * A board that is obtained by exchanging any pair of blocks
   */
  public Board twin() {
    return null;
  }

  public boolean equals(Object y) {
    return false;
  }

  /**
   * All neighboring boards
   */
  public Iterable<Board> neighbors() {
    return null;
  }

  /**
   * The input and output format for a board is the board dimension N followed by the N-by-N initial
   * board, using 0 to represent the blank square. As an example,
   * <pre>
   * 3
   *  0  1  3
   *  4  2  5
   *  7  8  6
   * </pre>
   */
  public String toString() {
    return "";
  }

  public static void main(String[] args) {

  }
}
