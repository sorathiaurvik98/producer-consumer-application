import java.util.LinkedList;
import java.util.Queue;

public class MessageQueue {
    private final Queue<String> queue = new LinkedList<>();
    private final int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void enqueue(String message) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }

        queue.offer(message);

        notifyAll();
    }

    public synchronized String dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }

        String message = queue.poll();

        notifyAll();

        return message;
    }
}
