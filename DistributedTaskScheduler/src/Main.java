import db.MysqlConnector;
import db.TaskDb;
import executors.ExecutorWorkers;
import puller.PullerWorkers;
import queue.TaskQueue;

public class Main {
    public static void main(String[] args) {

        MysqlConnector connector = new MysqlConnector();
        TaskQueue queue = new TaskQueue();
        TaskDb db = new TaskDb(connector.connection(), queue);

        TaskController controller = new TaskController(db);
        TaskCreator creator = new TaskCreator(controller);
        creator.createTasks();



        PullerWorkers pullerWorkers = new PullerWorkers(db);
        ExecutorWorkers executorWorkers = new ExecutorWorkers(db, queue);
      //  pullerWorkers.start();
     //   executorWorkers.start();





    }
}