package com.hirshi001.javarestapi;

import com.hirshi001.restapi.ScheduledExec;
import com.hirshi001.restapi.TimerAction;

import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JavaScheduledExecutor implements ScheduledExec {

    final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(ForkJoinPool.getCommonPoolParallelism());

    @Override
    public void run(Runnable runnable, long delay) {
        scheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run(Runnable runnable, long delay, TimeUnit period) {
        scheduledExecutorService.schedule(runnable, delay, period);
    }

    @Override
    public void runDeferred(Runnable runnable) {
        scheduledExecutorService.execute(runnable);
    }

    @Override
    public TimerAction repeat(Runnable runnable, long initialDelay, long delay, TimeUnit period) {
        return new JavaTimerAction(scheduledExecutorService.scheduleAtFixedRate(runnable, initialDelay, delay, period), initialDelay, delay, runnable);
    }
}
