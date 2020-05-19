import java.util.concurrent.Executors;

public class Java11Threads {
  public static void main(String [] args) throws Exception {
    new TestScenario(
        args.length == 0 ? null : args[0],
        7500,
        Java11Threads.class,
        (long nThreads) -> { return Executors.newFixedThreadPool((int)nThreads); }
    );
  }
}