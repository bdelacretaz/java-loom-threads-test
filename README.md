Basic test of the Project Loom virtual threads, May 2020 
-----

This compares a trivial task (decrementing a `CountDownLatch`) in many multiple
threads, using either a Java 11 fixed thread pool or a Project Loom unbounded
virtual threads executor.

I initially got _much_ lower execution times executing _many_ more tasks with
the Project Loom Virtual Threads - but running my Java 11 example with the
Java 15 JVM provided with the Early Access Project Loom I get similar results.

It looks like there's been general threading improvements in the Java 15 JVM,
and I'm not a specialist of that anyway so who am I to comment?

Anwyway, here's what I get on my 2018 Macbook Pro:

    $ . setjdk 11
    openjdk version "11.0.2" 2019-01-15
    OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
    OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode, sharing)

    $ rm -rf *.class && javac Java11Threads.java && java -version && java -Xmx256M Java11Threads 100000 7500
    openjdk version "11.0.2" 2019-01-15
    OpenJDK Runtime Environment 18.9 (build 11.0.2+9)
    OpenJDK 64-Bit Server VM 18.9 (build 11.0.2+9, mixed mode, sharing)
    Java11Threads: thread pool size=7500
    Java11Threads: running test scenario with 100.000 tasks
    Duration: 5.469 msec

    $ . setjdk 15
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)

    $ rm -rf *.class && javac Java11Threads.java && java -version && java -Xmx256M Java11Threads 100000 7500
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)
    Java11Threads: thread pool size=7500
    Java11Threads: running test scenario with 100’000 tasks
    Duration: 2’581 msec

    $ . setjdk 15
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)

    $ rm -rf *.class && javac LoomVirtualThreads.java && java -version && java -Xmx256M LoomVirtualThreads
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)
    LoomVirtualThreads: running test scenario with 100’000 tasks
    Duration: 2’056 msec

That's just scratching the surface of Project Loom however - by enabling simple and modern programming
styles where (virtual) threads are cheap it should be a game changer for JVM programming, and just
looking at such a trivial example is probably not very relevant.

We might be able to throw away a lot of code if this delivers on the current `Promises`...

## Links to Project Loom information and early access JVMs

* https://wiki.openjdk.java.net/display/loom/Main
* https://wiki.openjdk.java.net/display/loom/Getting+started 
* http://jdk.java.net/loom/ : early access builds, Linux, macOS, Windows