package puller;

import db.TaskDb;


public class RunnablePullerTask implements Runnable {

    final TaskDb taskDb;

    public RunnablePullerTask(final TaskDb taskDb) {
       this.taskDb = taskDb;

    }
    @Override
    public void run() {
        this.taskDb.executeTransactionalTaskUpdate();
    }
}
