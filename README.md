Basic test of the Project Loom virtual threads, May 2020 
-----

This compares a trivial task (decrementing a `CountDownLatch`) in many multiple
threads, using either a Java 11 fixed thread pool or a Project Loom unbounded
virtual threads executor.

Spoiler: the meaning of _many_ is very different between those environments!

On my 2018 Macbook Pro with just an `-Xmx` setting for the JVM I get:

    $ . setjdk 11
    openjdk version "11.0.2" 2019-01-15
    OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
    OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode, sharing)

    $ rm -rf *.class && javac Java11Threads.java && java -version && java -Xmx256M Java11Threads 
    openjdk version "11.0.2" 2019-01-15
    OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
    OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode, sharing)
    Java11Threads: running test scenario with 7.500 threads
    **Duration: 3.735 msec**

    $ . setjdk 15
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)

    $ rm -rf *.class && javac LoomVirtualThreads.java && java -version && java -Xmx256M LoomVirtualThreads
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)
    LoomVirtualThreads: running test scenario with 100’000 threads
    **Duration: 1’811 msec**

So the Loom version runs 100'000 instances of the task in less than two seconds, while the JDK 11 version
takes about twice as long to run only 7'500 instances - and fails with various errors if I try with 10'000
threads.

To me this looks like a game changer for the JVM, enabling simple and modern programming styles where (virtual) threads are cheap. Looks like we might be able to throw away a lot of code if this delivers
on the current `Promises`.

## Links to Project Loom information and early access JVMs

* https://wiki.openjdk.java.net/display/loom/Main
* https://wiki.openjdk.java.net/display/loom/Getting+started 
* http://jdk.java.net/loom/ : early access builds, Linux, macOS, Windows