package core;

public interface SchedulerWorker {

    void run(Runnable run);

    static <T> ObservableTransformer<T, T> ioMain() {
        return upstream ->
                upstream.subscribeOn(SchedulerWorker::workerThread)
                        .observeOn(SchedulerWorker::mockUiThread);
    }

    static void workerThread(Runnable runnable) {
        new Thread(runnable, "worker thread").start();
    }


    static void mockUiThread(Runnable runnable) {
        new Thread(runnable, "ui thread").start();
    }
}
