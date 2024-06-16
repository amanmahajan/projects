import db.TaskDao;
import db.TaskDb;

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
        this.db.save(dao);
    }
}
