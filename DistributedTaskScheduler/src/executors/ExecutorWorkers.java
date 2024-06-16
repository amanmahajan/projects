package executors;

import db.TaskDao;
import db.TaskDb;
import queue.TaskQueue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorWorkers {

    static int THREAD_POOL_SIZE = 5;

    static boolean isRunning = true;


    ExecutorService executor;
    TaskDb taskDb;

    TaskQueue taskQueue;


    public ExecutorWorkers(TaskDb taskDb, TaskQueue taskQueue) {
        this.taskDb = taskDb;
        this.executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        this.taskQueue = taskQueue;

    }

    public void start() {

        while (isRunning) {
            TaskDao taskDao = this.taskQueue.getTask();
            if (taskDao != null) {
                ExecutorRunnableWorker runnableWorker = new ExecutorRunnableWorker(taskDao, this.taskDb);
                this.executor.submit(runnableWorker);

            }

        }


    }

    public void stop() {
        isRunning = false;

    }


}
