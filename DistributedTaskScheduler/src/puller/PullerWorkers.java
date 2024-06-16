package puller;

import db.TaskDb;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PullerWorkers {

    static int THREAD_POOL_SIZE = 5;

    TaskDb taskDb;

    static boolean isRunning = true;
     ExecutorService executor;
     public PullerWorkers(TaskDb taskDb) {
         executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
         this.taskDb = taskDb;
     }

     public  void start() {

         while(isRunning) {
             RunnablePullerTask runnablePullerTask = new RunnablePullerTask(taskDb);
             this.executor.submit(runnablePullerTask);

         }
     }

     public void stop() {
         isRunning = false;
     }




}
