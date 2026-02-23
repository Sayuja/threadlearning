# Java 21 Threads Learning — 21 Tutorials

A step-by-step journey through Java concurrency, from basic thread creation to Java 21's Virtual Threads and Structured Concurrency.

## Prerequisites

- **Java 21** (or later)
- **Maven 3.9+**

## How to Run a Tutorial

Each tutorial is a standalone class with a `main` method:

```bash
# Compile the project
mvn compile

# Run a specific tutorial (example for Step 1)
mvn exec:java -Dexec.mainClass="com.threads.step01_thread_creation.ThreadCreationDemo"
```

Or compile and run directly with `javac` / `java`:

```bash
javac --enable-preview --source 21 src/main/java/com/threads/step01_thread_creation/ThreadCreationDemo.java
java --enable-preview com.threads.step01_thread_creation.ThreadCreationDemo
```

## Tutorials

| Step | Topic | Package |
|------|-------|---------|
| 1  | Thread Creation (`Thread` vs `Runnable`) | `step01_thread_creation` |
| 2  | Thread Lifecycle & States | `step02_thread_lifecycle` |
| 3  | Thread Sleep & Join | `step03_sleep_and_join` |
| 4  | Thread Interruption | `step04_thread_interruption` |
| 5  | Race Conditions | `step05_race_conditions` |
| 6  | Synchronized Methods & Blocks | `step06_synchronization` |
| 7  | Volatile Keyword | `step07_volatile` |
| 8  | Atomic Variables | `step08_atomic_variables` |
| 9  | Deadlock | `step09_deadlock` |
| 10 | ReentrantLock | `step10_reentrant_lock` |
| 11 | ReadWriteLock | `step11_readwrite_lock` |
| 12 | Condition Variables | `step12_condition_variables` |
| 13 | ExecutorService | `step13_executor_service` |
| 14 | Callable & Future | `step14_callable_future` |
| 15 | CompletableFuture | `step15_completable_future` |
| 16 | CountDownLatch | `step16_countdown_latch` |
| 17 | CyclicBarrier | `step17_cyclic_barrier` |
| 18 | Semaphore | `step18_semaphore` |
| 19 | Thread-Safe Collections | `step19_concurrent_collections` |
| 20 | Virtual Threads (Project Loom) | `step20_virtual_threads` |
| 21 | Structured Concurrency (Preview) | `step21_structured_concurrency` |
