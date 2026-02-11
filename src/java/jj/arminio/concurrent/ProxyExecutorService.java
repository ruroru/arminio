package jj.arminio.concurrent;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

public record ProxyExecutorService(ExecutorService executor) implements Executor, ExecutorService {

    @Override
    public void execute(Runnable task) {
        Thread.ofVirtual().start(() -> {
            try {
                executor.submit(task).get();
            } catch (InterruptedException | ExecutionException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    @Override
    public void shutdown() {
        executor.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }

    @Override
    public boolean isShutdown() {
        return executor.isShutdown();
    }

    @Override
    public boolean isTerminated() {
        return executor.isTerminated();
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            try {
                T result = executor.submit(task).get();
                future.complete(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (ExecutionException e) {
                future.completeExceptionally(e.getCause());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            try {
                executor.submit(task, result).get();
                future.complete(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (ExecutionException e) {
                future.completeExceptionally(e.getCause());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public Future<?> submit(Runnable task) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            try {
                executor.submit(task).get();
                future.complete(null);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (ExecutionException e) {
                future.completeExceptionally(e.getCause());
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        try {
            CompletableFuture<List<Future<T>>> future = new CompletableFuture<>();
            Thread.ofVirtual().start(() -> {
                try {
                    List<Future<T>> results = executor.invokeAll(tasks);
                    future.complete(results);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    future.completeExceptionally(e);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            return future.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException {
        try {
            CompletableFuture<List<Future<T>>> future = new CompletableFuture<>();
            Thread.ofVirtual().start(() -> {
                try {
                    List<Future<T>> results = executor.invokeAll(tasks, timeout, unit);
                    future.complete(results);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    future.completeExceptionally(e);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            return future.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            throw new RuntimeException(e.getCause());
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks)
            throws InterruptedException, ExecutionException {
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            try {
                T result = executor.invokeAny(tasks);
                future.complete(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ExecutionException) {
                throw (ExecutionException) e.getCause();
            }
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            throw new ExecutionException(e.getCause());
        }
    }

    @Override
    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit)
            throws InterruptedException, ExecutionException, TimeoutException {
        CompletableFuture<T> future = new CompletableFuture<>();
        Thread.ofVirtual().start(() -> {
            try {
                T result = executor.invokeAny(tasks, timeout, unit);
                future.complete(result);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.completeExceptionally(e);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof ExecutionException) {
                throw (ExecutionException) e.getCause();
            }
            if (e.getCause() instanceof InterruptedException) {
                throw (InterruptedException) e.getCause();
            }
            if (e.getCause() instanceof TimeoutException) {
                throw (TimeoutException) e.getCause();
            }
            throw new ExecutionException(e.getCause());
        }
    }
}