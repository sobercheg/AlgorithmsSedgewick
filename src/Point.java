import java.util.Comparator;

import edu.princeton.cs.algs4.StdDraw;

/**
 * Programming Assignment 3: Pattern Recognition.
 * http://coursera.cs.princeton.edu/algs4/assignments/collinear.html
 * The template is copied from http://coursera.cs.princeton.edu/algs4/testing/collinear/Point.java
 */
public class Point implements Comparable<Point> {

  private final int x;     // x-coordinate of this point
  private final int y;     // y-coordinate of this point

  /**
   * Initializes a new point.
   *
   * @param x the <em>x</em>-coordinate of the point
   * @param y the <em>y</em>-coordinate of the point
   */
  public Point(int x, int y) {
    /* DO NOT MODIFY */
    this.x = x;
    this.y = y;
  }

  /**
   * Draws this point to standard draw.
   */
  public void draw() {
    /* DO NOT MODIFY */
    StdDraw.point(x, y);
  }

  /**
   * Draws the line segment between this point and the specified point to standard draw.
   *
   * @param that the other point
   */
  public void drawTo(Point that) {
    /* DO NOT MODIFY */
    StdDraw.line(this.x, this.y, that.x, that.y);
  }

  /**
   * Returns the slope between this point and the specified point. Formally, if the two points are
   * (x0, y0) and (x1, y1), then the slope is (y1 - y0) / (x1 - x0). For completeness, the slope is
   * defined to be +0.0 if the line segment connecting the two points is horizontal;
   * Double.POSITIVE_INFINITY if the line segment is vertical; and Double.NEGATIVE_INFINITY if (x0,
   * y0) and (x1, y1) are equal.
   *
   * @param that the other point
   * @return the slope between this point and the specified point
   */
  public double slopeTo(Point that) {
    if (this.y == that.y) {
      if (this.x == that.x) {
        return Double.NEGATIVE_INFINITY;
      } else {
        return 0.0;
      }
    }
    if (this.x == that.x) {
      return Double.POSITIVE_INFINITY;
    }
    return (double) (that.y - this.y) / (that.x - this.x);
  }

  /**
   * Compares two points by y-coordinate, breaking ties by x-coordinate. Formally, the invoking
   * point (x0, y0) is less than the argument point (x1, y1) if and only if either y0 < y1 or if y0
   * = y1 and x0 < x1.
   *
   * @param that the other point
   * @return the value <tt>0</tt> if this point is equal to the argument point (x0 = x1 and y0 =
   * y1); a negative integer if this point is less than the argument point; and a positive integer
   * if this point is greater than the argument point
   */
  public int compareTo(Point that) {
    if (that == null) {
      throw new NullPointerException("The provided point is null");
    }
    if (this.y == that.y) {
      return Integer.compare(this.x, that.x);
    }

    return Integer.compare(this.y, that.y);
  }

  /**
   * Compares two points by the slope they make with this point. The slope is defined as in the
   * slopeTo() method.
   *
   * @return the Comparator that defines this ordering on points
   */
  public Comparator<Point> slopeOrder() {
    return new SlopeComparator(this);
  }

  private class SlopeComparator implements Comparator<Point> {

    private Point p;

    SlopeComparator(Point p) {
      this.p = p;
    }

    @Override
    public int compare(Point o1, Point o2) {
      if (o1 == null || o2 == null) {
        throw new NullPointerException("Null points");
      }
      return Double.compare(o1.slopeTo(p), o2.slopeTo(p));
    }
  }

  /**
   * Returns a string representation of this point. This method is provide for debugging; your
   * program should not rely on the format of the string representation.
   *
   * @return a string representation of this point
   */
  public String toString() {
    /* DO NOT MODIFY */
    return "(" + x + ", " + y + ")";
  }

  /**
   * Unit tests the Point data type.
   */
  public static void main(String[] args) {
    Point p1 = new Point(0, 0);
    Point p2 = new Point(1, 1);
    assertTrue(p1.slopeTo(p2) == 1.0, "diag slope " + p1.slopeTo(p2));

    assertTrue(p1.slopeTo(new Point(0, 1)) == Double.POSITIVE_INFINITY, "vertical");
    assertTrue(p1.slopeTo(new Point(1, 0)) == 0.0, "horizontal");
    assertTrue(p1.slopeTo(p1) == Double.NEGATIVE_INFINITY, "equal");

    assertTrue(p1.compareTo(p2) < 0, "compare diag");
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
