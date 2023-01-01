package com.hirshi001.javarestapi;

import com.hirshi001.restapi.RestFuture;
import com.hirshi001.restapi.RestFutureConsumer;
import com.hirshi001.restapi.RestFutureFactory;
import com.hirshi001.restapi.ScheduledExec;

import java.util.concurrent.*;

public class JavaRestFutureFactory implements RestFutureFactory {

    private static final ScheduledExec DEFAULT_EXECUTOR = new ScheduledExec() {
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
    };


    @Override
    public <T> RestFuture<T, T> create() {
        return create(DEFAULT_EXECUTOR, true, (RestFutureConsumer<T, T>) null);
    }

    @Override
    public <T> RestFuture<T, T> create(ScheduledExec executor) {
        return create(executor, true, (RestFutureConsumer<T, T>) null);
    }

    @Override
    public <T> RestFuture<T, T> create(RestFutureConsumer<T, T> consumer) {
        return create(DEFAULT_EXECUTOR, consumer);
    }

    @Override
    public <T> RestFuture<T, T> create(ScheduledExec executor, RestFutureConsumer<T, T> consumer) {
        return create(executor, true, consumer);
    }

    @Override
    public <T> RestFuture<T, T> create(ScheduledExec executor, boolean cancel, RestFutureConsumer<T, T> consumer) {
        return new JavaRestFuture<>(executor, cancel, consumer, null);
    }

    @Override
    public <T> RestFuture<T, T> create(Callable<T> action) {
        return create(DEFAULT_EXECUTOR, true, action);
    }

    @Override
    public <T> RestFuture<T, T> create(ScheduledExec executor, Callable<T> action) {
        return create(executor, true, action);
    }

    @Override
    public <T> RestFuture<T, T> create(ScheduledExec executor, boolean cancel, Callable<T> action) {
        return new JavaRestFuture<>(executor, cancel, (future, input) -> {
            try {
                T result = action.call();
                future.taskFinished(result);
            } catch (Exception e) {
                future.setCause(e);
            }
        }, null);
    }
}
