import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 * Programming Assignment 2: Randomized Queues and Deques
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 */
public class Subset {

  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);
    RandomizedQueue<String> q = new RandomizedQueue<>();
    while (!StdIn.isEmpty()) {
      q.enqueue(StdIn.readString());
    }
    for (int i = 0; i < k; i++) {
      StdOut.printf("%s%n", q.dequeue());
    }
  }
}
