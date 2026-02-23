package com.threads.step02_thread_lifecycle;

/**
 * ============================================================================
 * STEP 2: THREAD LIFECYCLE & STATES
 * ============================================================================
 *
 * Every thread in Java goes through a well-defined lifecycle with 6 states,
 * defined in the Thread.State enum:
 *
 *   ┌───────┐    start()    ┌──────────┐
 *   │  NEW  │ ─────────────►│ RUNNABLE │
 *   └───────┘               └────┬─────┘
 *                                │
 *              ┌─────────────────┼─────────────────┐
 *              │                 │                  │
 *              ▼                 ▼                  ▼
 *        ┌──────────┐    ┌───────────┐    ┌──────────────┐
 *        │ BLOCKED  │    │  WAITING  │    │ TIMED_WAITING│
 *        └────┬─────┘    └─────┬─────┘    └──────┬───────┘
 *              │                │                  │
 *              └─────────────────┼─────────────────┘
 *                                │
 *                                ▼
 *                          ┌────────────┐
 *                          │ TERMINATED │
 *                          └────────────┘
 *
 * STATES EXPLAINED:
 *
 * 1. NEW         → Thread object created, but start() not yet called.
 * 2. RUNNABLE    → Thread is eligible to run. It may be actually running
 *                  on the CPU or waiting for its turn from the OS scheduler.
 * 3. BLOCKED     → Thread is waiting to acquire a monitor lock (synchronized).
 * 4. WAITING     → Thread is waiting indefinitely for another thread to
 *                  perform an action (e.g. Object.wait(), Thread.join(),
 *                  LockSupport.park()).
 * 5. TIMED_WAITING → Like WAITING, but with a timeout (e.g. Thread.sleep(ms),
 *                    Object.wait(ms), Thread.join(ms)).
 * 6. TERMINATED  → Thread has completed execution (run() finished or
 *                  exception was thrown).
 *
 * KEY CONCEPTS:
 * - A thread can only be started ONCE. Calling start() again throws
 *   IllegalThreadStateException.
 * - getState() returns the current Thread.State enum value.
 * - RUNNABLE includes both "ready to run" and "actually running" — Java
 *   doesn't distinguish between them.
 * ============================================================================
 */
public class ThreadLifecycleDemo {

    // Shared lock object used to demonstrate BLOCKED state
    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=".repeat(60));
        System.out.println("  STEP 2: THREAD LIFECYCLE & STATES");
        System.out.println("=".repeat(60));
        System.out.println();

        // -----------------------------------------------------------------
        // DEMO 1: NEW and TERMINATED states
        // -----------------------------------------------------------------
        demo1_NewAndTerminated();

        // -----------------------------------------------------------------
        // DEMO 2: RUNNABLE state
        // -----------------------------------------------------------------
        demo2_Runnable();

        // -----------------------------------------------------------------
        // DEMO 3: TIMED_WAITING state (Thread.sleep)
        // -----------------------------------------------------------------
        demo3_TimedWaiting();

        // -----------------------------------------------------------------
        // DEMO 4: WAITING state (Thread.join / Object.wait)
        // -----------------------------------------------------------------
        demo4_Waiting();

        // -----------------------------------------------------------------
        // DEMO 5: BLOCKED state (waiting for a synchronized lock)
        // -----------------------------------------------------------------
        demo5_Blocked();

        // -----------------------------------------------------------------
        // DEMO 6: Full lifecycle observation
        // -----------------------------------------------------------------
        demo6_FullLifecycle();

