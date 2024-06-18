import db.TaskDao;
import db.TaskDb;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

import java.util.concurrent.ScheduledExecutorService;

public class TaskController {

    TaskDb db;

    public TaskController(TaskDb db) {
        this.db = db;
    }

    public void addTasks(Task task) throws Exception {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        Runnable addTask = () -> {
            CompletableFuture.runAsync(() -> {
                TaskDao dao = new TaskDao();
                dao.setId(task.id);
                dao.setName(task.name);
                dao.setCommand(task.command);
                dao.setScheduledAt(task.getScheduledAt().getTime());
                try {
                    db.save(dao);
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }
                System.out.println("Successfully Creating the task " + task.id);
            });
        };

        // Schedule the task to run every 500 milliseconds
        scheduler.scheduleAtFixedRate(addTask, 0, 500, TimeUnit.MILLISECONDS);

        // Optionally, stop the scheduler after a certain period
        scheduler.schedule(() -> {
            scheduler.shutdown();
            System.out.println("Scheduler stopped.");
        }, 10, TimeUnit.SECONDS); // Stop after 10 seconds for this example


    }
}
