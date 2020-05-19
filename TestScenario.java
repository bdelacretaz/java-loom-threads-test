import java.text.DecimalFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

class TestScenario {

    public static final int DEFAULT_THREADS = 100;

    private CountDownLatch startLatch;
    private CountDownLatch counter;

    @FunctionalInterface
    static interface ExecutorProvider {
        ExecutorService getExecutor(long nThreads);
    }

    TestScenario(String threadsArg, int defaultThreads, Class<?> caller, ExecutorProvider provider) throws InterruptedException {
        final int nThreads = threadsArg == null ? defaultThreads : Integer.valueOf(threadsArg);
        startLatch = new CountDownLatch(1);
        counter = new CountDownLatch(nThreads);
        final DecimalFormat f = new DecimalFormat("###,###");

        long time = 0;
        final ExecutorService executor = provider.getExecutor(nThreads); 
        try {
            for (int i = 0; i < nThreads; i++) {
                executor.submit(() -> {
                    try {
                        startLatch.await();
                    } catch (InterruptedException ignored) {
                    }
                    counter.countDown();
                });
            }
            System.out.println(caller.getSimpleName() + ": running test scenario with " + f.format(counter.getCount()) + " threads");
            time = System.currentTimeMillis();
            startLatch.countDown();
        } finally {
            counter.await();
            executor.shutdown();
        }
        System.out.println("Duration: " + f.format(System.currentTimeMillis() - time) + " msec");

    }
}