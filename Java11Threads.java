import java.util.concurrent.Executors;

public class Java11Threads {
  public static void main(String [] args) throws Exception {
    final int defaultThreads = 7500;
    final int nThreads = args.length > 0  ? Integer.valueOf(args[0]) : defaultThreads;
    final int poolSize = args.length > 0  ? Integer.valueOf(args[1]) : nThreads;

    System.out.println(Java11Threads.class.getSimpleName() + ": thread pool size=" + poolSize);
    
    new TestScenario(
        nThreads,
        Java11Threads.class,
        (long unused) -> { return Executors.newFixedThreadPool(poolSize); }
    );
  }
}