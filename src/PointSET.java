import java.util.Iterator;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Programming Assignment 5: Kd-Trees.
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 */
public class PointSET {

  private final SET<Point2D> points;

  public PointSET() {
    this.points = new SET<>();
  }

  public boolean isEmpty() {
    return points.isEmpty();
  }

  public int size() {
    return points.size();
  }

  public void insert(Point2D p) {
    points.add(checkArgument(p));
  }

  public boolean contains(Point2D p) {
    return points.contains(checkArgument(p));
  }

  public void draw() {
    for (Point2D point : points) {
      StdDraw.point(point.x(), point.y());
    }
  }

  public Iterable<Point2D> range(RectHV rect) {
    checkArgument(rect);
    Queue<Point2D> rectPoints = new Queue<>();
    for (Point2D p : points) {
      if (rect.contains(p)) {
        rectPoints.enqueue(p);
      }
    }
    return rectPoints;
  }

  /**
   * Return a nearest neighbor in the set to point p; null if the set is empty.
   */
  public Point2D nearest(Point2D point) {
    if (isEmpty()) {
      return null;
    }
    Point2D closestPoint = null;
    double bestDist = Double.MAX_VALUE;
    for (Point2D p : points) {
      double dist = point.distanceSquaredTo(p);
      if (dist < bestDist) {
        bestDist = dist;
        closestPoint = p;
      }
    }
    return closestPoint;
  }

  private static <T> T checkArgument(T o) {
    if (o == null) {
      throw new NullPointerException("Null input");
    }
    return o;
  }

  public static void main(String[] args) {
    PointSET pointSET = new PointSET();
    pointSET.insert(new Point2D(0, 0.5));
    pointSET.insert(new Point2D(0.5, 1));
    pointSET.insert(new Point2D(0.5, 0));
    pointSET.insert(new Point2D(1, 0.5));
    Iterator<Point2D> range = pointSET.range(new RectHV(0, 0, 1, 1)).iterator();
    assertTrue(range.hasNext(), "more points");
    range.next();
    assertTrue(range.hasNext(), "more points");
    range.next();
    assertTrue(range.hasNext(), "more points");
    range.next();
    assertTrue(range.hasNext(), "more points");
    range.next();
    assertTrue(!range.hasNext(), "no more points");

    Point2D nearest = pointSET.nearest(new Point2D(1.1, 0.6));
    assertTrue(Double.compare(nearest.x(), 1) == 0, "nearest x");
    assertTrue(Double.compare(nearest.y(), 0.5) == 0, "nearest y");
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
