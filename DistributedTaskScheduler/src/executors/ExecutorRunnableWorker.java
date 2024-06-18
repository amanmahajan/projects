package executors;

import db.TaskDao;
import db.TaskDb;

public class ExecutorRunnableWorker implements Runnable{



    final TaskDao taskDao;
    final TaskDb taskDb;

    public ExecutorRunnableWorker(final TaskDao taskDao, final TaskDb taskDb) {
        this.taskDao = taskDao;
        this.taskDb = taskDb;

    }
    @Override
    public void run() {

        // Executing the task

        try {
            Thread.sleep(1000);
            System.out.println("Executing the task: " +  this.taskDao.getName());

            this.taskDb.updateCompletedAt(taskDao.getId());

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
}