        System.out.println("=".repeat(60));
        System.out.println("  KEY TAKEAWAYS:");
        System.out.println("=".repeat(60));
        System.out.println("""
            1. NEW         → Created but not started.
            2. RUNNABLE    → Running or ready to run (OS decides).
            3. BLOCKED     → Waiting for a monitor lock (synchronized).
            4. WAITING     → Waiting indefinitely (wait(), join(), park()).
            5. TIMED_WAITING → Waiting with timeout (sleep(), wait(ms), join(ms)).
            6. TERMINATED  → Finished execution.
            
            - A thread moves from NEW → RUNNABLE when start() is called.
            - A thread can never go back to NEW or be restarted.
            - BLOCKED, WAITING, TIMED_WAITING all return to RUNNABLE.
            - getState() is useful for debugging but NOT for synchronization.
            """);
    }

    // =====================================================================
    // DEMO 1: NEW → TERMINATED
    // =====================================================================
    static void demo1_NewAndTerminated() throws InterruptedException {
        System.out.println("--- Demo 1: NEW and TERMINATED states ---");

        Thread thread = new Thread(() -> {
            System.out.println("  Thread is running...");
        }, "Demo1-Thread");

        // Before start() → state is NEW
        System.out.println("  Before start() → State: " + thread.getState());   // NEW

        thread.start();
        thread.join(); // Wait for thread to finish

        // After thread completes → state is TERMINATED
        System.out.println("  After join()   → State: " + thread.getState());   // TERMINATED

        // Trying to start again throws IllegalThreadStateException
        try {
            thread.start();
        } catch (IllegalThreadStateException e) {
            System.out.println("  Restarting thread → " + e.getClass().getSimpleName()
                    + ": Cannot start a thread twice!");
        }

        System.out.println();
    }

    // =====================================================================
    // DEMO 2: RUNNABLE state
    // =====================================================================
    static void demo2_Runnable() throws InterruptedException {
        System.out.println("--- Demo 2: RUNNABLE state ---");

        Thread thread = new Thread(() -> {
            // Busy work to keep the thread in RUNNABLE state
            long sum = 0;
            for (int i = 0; i < 100_000_000; i++) {
                sum += i;
            }
            // Use sum to prevent compiler optimization
            if (sum < 0) System.out.println("  Unexpected");
        }, "Demo2-Runnable");

        thread.start();

        // Check state while the thread is doing busy work
        // Note: This is a "best effort" check — timing depends on the OS scheduler
        Thread.State state = thread.getState();
        System.out.println("  During busy work → State: " + state);  // Likely RUNNABLE

        thread.join();
        System.out.println("  After completion → State: " + thread.getState()); // TERMINATED
        System.out.println();
    }

    // =====================================================================
    // DEMO 3: TIMED_WAITING state (Thread.sleep)
    // =====================================================================
    static void demo3_TimedWaiting() throws InterruptedException {
        System.out.println("--- Demo 3: TIMED_WAITING state (Thread.sleep) ---");

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(2000); // Sleep for 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Demo3-TimedWaiting");

        thread.start();

        // Give the thread a moment to enter sleep
        Thread.sleep(100);

        // While sleeping → state is TIMED_WAITING
        System.out.println("  During sleep()  → State: " + thread.getState()); // TIMED_WAITING

        thread.join();
        System.out.println("  After join()    → State: " + thread.getState()); // TERMINATED
        System.out.println();
    }

    // =====================================================================
    // DEMO 4: WAITING state (Thread.join)
    // =====================================================================
    static void demo4_Waiting() throws InterruptedException {
        System.out.println("--- Demo 4: WAITING state (Thread.join) ---");

        // Inner thread that takes time to complete
        Thread innerThread = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Demo4-Inner");

        // Outer thread that calls join() on the inner thread → enters WAITING
        Thread outerThread = new Thread(() -> {
            try {
                innerThread.join(); // This will cause WAITING state
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Demo4-Outer-WaitingOnJoin");

        innerThread.start();
        outerThread.start();

        // Let outerThread enter wait
        Thread.sleep(100);

        // outerThread is waiting for innerThread to finish → WAITING
        System.out.println("  outerThread during join() → State: " + outerThread.getState()); // WAITING
        System.out.println("  innerThread during sleep  → State: " + innerThread.getState()); // TIMED_WAITING

        innerThread.join();
        outerThread.join();
        System.out.println("  Both threads completed    → outer: " + outerThread.getState()
                + ", inner: " + innerThread.getState()); // TERMINATED
        System.out.println();
    }

    // =====================================================================
    // DEMO 5: BLOCKED state (waiting for synchronized lock)
    // =====================================================================
    static void demo5_Blocked() throws InterruptedException {
        System.out.println("--- Demo 5: BLOCKED state (synchronized lock) ---");

        // Thread 1 acquires the lock and holds it for 2 seconds
        Thread thread1 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("  Thread1 acquired the lock");
                try {
                    Thread.sleep(2000); // Hold lock for 2 seconds
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("  Thread1 releasing the lock");
            }
        }, "Demo5-HoldsLock");

        // Thread 2 tries to acquire the same lock → will be BLOCKED
        Thread thread2 = new Thread(() -> {
            synchronized (lock) {
                System.out.println("  Thread2 acquired the lock");
            }
        }, "Demo5-WantsLock");

        thread1.start();
        Thread.sleep(100); // Ensure thread1 acquires lock first

        thread2.start();
        Thread.sleep(100); // Give thread2 time to attempt lock acquisition

        // thread2 is trying to enter synchronized block → BLOCKED
        System.out.println("  Thread1 (holds lock)  → State: " + thread1.getState()); // TIMED_WAITING
        System.out.println("  Thread2 (wants lock)  → State: " + thread2.getState()); // BLOCKED

        thread1.join();
        thread2.join();
        System.out.println("  Both threads completed → t1: " + thread1.getState()
                + ", t2: " + thread2.getState()); // TERMINATED
        System.out.println();
    }

    // =====================================================================
    // DEMO 6: Full lifecycle observation
    // =====================================================================
    static void demo6_FullLifecycle() throws InterruptedException {
        System.out.println("--- Demo 6: Full Lifecycle Observation ---");

        Thread thread = new Thread(() -> {
            try {
                // Phase 1: Do some work (RUNNABLE)
                long sum = 0;
                for (int i = 0; i < 50_000_000; i++) sum += i;
                if (sum < 0) System.out.println("nope");

                // Phase 2: Sleep (TIMED_WAITING)
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Demo6-Lifecycle");

        // Observe state transitions
        System.out.println("  Created         → State: " + thread.getState()); // NEW

        thread.start();

        // Poll the state a few times to observe transitions
        Thread.State previousState = null;
        while (thread.isAlive()) {
            Thread.State currentState = thread.getState();
            if (currentState != previousState) {
                System.out.println("  State changed   → State: " + currentState);
                previousState = currentState;
            }
            Thread.sleep(50); // Poll every 50ms
        }

        System.out.println("  Finished        → State: " + thread.getState()); // TERMINATED
        System.out.println();
    }
}
