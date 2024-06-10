package puller;

import db.TaskDao;

import java.util.ArrayList;
import java.util.List;

public class RunnablePullerTask implements Runnable {

    List<TaskDao> tasks = new ArrayList<>();


    public RunnablePullerTask(List<TaskDao> tasks) {
        this.tasks.addAll(tasks);

    }
    @Override
    public void run() {

    }
}
