import java.util.Arrays;

import edu.princeton.cs.algs4.Queue;

/**
 * Programming Assignment 3: Pattern Recognition.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 */
public class BruteCollinearPoints {

  private final LineSegment[] segments;

  /**
   * Finds all line segments containing 4 points.
   */
  public BruteCollinearPoints(Point[] pointsInput) {
    if (pointsInput == null) {
      throw new NullPointerException("null array");
    }
    Point[] points = new Point[pointsInput.length];
    System.arraycopy(pointsInput, 0, points, 0, pointsInput.length);
    for (int p = 0; p < points.length - 1; p++) {
      checkPoint(points[p]);
      for (int q = p + 1; q < points.length; q++) {
        checkPoint(points[q]);
        if (points[p].compareTo(points[q]) == 0) {
          throw new IllegalArgumentException("equal points: " + points[p]);
        }
      }
    }
    if (pointsInput.length < 4) {
      segments = new LineSegment[0];
      return;
    }
    Queue<LineSegment> segmentQueue = new Queue<>();
    for (int p = 0; p < points.length - 3; p++) {
      for (int q = p + 1; q < points.length - 2; q++) {
        for (int r = q + 1; r < points.length - 1; r++) {
          for (int s = r + 1; s < points.length; s++) {
            if (points[p].slopeTo(points[q]) == points[p].slopeTo(points[r])
                && points[p].slopeTo(points[q]) == points[p].slopeTo(points[s])) {
              Point[] line = new Point[] {points[p], points[q], points[r], points[s]};
              Arrays.sort(line);
              segmentQueue.enqueue(new LineSegment(line[0], line[3]));
            }
          }
        }
      }
    }
    segments = new LineSegment[segmentQueue.size()];
    for (int i = 0; i < segments.length; i++) {
      segments[i] = segmentQueue.dequeue();
    }
  }

  public int numberOfSegments() {
    return segments.length;
  }

  public LineSegment[] segments() {
    LineSegment[] s = new LineSegment[segments.length];
    System.arraycopy(segments, 0, s, 0, segments.length);
    return s;
  }

  private void checkPoint(Point p) {
    if (p == null) {
      throw new NullPointerException("null point");
    }
  }

  public static void main(String[] args) {

    try {
      new BruteCollinearPoints(new Point[] {new Point(14906, 16894), new Point(14906, 16894)});
      throw new IllegalStateException("Duplicate points");
    } catch (IllegalArgumentException e) {
      // OK
    }

    Point[] points = new Point[] {
        new Point(0, 0),
        new Point(1, 1),
        new Point(2, 2),
        new Point(3, 3),
        new Point(3, 4)
    };
    BruteCollinearPoints collinearPoints = new BruteCollinearPoints(points);
    assertEquals(collinearPoints.numberOfSegments(), 1, "1 segment expected");
    assertEquals(collinearPoints.segments()[0].toString(), "(0, 0) -> (3, 3)", "segment");

    points = new Point[] {
        new Point(0, 0),
        new Point(1, 2),
        new Point(1, 3),
        new Point(3, 4),
        new Point(4, 3),
        new Point(99, 100),
    };
    collinearPoints = new BruteCollinearPoints(points);
    assertEquals(collinearPoints.numberOfSegments(), 0, "0 segment expected");

    points = new Point[]{
        new Point(10000, 0),
        new Point(0, 10000),
        new Point(3000, 7000),
        new Point(7000, 3000),
        new Point(20000, 21000),
        new Point(3000, 4000),
        new Point(14000, 15000),
        new Point(6000, 7000),
    };
    collinearPoints = new BruteCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(10000, 0) -> (0, 10000), (3000, 4000) -> (20000, 21000)]", "segments");

  }

  private static void assertEquals(Object actual, Object expected, String msg) {
    if (actual.equals(expected)) {
      System.out.printf("%s: Passed. Actual: %s%n", msg, actual);
    } else {
      System.err.printf("%s: Failed. Actual: %s, expected: %s%n", msg, actual, expected);
    }
  }
}
