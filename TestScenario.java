import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

class TestScenario {

    private Instant nextReport = Instant.now();

    @FunctionalInterface
    static interface ExecutorProvider {
        ExecutorService getExecutor(long nThreads);
    }

    private synchronized void maybeReport(long count) {
        if (count <= 0)
            return;
        if (Instant.now().isAfter(nextReport)) {
            System.out.print(count + " ");
            nextReport = Instant.now().plusSeconds(1);
        }
    }

    TestScenario(int nEvents, Class<?> caller, ExecutorProvider provider) throws InterruptedException {
        final BlockingQueue<Integer> eventsQueue = new ArrayBlockingQueue<Integer>(nEvents);
        final CountDownLatch counter = new CountDownLatch(nEvents);
        final DecimalFormat f = new DecimalFormat("###,###");

        Instant start = null;
        final ExecutorService executor = provider.getExecutor(nEvents);
        final Random random = new Random(42);
        try {
            executor.submit(() -> {
                while (counter.getCount() > 0) {
                    // Simulate a naive request processing implementation
                    // with one thread per incoming event.
                    // Naive is cool - we shouldn't have to worry about the rest
                    try {
                        eventsQueue.take();
                    } catch (InterruptedException ignored) {
                    }
                    executor.submit(() -> {
                        try {
                            Thread.sleep(50 + random.nextInt(250));
                        } catch (InterruptedException ignored) {
                        }
                        counter.countDown();
                        maybeReport(counter.getCount());
                    });
                }
            });
            System.out.println(caller.getSimpleName() + ": running test scenario with " + f.format(counter.getCount()) + " events");
            System.out.print("Remaining count: ");
            start = Instant.now();

            // Feed events into our queue
            for(int i=0; i < nEvents; i++) {
                eventsQueue.put(i);
            }
        } finally {
            counter.await();
            System.out.println();
            System.out.println("Shutting down executor...");
            executor.shutdown();
        }
        System.out.println("Duration: " + Duration.between(start, Instant.now()).toSeconds() + " seconds");
        System.exit(0);
    }
}