import java.util.concurrent.Executors;

public class LoomVirtualThreads {

  public static void main(String [] args) throws Exception {
    final int defaultEventsCount = 1000; 
    final int nThreads = args.length > 0  ? Integer.valueOf(args[0]) : defaultEventsCount;
 
    new TestScenario(
      nThreads,
      LoomVirtualThreads.class,
      Executors.newUnboundedVirtualThreadExecutor()
    );
  }
}