package com.threads.step01_thread_creation;

/**
 * ============================================================================
 * STEP 1: THREAD CREATION IN JAVA
 * ============================================================================
 *
 * There are multiple ways to create threads in Java. This tutorial covers:
 *
 * 1. Extending the Thread class
 * 2. Implementing the Runnable interface
 * 3. Using a lambda expression (since Runnable is a functional interface)
 * 4. Using an anonymous inner class
 *
 * KEY CONCEPTS:
 * - A Thread is the smallest unit of execution within a process.
 * - Every Java program has at least one thread: the "main" thread.
 * - Thread.start() creates a NEW thread of execution and calls run().
 * - NEVER call run() directly — it runs on the CURRENT thread, not a new one.
 * - Thread execution order is non-deterministic — the OS scheduler decides.
 *
 * WHY RUNNABLE IS PREFERRED OVER EXTENDING THREAD:
 * - Java supports single inheritance, so extending Thread prevents extending
 *   another class.
 * - Runnable separates the TASK from the THREAD (better design).
 * - Runnable can be submitted to ExecutorService (covered in Step 13).
 * ============================================================================
 */
public class ThreadCreationDemo {

    // ========================================================================
    // APPROACH 1: Extending the Thread class
    // ========================================================================
    /**
     * By extending Thread, the class IS-A thread.
     * You override the run() method with the task logic.
     *
     * Drawback: This class can no longer extend another class (single inheritance).
     */
    static class MyThread extends Thread {
        @Override
        public void run() {
            // Thread.currentThread().getName() returns the name of the executing thread
            System.out.println("[Approach 1 - Extending Thread]");
            System.out.println("  Thread name : " + Thread.currentThread().getName());
            System.out.println("  Thread ID   : " + Thread.currentThread().getId());
            System.out.println("  Is alive?   : " + Thread.currentThread().isAlive());
            System.out.println();
        }
    }

    // ========================================================================
    // APPROACH 2: Implementing the Runnable interface
    // ========================================================================
    /**
     * Runnable defines a single method: void run()
     * The class HAS-A task, which is passed to a Thread object.
     *
     * Advantage: The class can still extend another class.
     */
    static class MyRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println("[Approach 2 - Implementing Runnable]");
            System.out.println("  Thread name : " + Thread.currentThread().getName());
            System.out.println("  Thread ID   : " + Thread.currentThread().getId());
            System.out.println();
        }
    }

    // ========================================================================
    // MAIN METHOD — Entry point
    // ========================================================================
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("  STEP 1: THREAD CREATION IN JAVA");
        System.out.println("=".repeat(60));
        System.out.println();

        // Print info about the main thread
        Thread mainThread = Thread.currentThread();
        System.out.println("[Main Thread Info]");
        System.out.println("  Thread name : " + mainThread.getName());
        System.out.println("  Thread ID   : " + mainThread.getId());
        System.out.println("  Priority    : " + mainThread.getPriority());
        System.out.println();

        // ---- Approach 1: Extending Thread ----
        MyThread thread1 = new MyThread();
        thread1.setName("Worker-ExtendThread");
        thread1.start(); // start() spawns a NEW thread and calls run()

        // ---- Approach 2: Implementing Runnable ----
        Thread thread2 = new Thread(new MyRunnable(), "Worker-Runnable");
        thread2.start();

        // ---- Approach 3: Lambda expression (most concise) ----
        Thread thread3 = new Thread(() -> {
            System.out.println("[Approach 3 - Lambda Runnable]");
            System.out.println("  Thread name : " + Thread.currentThread().getName());
            System.out.println("  Thread ID   : " + Thread.currentThread().getId());
            System.out.println();
        }, "Worker-Lambda");
        thread3.start();

        // ---- Approach 4: Anonymous inner class ----
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("[Approach 4 - Anonymous Inner Class]");
                System.out.println("  Thread name : " + Thread.currentThread().getName());
                System.out.println("  Thread ID   : " + Thread.currentThread().getId());
                System.out.println();
            }
        }, "Worker-AnonInner");
        thread4.start();

        // ---- Wait for all threads to finish before printing summary ----
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Main thread interrupted while waiting!");
        }

        System.out.println("=".repeat(60));
        System.out.println("  KEY TAKEAWAYS:");
        System.out.println("=".repeat(60));
        System.out.println("""
            1. Extending Thread: Simple but limits inheritance.
            2. Implementing Runnable: Preferred — separates task from thread.
            3. Lambda Runnable: Most concise for simple tasks.
            4. Anonymous inner class: Verbose, but useful before Java 8.
            
            IMPORTANT:
            - Always call start(), NEVER call run() directly.
            - start() → creates new thread → OS schedules it → run() executes.
            - run()  → executes on the CURRENT thread (no new thread created).
            - Thread execution order is NOT guaranteed.
            """);
    }
}
