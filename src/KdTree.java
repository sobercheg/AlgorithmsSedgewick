import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

/**
 * Programming Assignment 5: Kd-Trees.
 * http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html
 */
public class KdTree {

  private static final RectHV BOUND_RECT = new RectHV(0, 0, 1, 1);

  private static final class Node {
    private Point2D p;      // the point
    private Node lb;        // the left/bottom subtree
    private Node rt;        // the right/top subtree
  }

  private enum SplitDirection {
    VERTICAL, HORIZONTAL;

    private static SplitDirection getOther(SplitDirection splitDirection) {
      if (splitDirection == VERTICAL) {
        return HORIZONTAL;
      }
      return VERTICAL;
    }
  }

  private Node root;
  private int size;

  public KdTree() {
    root = null;
    size = 0;
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void insert(Point2D p) {
    checkArgument(p);
    root = insertInternal(root, p, SplitDirection.VERTICAL);
  }

  private Node insertInternal(Node node, Point2D point, SplitDirection direction) {
    if (node == null) {
      node = new Node();
      node.p = point;
      size++;
      return node;
    }
    // duplicate check
    if (node.p.equals(point)) {
      return node;
    }
    if (direction == SplitDirection.VERTICAL) {
      if (point.x() < node.p.x()) {
        node.lb = insertInternal(node.lb, point, SplitDirection.HORIZONTAL);
      } else {
        node.rt = insertInternal(node.rt, point, SplitDirection.HORIZONTAL);
      }
    } else {
      if (point.y() < node.p.y()) {
        node.lb = insertInternal(node.lb, point, SplitDirection.VERTICAL);
      } else {
        node.rt = insertInternal(node.rt, point, SplitDirection.VERTICAL);
      }
    }
    return node;
  }

  public boolean contains(Point2D p) {
    checkArgument(p);
    return containsInternal(root, p, SplitDirection.VERTICAL);
  }

  private boolean containsInternal(Node node, Point2D p, SplitDirection splitDirection) {
    if (node == null) {
      return false;
    }
    if (node.p.equals(p)) {
      return true;
    }
    if (splitDirection == SplitDirection.VERTICAL) {
      if (p.x() < node.p.x()) {
        return containsInternal(node.lb, p, SplitDirection.HORIZONTAL);
      } else {
        return containsInternal(node.rt, p, SplitDirection.HORIZONTAL);
      }
    } else {
      if (p.y() < node.p.y()) {
        return containsInternal(node.lb, p, SplitDirection.VERTICAL);
      } else {
        return containsInternal(node.rt, p, SplitDirection.VERTICAL);
      }
    }
  }

  public void draw() {
    if (root == null) {
      return;
    }
    for (Point2D point : traverse()) {
      StdDraw.point(point.x(), point.y());
    }
  }

  private Iterable<Point2D> traverse() {
    Queue<Node> q = new Queue<>();
    Queue<Point2D> points = new Queue<>();
    q.enqueue(root);
    while (!q.isEmpty()) {
      Node node = q.dequeue();
      points.enqueue(node.p);
      if (node.lb != null) {
        q.enqueue(node.lb);
      }
      if (node.rt != null) {
        q.enqueue(node.rt);
      }
    }
    return points;
  }

  public Iterable<Point2D> range(RectHV rect) {
    checkArgument(rect);
    Queue<Point2D> rectPoints = new Queue<>();
    rangeInternal(root, rect, BOUND_RECT, SplitDirection.VERTICAL, rectPoints);
    return rectPoints;
  }

  private void rangeInternal(Node node, RectHV rect, RectHV nodeRect, SplitDirection splitDirection, Queue<Point2D> rectPoints) {
    if (node == null) {
      return;
    }
    if (!nodeRect.intersects(rect)) {
      return;
    }
    if (rect.contains(node.p)) {
      rectPoints.enqueue(node.p);
    }
    RectHV lbRect;
    RectHV rtRect;
    if (splitDirection == SplitDirection.VERTICAL) {
      lbRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.p.x(), nodeRect.ymax());
      rtRect = new RectHV(node.p.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
    } else {
      lbRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.p.y());
      rtRect = new RectHV(nodeRect.xmin(), node.p.y(), nodeRect.xmax(), nodeRect.ymax());
    }
    rangeInternal(node.lb, rect, lbRect, SplitDirection.getOther(splitDirection), rectPoints);
    rangeInternal(node.rt, rect, rtRect, SplitDirection.getOther(splitDirection), rectPoints);
  }

