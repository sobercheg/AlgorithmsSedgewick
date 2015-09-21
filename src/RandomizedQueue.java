import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

/**
 * Programming Assignment 2: Randomized Queues and Deques
 * http://coursera.cs.princeton.edu/algs4/assignments/queues.html
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

  private static final int INITIAL_CAPACITY = 2;
  private int size;
  private Item[] array;

  public RandomizedQueue() {
    array = (Item[]) new Object[INITIAL_CAPACITY];
  }

  public boolean isEmpty() {
    return size == 0;
  }

  public int size() {
    return size;
  }

  public void enqueue(Item item) {
    if (item == null) {
      throw new NullPointerException("Cannot add null to the queue");
    }
    if (size == array.length) {
      resize(array.length * 2);
    }
    array[size++] = item;
  }

  public Item dequeue() {
    if (size == 0) {
      throw new NoSuchElementException("Cannot dequeue from an empty queue");
    }
    if (size > 0 && size + 1 == array.length / 4) {
      resize(array.length / 2);
    }
    int index = StdRandom.uniform(size);
    Item item = array[index];
    array[index] = array[size - 1];
    array[size - 1] = null;
    size--;
    return item;
  }

  public Item sample() {
    if (size == 0) {
      throw new NoSuchElementException("Cannot sample from an empty queue");
    }
    return array[StdRandom.uniform(size)];
  }

  private class ThisIterator implements Iterator<Item> {

    private int i = 0;
    private Item[] array;

    public ThisIterator() {
      array = (Item[]) new Object[size];
      System.arraycopy(RandomizedQueue.this.array, 0, array, 0, size);
      StdRandom.shuffle(array);
    }

    @Override
    public boolean hasNext() {
      return i < size;
    }

    @Override
    public Item next() {
      if (i == size) {
        throw new NoSuchElementException("Only " + size + " elements are there");
      }
      return array[i++];
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException("remove is not supported");
    }
  }

  public Iterator<Item> iterator() {
    return new ThisIterator();
  }

  private void resize(int newSize) {
    Item[] newArray = (Item[]) new Object[newSize];
    System.arraycopy(array, 0, newArray, 0, size);
    array = newArray;
  }

  public static void main(String[] args) {
    RandomizedQueue<Integer> q = new RandomizedQueue<>();
    q.enqueue(1);
    q.enqueue(2);
    q.enqueue(3);
    assertTrue(q.size() == 3, "Queue size is 3");
    int[] result = new int[3];
    result[0] = q.dequeue();
    result[1] = q.dequeue();
    result[2] = q.dequeue();
    assertTrue(result[0] != result[1] && result[1] != result[2] && result[2] != result[0],
        "1, 2, 3 are present");
    assertTrue(q.isEmpty(), "empty queue");
    q.enqueue(10);
    q.enqueue(20);

    Iterator<Integer> it = q.iterator();
    assertTrue(it.hasNext(), "iterator has next");
    int itNext = it.next();
    assertTrue(itNext == 10 || itNext == 20, "it.next() in {10, 20}");
    itNext = it.next();
    assertTrue(itNext == 10 || itNext == 20, "it.next() in {10, 20}");
    assertTrue(!it.hasNext(), "no more elements in iterator");
  }

  private static void assertTrue(boolean expression, String msg) {
    if (expression) {
      System.out.printf("%s: Passed%n", msg);
    } else {
      System.err.printf("%s: Failed%n", msg);
    }
  }
}
