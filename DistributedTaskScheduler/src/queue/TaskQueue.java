package queue;

import db.TaskDao;
import java.util.concurrent.PriorityBlockingQueue;

public class TaskQueue {

    PriorityBlockingQueue<TaskDao> pq;

   public TaskQueue() {
       this.pq = new PriorityBlockingQueue<>(100, (x, y) -> {
           if (x.getScheduledAt() < y.getScheduledAt()) {
               return 1;
           }
           return -1;
       });
   }


   public void addTask(final TaskDao taskDao) {
       this.pq.add(taskDao);
   }

   public TaskDao getTask() {
       if (this.isQueueEmpty()) {
           return null;
       }
       return pq.poll();
   }

   public synchronized boolean isQueueEmpty() {
       return pq.isEmpty();
   }






}
