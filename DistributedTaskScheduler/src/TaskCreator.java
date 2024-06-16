import java.time.Instant;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskCreator {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    AtomicInteger id = new AtomicInteger(0);

    TaskController taskController;

    public TaskCreator(final TaskController taskController) {
        this.taskController = taskController;
    }


    public void createTasks() {
        Task task = new Task();
        task.setId(String.valueOf(this.getId()));
        task.setName(this.getTaskName());
        task.setScheduledAt(Date.from(Instant.now()));

        task.setCommand("Execute the task: " + task.getId());
        try {
            this.taskController.addTasks(task);
        } catch (Exception e) {
            System.out.println("Something bad happened in adding the task " + e);
        }


    }

    private int getId() {
        return id.incrementAndGet();
    }

    private String getTaskName() {
        // Method to generate a random string of a given length
        Random random = new Random();
        int length = 10;
        StringBuilder stringBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            stringBuilder.append(CHARACTERS.charAt(randomIndex));
        }

        return stringBuilder.toString();
    }


}