  /**
   * Return a nearest neighbor in the set to point p; null if the set is empty.
   */
  public Point2D nearest(Point2D point) {
    if (isEmpty()) {
      return null;
    }
    return nearestInternal(point, root, BOUND_RECT, SplitDirection.VERTICAL, root.p, Double.MAX_VALUE);
  }

  private Point2D nearestInternal(Point2D point, Node node, RectHV nodeRect, SplitDirection splitDirection, Point2D currentNearest, double currentDistance) {
    if (node == null) {
      return currentNearest;
    }
    if (nodeRect.distanceSquaredTo(point) > currentDistance) {
      return currentNearest;
    }
    double pointDist = node.p.distanceSquaredTo(point);
    double bestDist = currentDistance;
    Point2D bestPoint = currentNearest;
    if (pointDist < currentDistance) {
      bestDist = pointDist;
      bestPoint = node.p;
    }
    RectHV lbRect;
    RectHV rtRect;
    if (splitDirection == SplitDirection.VERTICAL) {
      lbRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), node.p.x(), nodeRect.ymax());
      rtRect = new RectHV(node.p.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());
    } else {
      lbRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), node.p.y());
      rtRect = new RectHV(nodeRect.xmin(), node.p.y(), nodeRect.xmax(), nodeRect.ymax());
    }

    bestPoint = nearestInternal(point, node.lb, lbRect, SplitDirection.getOther(splitDirection), bestPoint, bestDist);
    bestDist = Math.min(point.distanceSquaredTo(bestPoint), bestDist);
    bestPoint = nearestInternal(point, node.rt, rtRect, SplitDirection.getOther(splitDirection), bestPoint, bestDist);
    return bestPoint;
  }

  private static <T> T checkArgument(T o) {
    if (o == null) {
      throw new NullPointerException("Null input");
    }
    return o;
  }

  public static void main(String[] args) {
    KdTree kdTree = new KdTree();
    assertTrue(kdTree.size() == 0, "size 0");
    kdTree.insert(new Point2D(0, 0.5));
    assertTrue(kdTree.size() == 1, "size 1");
    kdTree.insert(new Point2D(0.5, 1));
    assertTrue(kdTree.size() == 2, "size 2");
    kdTree.insert(new Point2D(0.5, 0));
    assertTrue(kdTree.size() == 3, "size 3");
    kdTree.insert(new Point2D(1, 0.5));
    assertTrue(kdTree.size() == 4, "size 4");

    assertTrue(kdTree.contains(new Point2D(0, 0.5)), "contains");
    assertTrue(kdTree.contains(new Point2D(1, 0.5)), "contains");
    assertTrue(!kdTree.contains(new Point2D(0.5, 0.5)), "not contains");

    Iterable<Point2D> rang = kdTree.range(new RectHV(0, 0, 0.3, 0.3));
    assertTrue(!rang.iterator().hasNext(), "nothing in range");
    rang = kdTree.range(new RectHV(0, 0, 0.5, 0.5));
    SET<Point2D> rangeQueue = new SET<>();
    for (Point2D p : rang) {
      rangeQueue.add(p);
    }
    assertTrue(rangeQueue.size() == 2, "contains 2 points");
    assertTrue(rangeQueue.contains(new Point2D(0, 0.5)), "contains");
    assertTrue(rangeQueue.contains(new Point2D(0.5, 0)), "contains");
    assertTrue(!rangeQueue.contains(new Point2D(1, 0.5)), "not contains");
    assertTrue(!rangeQueue.contains(new Point2D(0.5, 1)), "not contains");

    assertTrue(kdTree.nearest(new Point2D(0.1, 0.6)).equals(new Point2D(0, 0.5)), "closest to 0, 0.5");

    // duplicates, check size
    kdTree = new KdTree();
    kdTree.insert(new Point2D(1, 1));
    kdTree.insert(new Point2D(1, 1));
    assertTrue(kdTree.size() == 1, "size still 1");
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
