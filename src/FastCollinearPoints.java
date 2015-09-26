import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import edu.princeton.cs.algs4.Queue;

/**
 * Programming Assignment 3: Pattern Recognition.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 */
public class FastCollinearPoints {

  private final LineSegment[] segments;

  public FastCollinearPoints(Point[] pointsInput) {

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
    int endCutOff = points.length;
    Queue<LineSegment> segmentQueue = new Queue<>();
    Point[] sortPoints = new Point[points.length];
    Set<String> segmentSet = new HashSet<>();
    for (int i = 0; i < points.length; i++) {
      if (i >= endCutOff) {
        break;
      }
      System.arraycopy(points, 0, sortPoints, 0, endCutOff);
      swapPoints(sortPoints, 0, i);
      Arrays.sort(sortPoints, 1, endCutOff, sortPoints[0].slopeOrder());
      double[] slopes = new double[endCutOff];
      int stride = 0;
      for (int k = 0; k < endCutOff - 1; k++) {
        slopes[k] = sortPoints[0].slopeTo(sortPoints[k + 1]);
      }
      slopes[endCutOff - 1] = Double.NEGATIVE_INFINITY; // sentinel
      for (int k = 1; k < endCutOff; k++) {
        if (slopes[k] == slopes[k - 1]) {
          stride++;
        } else {
          if (stride >= 2) {
            Point[] segPoints = new Point[stride + 2];
            System.arraycopy(sortPoints, k - stride, segPoints, 0, stride + 1);
            segPoints[stride + 1] = sortPoints[0];
            Arrays.sort(segPoints);
            String segString = segToString(segPoints[0], segPoints[segPoints.length - 1]);
            if (!segmentSet.contains(segString)) {
              LineSegment newSegment = new LineSegment(segPoints[0],
                  segPoints[segPoints.length - 1]);
              segmentQueue.enqueue(newSegment);

              String reverseSegString = segToString(segPoints[segPoints.length - 1], segPoints[0]);

              segmentSet.add(segString);
              segmentSet.add(reverseSegString);
            }
          }
          stride = 0;
        }
      }
    }
    segments = new LineSegment[segmentQueue.size()];
    for (int i = 0; i < segments.length; i++) {
      segments[i] = segmentQueue.dequeue();
    }
  }

  private String segToString(Point p1, Point p2) {
    return p1.toString() + "->" + p2.toString();
  }

  private void swapPoints(Point[] points, int i, int j) {
    Point tmp = points[i];
    points[i] = points[j];
    points[j] = tmp;
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
    Point[] points = new Point[]{
        new Point(0, 0),
        new Point(1, 1),
        new Point(2, 2),
        new Point(3, 3),
        new Point(3, 4)
    };
    FastCollinearPoints collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()), "[(0, 0) -> (3, 3)]", "1 segment");

    points = new Point[]{
        new Point(0, 0),
        new Point(1, 2),
        new Point(1, 3),
        new Point(3, 4),
        new Point(4, 3),
        new Point(99, 100),
    };
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(collinearPoints.numberOfSegments(), 0, "0 segment expected");

    // input8
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
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(10000, 0) -> (0, 10000), (3000, 4000) -> (20000, 21000)]", "segments");

    // input9
    points = new Point[]{
        new Point(9000, 9000),
        new Point(8000, 8000),
        new Point(7000, 7000),
        new Point(6000, 6000),
        new Point(5000, 5000),
        new Point(4000, 4000),
        new Point(3000, 3000),
        new Point(2000, 2000),
        new Point(1000, 1000),
    };
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(1000, 1000) -> (9000, 9000)]", "segments");

    // horizontal
    points = new Point[]{
        new Point(1, 10),
        new Point(2, 10),
        new Point(3, 10),
        new Point(4, 10),
        new Point(9, 20),
        new Point(8, 20),
        new Point(7, 20),
        new Point(6, 20),
    };
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(1, 10) -> (4, 10), (6, 20) -> (9, 20)]", "segments");

    // 4 horizontal
    points = new Point[]{
        new Point(1, 10),
        new Point(2, 10),
        new Point(3, 10),
        new Point(4, 10),
    };
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(1, 10) -> (4, 10)]", "segments");

    // 4x4 grid
    points = new Point[]{
        new Point(0, 0),
        new Point(0, 1),
        new Point(0, 2),
        new Point(0, 3),
        new Point(1, 0),
        new Point(1, 1),
        new Point(1, 2),
        new Point(1, 3),
        new Point(2, 0),
        new Point(2, 1),
        new Point(2, 2),
        new Point(2, 3),
        new Point(3, 0),
        new Point(3, 1),
        new Point(3, 2),
        new Point(3, 3),
    };
    collinearPoints = new FastCollinearPoints(points);
    assertEquals(Arrays.toString(collinearPoints.segments()),
        "[(0, 0) -> (3, 0), (0, 0) -> (3, 3), (0, 0) -> (0, 3), (0, 1) -> (3, 1), "
            + "(0, 2) -> (3, 2), (3, 0) -> (0, 3), (0, 3) -> (3, 3), (1, 0) -> (1, 3), "
            + "(2, 0) -> (2, 3), (3, 0) -> (3, 3)]", "segments");

  }

  private static void assertEquals(Object actual, Object expected, String msg) {
    if (actual.equals(expected)) {
      System.out.printf("%s: Passed. Actual: %s%n", msg, actual);
    } else {
      System.err.printf("%s: Failed. Actual: %s, expected: %s%n", msg, actual, expected);
    }
  }
}
