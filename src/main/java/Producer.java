public class Producer implements Runnable {
    private final MessageQueue queue;
    private final int messageCount;

    public Producer(MessageQueue queue, int messageCount) {
        this.queue = queue;
        this.messageCount = messageCount;
    }

    @Override
    public void run() {
        for (int i = 1; i <= messageCount; i++) {
            String message = String.format("Message %d", i);

            try {
                queue.enqueue(message);
                System.out.println("Produced: " + message);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer interrupted: " + e.getMessage());
            }
        }
    }
}
