import java.util.Arrays;
import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;

/**
 * Programming Assignment 4.
 * 8 Puzzle. http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 */
public class Board {

  // (where blocks[i][j] = block in row i, column j)
  private final int[][] blocks;
  private final int n;

  public Board(int[][] blocks) {
    this.blocks = new int[blocks.length][blocks[0].length];
    for (int i = 0; i < blocks.length; i++) {
      assert blocks.length == blocks[i].length : "Board should be a square";
      for (int j = 0; j < blocks[i].length; j++) {
        this.blocks[i][j] = blocks[i][j];
      }
    }
    this.n = blocks.length;
  }

  public int dimension() {
    return n;
  }

  /**
   * Number of blocks out of place Hamming priority function. The number of blocks in the wrong
   * position, plus the number of moves made so far to get to the search node. Intuitively, a search
   * node with a small number of blocks in the wrong position is close to the goal, and we prefer a
   * search node that have been reached using a small number of moves.
   */
  public int hamming() {
    int wrongBlocks = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] > 0 && blocks[i][j] != i * n + j + 1) {
          wrongBlocks++;
        }
      }
    }
    return wrongBlocks;
  }

  /**
   * Sum of Manhattan distances between blocks and goal. The sum of the Manhattan distances (sum of
   * the vertical and horizontal distance) from the blocks to their goal positions, plus the number
   * of moves made so far to get to the search node.
   */
  public int manhattan() {
    int wrongBlocks = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] == 0) {
          continue;
        }
        int[] desiredPosition = getPosition(blocks[i][j]);
        wrongBlocks += Math.abs(i - desiredPosition[0]) + Math.abs(j - desiredPosition[1]);
      }
    }
    return wrongBlocks;
  }

  private int[] getPosition(int value) {
    if (value == 0) {
      return new int[]{n - 1, n - 1};
    }
    return new int[]{(value - 1) / n, (value - 1) % n};
  }

  public boolean isGoal() {
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        int[] desiredPosition = getPosition(blocks[i][j]);
        if (desiredPosition[0] != i || desiredPosition[1] != j) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * A board that is obtained by exchanging any pair of blocks.
   */
  public Board twin() {
    int[][] twin = getClone();

    if (twin[0][0] > 0 && twin[0][1] > 0) {
      swap(twin, 0, 0, 0, 1);
    } else if (twin[0][0] > 0 && twin[1][0] > 0) {
      swap(twin, 0, 0, 1, 0);
    } else if (twin[0][1] > 0 && twin[1][1] > 0) {
      swap(twin, 0, 1, 1, 1);
    } else if (twin[1][0] > 0 && twin[1][1] > 0) {
      swap(twin, 1, 0, 1, 1);
    } else {
      throw new IllegalStateException(
          "Cannot exchange a pair of blocks. That should never happen! Debug!");
    }
    return new Board(twin);
  }

  private int[][] getClone() {
    int[][] clone = new int[n][n];
    for (int i = 0; i < blocks.length; i++) {
      for (int j = 0; j < blocks[i].length; j++) {
        clone[i][j] = blocks[i][j];
      }
    }
    return clone;
  }

  private void swap(int[][] array, int i1, int j1, int i2, int j2) {
    int tmp = array[i1][j1];
    array[i1][j1] = array[i2][j2];
    array[i2][j2] = tmp;
  }
  public boolean equals(Object y) {
    if (this == y) {
      return true;
    }
    if (y == null) {
      return false;
    }
    if (this.getClass() != y.getClass()) {
      return false;
    }
    Board that = (Board) y;
    if (that.blocks.length != blocks.length) {
      return false;
    }

    for (int i = 0; i < n; i++) {
      if (!Arrays.equals(blocks[i], that.blocks[i])) {
        return false;
      }
    }
    return true;
  }

  /**
   * All neighboring boards.
   */
  public Iterable<Board> neighbors() {
    int zeroRow = 0;
    int zeroCol = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (blocks[i][j] == 0) {
          zeroRow = i;
          zeroCol = j;
        }
      }
    }

    Queue<Board> neighbors = new Queue<>();
    // Right neighbor
    if (zeroCol < n - 1) {
      int[][] neighbor = getClone();
      swap(neighbor, zeroRow, zeroCol, zeroRow, zeroCol + 1);
      neighbors.enqueue(new Board(neighbor));
    }
    // Left neighbor
    if (zeroCol > 0) {
      int[][] neighbor = getClone();
      swap(neighbor, zeroRow, zeroCol, zeroRow, zeroCol - 1);
      neighbors.enqueue(new Board(neighbor));
    }
    // Bottom neighbor
    if (zeroRow < n - 1) {
      int[][] neighbor = getClone();
      swap(neighbor, zeroRow, zeroCol, zeroRow + 1, zeroCol);
      neighbors.enqueue(new Board(neighbor));
    }
    // Top neighbor
    if (zeroRow > 0) {
      int[][] neighbor = getClone();
      swap(neighbor, zeroRow, zeroCol, zeroRow - 1, zeroCol);
      neighbors.enqueue(new Board(neighbor));
    }
    return neighbors;
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
    StringBuilder s = new StringBuilder();
    s.append(n).append("\n");
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
            s.append(String.format("%2d ", blocks[i][j]));
        }
        s.append("\n");
    }
    return s.toString();
  }

  public static void main(String[] args) {
    Board board = new Board(new int[][]{
        {0, 1, 3},
        {4, 2, 5},
        {7, 8, 6}
    });
    assertEquals(board.dimension(), 3, "dimension");
    assertEquals(board.hamming(), 4, "hamming");
    assertEquals(board.manhattan(), 4, "manhattan");
    assertEquals(board.isGoal(), false, "not goal");
    assertEquals(new Board(new int[][]{
        {1, 2, 3},
        {4, 5, 6},
        {7, 8, 0}
    }).isGoal(), true, "goal");
    assertEquals(board.toString(), "3\n"
        + " 0  1  3 \n"
        + " 4  2  5 \n"
        + " 7  8  6 \n", "toString()");

    Iterator<Board> neighbors = board.neighbors().iterator();
    assertEquals(neighbors.next(), new Board(new int[][] {
        {1, 0, 3},
        {4, 2, 5},
        {7, 8, 6}
    }), "right neighbor");
    assertEquals(neighbors.next(), new Board(new int[][] {
        {4, 1, 3},
        {0, 2, 5},
        {7, 8, 6}
    }), "bottom neighbor");
    assertEquals(neighbors.hasNext(), false, "no more neighbors");

    assertEquals(board.twin(), new Board(new int[][] {
        {0, 2, 3},
        {4, 1, 5},
        {7, 8, 6}
    }), "twin");
  }

  private static void assertEquals(Object actual, Object expected, String msg) {
    if (actual.equals(expected)) {
      System.out.printf("%s: Passed. Actual: %s%n", msg, actual);
    } else {
      System.err.printf("%s: Failed. Actual: %s, expected: %s%n", msg, actual, expected);
    }
  }
}
