import java.util.concurrent.Executors;

public class LoomVirtualThreads {

  public static void main(String [] args) throws Exception {
    new TestScenario(
      args.length == 0 ? null : args[0],
      100000,
      LoomVirtualThreads.class,
      (long unused) -> { return Executors.newUnboundedVirtualThreadExecutor(); }
    );
  }
}