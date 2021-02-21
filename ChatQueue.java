import java.util.LinkedList;

/**
 * simple queue data structure to hold chat messages
 */
class ChatQueue {

  private LinkedList<String> queue;

  public ChatQueue(LinkedList<String> queue) {
    this.queue = queue;
  }

  public void enqueue(String msg) {
    queue.add(msg);
  }

  public void dequeue() {
    queue.removeFirst();
  }

  public String next() {
    return queue.getFirst();
  }

  public boolean isEmpty() {
    return queue.isEmpty();
  }
}
