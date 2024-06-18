package puller;

import db.TaskDb;


public class RunnablePullerTask implements Runnable {

    final TaskDb taskDb;

    public RunnablePullerTask(final TaskDb taskDb) {
       this.taskDb = taskDb;

    }
    @Override
    public void run() {
        try {
            this.taskDb.executeTransactionalTaskUpdate();
        } catch (Exception ex) {
            System.out.println("Not able to pull the task " + ex);
        }

    }
}
