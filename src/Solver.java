import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Programming Assignment 4.
 * 8 puzzle. http://coursera.cs.princeton.edu/algs4/assignments/8puzzle.html
 */
public class Solver {

  private boolean isSolvable;
  private SearchNode solutionNode;

  /**
   * Find a solution to the initial board (using the A* algorithm)
   */
  public Solver(Board initial) {
    if (initial == null) {
      throw new NullPointerException("Initial board cannot be null");
    }
    MinPQ<SearchNode> q = new MinPQ<>(new SearchNodeComparator());
    MinPQ<SearchNode> qTwin = new MinPQ<>(new SearchNodeComparator());
    q.insert(new SearchNode(initial, null, 0));
    qTwin.insert(new SearchNode(initial.twin(), null, 0));

    do {
      SearchNode node = q.delMin();
      SearchNode twinNode = qTwin.delMin();
      if (node.board.isGoal()) {
        solutionNode = node;
        isSolvable = true;
        break;
      }
      if (twinNode.board.isGoal()) {
        isSolvable = false;
        break;
      }
      SearchNode previous = node.previous;
      for (Board neighbor : node.board.neighbors()) {
        if (previous == null || !neighbor.equals(previous.board)) {
          q.insert(new SearchNode(neighbor, node, node.moves + 1));
        }
      }
      SearchNode previousTwin = twinNode.previous;
      for (Board neighbor : twinNode.board.neighbors()) {
        if (previousTwin == null || !neighbor.equals(previousTwin.board)) {
          qTwin.insert(new SearchNode(neighbor, twinNode, twinNode.moves + 1));
        }
      }
    } while (true);
  }

  private class SearchNodeComparator implements Comparator<SearchNode> {

    @Override
    public int compare(SearchNode o1, SearchNode o2) {
      return Integer.compare(o1.board.manhattan() + o1.moves, o2.board.manhattan() + o2.moves);
    }
  }

  private class SearchNode implements Comparable<SearchNode> {

    private Board board;
    private SearchNode previous;
    private int moves;

    SearchNode(Board board, SearchNode previous, int moves) {
      this.board = board;
      this.previous = previous;
      this.moves = moves;
    }

    @Override
    public int compareTo(SearchNode o) {
      return Integer.compare(board.manhattan() + moves, o.board.manhattan() + o.moves);
    }
  }

  public boolean isSolvable() {
    return isSolvable;
  }

  /**
   * Min number of moves to solve initial board; -1 if unsolvable
   */
  public int moves() {
    if (solutionNode != null) {
      return solutionNode.moves;
    }
    return -1;
  }

  /**
   * Sequence of boards in a shortest solution; null if unsolvable
   */
  public Iterable<Board> solution() {
    if (solutionNode == null) {
      return null;
    }
    Stack<Board> solution = new Stack<>();
    SearchNode node = solutionNode;
    while (node != null) {
      solution.push(node.board);
      node = node.previous;
    }
    return solution;
  }

  /**
   * Solve a slider puzzle (given below)
   */
  public static void main(String[] args) {
    // for each command-line argument
    for (String filename : args) {

      // read in the board specified in the filename
      In in = new In(filename);
      int n = in.readInt();
      int[][] tiles = new int[n][n];
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          tiles[i][j] = in.readInt();
        }
      }

      // solve the slider puzzle
      Board initial = new Board(tiles);
      Solver solver = new Solver(initial);
      StdOut.println(filename);
      StdOut.println("Minimum number of moves = " + solver.moves());
      StdOut.println();
      for (Board board : solver.solution()) {
        StdOut.println(board);
      }
    }
  }

  private static void assertEquals(Object actual, Object expected, String msg) {
    if (actual.equals(expected)) {
      System.out.printf("%s: Passed. Actual: %s%n", msg, actual);
    } else {
      System.err.printf("%s: Failed. Actual: %s, expected: %s%n", msg, actual, expected);
    }
  }
}
