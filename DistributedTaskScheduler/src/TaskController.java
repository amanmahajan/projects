import db.TaskDao;
import db.TaskDb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskController {

    TaskDb db;

    public TaskController(TaskDb db) {
        this.db = db;
    }

    public void addTasks(Task task) throws Exception {
        TaskDao dao = new TaskDao();
        dao.setId(task.id);
        dao.setName(task.name);
        dao.setCommand(task.command);
        dao.setScheduledAt(task.getScheduledAt().getTime());
        try {
            db.save(dao);
        } catch (Exception e) {

            System.out.println("Failed to save task " + e);

            throw new RuntimeException(e);
        }
        System.out.println("Successfully Creating the task " + task.id);

    }
}
