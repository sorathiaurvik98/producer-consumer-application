import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private final MessageQueue queue;
    private final AtomicInteger successCount;
    private final AtomicInteger failureCount;

    public Consumer(MessageQueue queue, AtomicInteger successCount, AtomicInteger failureCount) {
        this.queue = queue;
        this.successCount = successCount;
        this.failureCount = failureCount;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = queue.dequeue();
                System.out.println("Consumed: " + message);
                successCount.incrementAndGet();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                failureCount.incrementAndGet();
                System.err.println("Consumer interrupted: " + e.getMessage());
            }
        }
    }
}
