import java.util.LinkedList;

class ChatQueue {

  private LinkedList<String> queue;
  //queueSize

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
