import db.MysqlConnector;
import db.TaskDb;
import executors.ExecutorWorkers;
import puller.PullerWorkers;
import queue.TaskQueue;

public class Main {
    public static void main(String[] args) {

        MysqlConnector connector = new MysqlConnector();
        TaskDb db = new TaskDb(connector.connection());

        TaskController controller = new TaskController(db);
        TaskCreator creator = new TaskCreator(controller);
        creator.createTasks();

        TaskQueue queue = new TaskQueue();

        PullerWorkers pullerWorkers = new PullerWorkers(db);
        ExecutorWorkers executorWorkers = new ExecutorWorkers(db, queue);
        pullerWorkers.start();
        executorWorkers.start();





    }
}