import java.util.concurrent.Executors;

public class LoomVirtualThreads {

  public static void main(String [] args) throws Exception {
    final int defaultThreads = 100000; 
    final int nThreads = args.length > 0  ? Integer.valueOf(args[0]) : defaultThreads;
 
    new TestScenario(
      nThreads,
      LoomVirtualThreads.class,
      (long unused) -> { return Executors.newUnboundedVirtualThreadExecutor(); }
    );
  }
}