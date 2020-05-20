Basic test of the Project Loom virtual threads, May 2020 
-----

This simulates processing incoming events with multiple threads, to compare
performance between a Java 11 _fixed thread pool_ and a Project Loom _unbounded
virtual thread executor_.

A first version didn't lead to impressive results as nothing was blocking, which
is not typical of request processing where we usually have to wait for I/O or events.

Thanks to [Ron Pressler](https://twitter.com/pressron/status/1262883349108068354) for
pointing this out, and this new version where tasks sleep a few milliseconds to simulate
the Real World Of Waiting For Things shows dramatic differences:

> 4-10 seconds to run the test using Project Loom virtual threads, compared
> to about 150 seconds with Java 11 threads.

I didn't optimize JVM parameters, using the same heap size for both tests. 

For the Java 11 style tests the thread pool size plays a role and I suspect optimizing
it in relation with JVM parameters would help. However with the Project Loom virtual
threads you don't have to care about this which is really nice, I suppose just setting
the right heap size is good enough.

Here's how to run these tests, with typical timings from my 2018 Macbook Pro, using the 
Project Loom early access Java 15 JVM.

    $ java -version
    openjdk version "15-loom" 2020-09-15
    OpenJDK Runtime Environment (build 15-loom+7-141)
    OpenJDK 64-Bit Server VM (build 15-loom+7-141, mixed mode, sharing)

    $ export N=1000000
    $ export MEM=-Xmx2G
    $ export P=7500

    $ rm -rf *.class && javac Java11Threads.java && java $MEM Java11Threads $N $P
    Java11Threads: thread pool size=7500
    Java11Threads: running test scenario with 1’000’000 events
    Remaining count: 999999 993932 986997 977108...13626 8533 3434 
    Shutting down executor...
    Duration: 148 seconds

    $ rm -rf *.class && javac LoomVirtualThreads.java && java $MEM LoomVirtualThreads $N
    LoomVirtualThreads: running test scenario with 1’000’000 events
    Remaining count: 999998 948711 885636 434131 
    Shutting down executor...
    Duration: 5 seconds

## Conclusions

This is just scratching the surface of Project Loom: by enabling simple and modern programming
styles where (virtual) threads and blocking are cheap it should be a game changer for JVM 
programming.

We might be able to throw away a lot of code if this delivers on the current `Promises`...

## Links to Project Loom information and early access JVMs

* https://wiki.openjdk.java.net/display/loom/Main
* https://wiki.openjdk.java.net/display/loom/Getting+started 
* http://jdk.java.net/loom/ : early access builds, Linux, macOS, Windows
