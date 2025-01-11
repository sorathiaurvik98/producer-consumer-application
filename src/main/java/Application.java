import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Application {
    public static void main(String[] args) {
        int queueCapacity = 5;
        int totalMessages = 10;

        MessageQueue messageQueue = new MessageQueue(queueCapacity);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Producer producer = new Producer(messageQueue, totalMessages);
        Consumer consumer = new Consumer(messageQueue, successCount, failureCount);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(producer);
        executorService.execute(consumer);

        // Allow threads to run for some time.
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            executorService.shutdown();
            System.out.println("Total messages processed successfully: " + successCount.get());
            System.out.println("Total errors encountered: " + failureCount.get());
        }
    }
}
