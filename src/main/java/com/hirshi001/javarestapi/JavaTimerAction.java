package com.hirshi001.javarestapi;

import com.hirshi001.restapi.TimerAction;

import java.util.concurrent.ScheduledFuture;

public class JavaTimerAction extends TimerAction {

    private final ScheduledFuture<?> future;

    public JavaTimerAction(ScheduledFuture<?> future, long initialDelay, long delay, Runnable runnable) {
        super(initialDelay, delay, runnable);
        this.future = future;
    }

    @Override
    public void cancel() {
        super.cancel();
        future.cancel(true);
    }



}
