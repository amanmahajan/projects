package puller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PullerWorkers {

    static int THREAD_POOL_SIZE = 5;

    boolean isRunning = true;
     ExecutorService executor;
     public PullerWorkers() {
         executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
     }

     public static void start() {



     }



}
