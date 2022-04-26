package com.cache;


import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.atomic.AtomicLong;

public final class FalseSharingNoPadded
        implements Runnable
{
    public final static int NUM_THREADS = 4; // change
    public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static AtomicLong[] longs = new AtomicLong[NUM_THREADS];
    static
    {
        for (int i = 0; i < longs.length; i++)
        {
            longs[i] = new AtomicLong();
        }
    }

    public FalseSharingNoPadded(final int arrayIndex)
    {
        this.arrayIndex = arrayIndex;
    }

    public static void main(final String[] args) throws Exception
    {

        System.out.println(ClassLayout.parseClass(AtomicLong.class).toPrintable());
        Thread.sleep(5000);
        System.gc();


        final long start = System.nanoTime();
        runTest();
        System.out.println("Ran " + ITERATIONS + " iterations in duration = " + (System.nanoTime() - start));
    }

    private static void runTest() throws InterruptedException
    {
        Thread[] threads = new Thread[NUM_THREADS];

        for (int i = 0; i < threads.length; i++)
        {
            threads[i] = new Thread(new FalseSharingNoPadded(i));
        }

        for (Thread t : threads)
        {
            t.start();
        }

        for (Thread t : threads)
        {
            t.join();
        }
    }

    public void run()
    {
        long i = ITERATIONS + 1;
        while (0 != --i)
        {
            longs[arrayIndex].set(i);
        }
    }


}
