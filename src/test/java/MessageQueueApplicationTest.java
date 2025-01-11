import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MessageQueueApplicationTest {

    @Test
    void testSuccessfulMessageProcessing() throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(5);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Producer producer = new Producer(messageQueue, 3);
        Consumer consumer = new Consumer(messageQueue, successCount, failureCount);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.interrupt();
        consumerThread.join();

        assertEquals(3, successCount.get());
        assertEquals(0, failureCount.get());
    }

    @Test
    void testFailureMessageProcessing() throws InterruptedException {
        MessageQueue messageQueue = new MessageQueue(0); // Queue with 0 capacity to simulate failure
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        Producer producer = new Producer(messageQueue, 1);
        Consumer consumer = new Consumer(messageQueue, successCount, failureCount);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        Thread.sleep(500);

        producerThread.interrupt();
        consumerThread.interrupt();

        producerThread.join();
        consumerThread.join();

        assertEquals(0, successCount.get());
        assertTrue(failureCount.get() > 0, "Errors should be logged for failed processing");
    }
}
