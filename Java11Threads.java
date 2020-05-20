import java.util.concurrent.Executors;

public class Java11Threads {
  public static void main(String [] args) throws Exception {
    final int defaultEventsCount = 1000;
    final int defaultPoolSize = 100;
    final int nEvents = args.length > 0  ? Integer.valueOf(args[0]) : defaultEventsCount;
    final int poolSize = args.length > 1  ? Integer.valueOf(args[1]) : defaultPoolSize;

    System.out.println(Java11Threads.class.getSimpleName() + ": thread pool size=" + poolSize);
    
    new TestScenario(
        nEvents,
        Java11Threads.class,
        Executors.newFixedThreadPool(poolSize)
    );
  }
}