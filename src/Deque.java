import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Programming Assignment 2: Randomized Queues and Deques
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 */
public class Deque<Item> implements Iterable<Item> {

  private int size = 0;
  private Node first = new Node(); // sentinel node with null Item
  private Node last = first;  // sentinel node with null Item

  private class Node {
    private Item item;
    private Node next;
    private Node previous;
  }

  public Deque() {
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void addFirst(Item item) {
    if (item == null) {
      throw new NullPointerException("Cannot add null elements");
    }
    size++;
    if (first.item == null) {
      first.item = item;
      return;
    }
    Node newNode = new Node();
    newNode.item = item;
    first.previous = newNode;
    newNode.next = first;
    first = newNode;
  }

  public void addLast(Item item) {
    if (item == null) {
      throw new NullPointerException("Cannot add null elements");
    }
    size++;
    if (last.item == null) {
      last.item = item;
      return;
    }
    Node newNode = new Node();
    newNode.item = item;
    last.next = newNode;
    newNode.previous = last;
    last = newNode;
  }

  public Item removeFirst() {
    if (isEmpty()) {
      throw new NoSuchElementException("Deque is empty");
    }
    Item item = first.item;

    if (size == 1) {
      first.item = null;
    } else {
      Node next = first.next;
      next.previous = null;
      first = next;
    }
    size--;
    return item;
  }

  public Item removeLast() {
    if (isEmpty()) {
      throw new NoSuchElementException("Deque is empty");
    }
    Item item = last.item;
    if (size == 1) {
      last.item = null;
    } else {
      Node previous = last.previous;
      previous.next = null;
      last = previous;
    }
   size--;
   return item;
  }

  private class DequeIterator implements Iterator<Item> {

    private Node node = first;

    @Override
    public boolean hasNext() {
      return node != null && node.item != null;
    }

    @Override
    public Item next() {
      if (!hasNext()) {
        throw new NoSuchElementException("No more elements");
      }
      Item item = node.item;
      node = node.next;
      return item;
    }

    @Override
    public void remove() {
       throw new UnsupportedOperationException("Remove is not supported");
    }
  }
  public Iterator<Item> iterator() {
    return new DequeIterator();
  }

  public static void main(String[] args) {
    Deque<String> emptyDeque = new Deque<>();
    assertTrue(emptyDeque.isEmpty(), "empty deque");
    assertTrue(emptyDeque.size() == 0, "empty deque");
    assertTrue(!emptyDeque.iterator().hasNext(), "empty deque iterator no next");

    Deque<String> sequence = new Deque<>();
    sequence.addLast("1");
    sequence.addLast("2");
    sequence.addLast("3");
    assertTrue(sequence.size() == 3, "Deque size 3");
    assertTrue(sequence.removeFirst().equals("1"), "1");
    assertTrue(sequence.removeFirst().equals("2"), "2");
    assertTrue(sequence.removeFirst().equals("3"), "3");
    assertTrue(sequence.size() == 0, "Deque size 0");

    sequence.addFirst("1");
    sequence.addFirst("2");
    assertTrue(sequence.removeLast().equals("1"), "1");
    assertTrue(sequence.removeLast().equals("2"), "2");

    sequence.addFirst("10");
    Iterator<String> it1 = sequence.iterator();
    Iterator<String> it2 = sequence.iterator();
    assertTrue(it1.next().equals("10"), "Iterator #1: 10");
    assertTrue(it2.next().equals("10"), "Iterator #2: 10");
    assertTrue(!it1.hasNext(), "Iterator #1: no next");
    assertTrue(!it2.hasNext(), "Iterator #2: no next");
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
