package db;

import queue.TaskQueue;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDb {


    public static final String SaveQuery = "INSERT INTO task (id, name, scheduledAt, command, completedAt, picketAt, endedAt, failedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private  Connection connection;

    private final TaskQueue taskQueue;

    public  TaskDb(Connection connection, TaskQueue queue) {
        this.connection = connection;
        this.taskQueue = queue;
    }


    public TaskDao save(TaskDao task) throws Exception {

        if (this.connection == null) {
            throw new Exception("No connection is found");
        }


        PreparedStatement statement = connection.prepareStatement("INSERT INTO task (id, name, scheduledAt, command, completedAt, picketAt, endedAt, failedAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
        statement.setString(1, task.getId());
        statement.setString(2, task.getName());
        statement.setLong(3, task.getScheduledAt());
        statement.setString(4, task.getCommand());
        statement.setLong(5, task.getCompletedAt());
        statement.setLong(6, task.getPicketAt());
        statement.setLong(7, task.getEndedAt());
        statement.setLong(8, task.getFailedAt());

        int rowsInserted = statement.executeUpdate();
        if (rowsInserted > 0) {
            System.out.println("Task saved to database successfully.");
        } else {
            System.out.println("Task was not saved to database.");
        }
        System.out.println("Task saved to database successfully.");


        return task;
    }

    public TaskDao getTask(final String id) throws Exception {

        if (this.connection == null) {
            throw new Exception("No connection is found");
        }

        TaskDao task = null;
        String query = "SELECT * FROM task WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = new TaskDao();
                task.setId(resultSet.getString("id"));
                task.setName(resultSet.getString("name"));
                task.setScheduledAt(resultSet.getLong("scheduledAt"));
                task.setCommand(resultSet.getString("command"));
                task.setCompletedAt(resultSet.getLong("completedAt"));
                task.setPicketAt(resultSet.getLong("picketAt"));
                task.setEndedAt(resultSet.getLong("endedAt"));
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving task from database: " + ex.getMessage());
            throw ex;
        }


        return task;
    }

    public void executeTransactionalTaskUpdate() throws Exception {
        if (this.connection == null) {
            throw new Exception("No connection is found");
        }
        try {
            // Begin transaction

            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            // Step 1: Select tasks scheduled 30 seconds ago with picket_at null
            final String selectQuery = new StringBuilder()
                    .append("SELECT id FROM task ")
                    .append("WHERE scheduledAt <= UNIX_TIMESTAMP(NOW() - INTERVAL 30 SECOND) ")
                    .append("AND picketAt IS NULL ")
                    .append("FOR UPDATE SKIP LOCKED;")
                    .toString();

            var resultSet = statement.executeQuery(selectQuery);

            if (!resultSet.next()) {
                System.out.println("No task found");
                return;
            }

            // Store the IDs of selected tasks
            List<TaskDao> selectedTasks = new ArrayList<>();
            while (resultSet.next()) {
                TaskDao dao = new TaskDao();
                dao.setId(resultSet.getString("id"));
                dao.setName(resultSet.getString("name"));
                dao.setScheduledAt(resultSet.getLong("scheduledAt"));
                dao.setCommand(resultSet.getString("command"));
                selectedTasks.add(dao);
            }

            // Write to Priority Q

            for(TaskDao dao: selectedTasks) {

                System.out.println("Picking up the task " + dao.id);
                this.taskQueue.addTask(dao);
            }


            // Step 2: Update picket_at for the selected tasks
            StringBuilder updateQuery = new StringBuilder("UPDATE task SET picket_at = UNIX_TIMESTAMP(NOW()) WHERE id IN (");
            for (int i = 0; i < selectedTasks.size(); i++) {
                updateQuery.append("'").append(selectedTasks.get(i).id).append("'");
                if (i < selectedTasks.size() - 1) {
                    updateQuery.append(", ");
                }
            }
            updateQuery.append(");");
            statement.executeUpdate(updateQuery.toString());

            // Commit transaction
            connection.commit();
            System.out.println("Transaction committed successfully.");
        } catch (SQLException e) {
            System.out.println("Transaction failed! Rolling back changes. Error: " + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException rollbackException) {
                System.out.println("Rollback failed! Error: " + rollbackException.getMessage());
            }
        }
    }

    public void updateCompletedAt(String taskId) throws Exception  {

        PreparedStatement preparedStatement = null;

        if (this.connection == null) {
            throw new Exception("No connection is found");
        }

        try {
            String updateSQL = "UPDATE task SET completedAt = ? WHERE id = ?";
            preparedStatement = connection.prepareStatement(updateSQL);

            long now = System.currentTimeMillis();
            preparedStatement.setLong(1, now);
            preparedStatement.setString(2, taskId);

            int rowsAffected = preparedStatement.executeUpdate();

            // Checking if the update was successful
            if (rowsAffected > 0) {
                System.out.println("Task " + taskId + " updated successfully.");
            } else {
                System.out.println("No task found with ID: " + taskId);
            }



        } catch (final Exception ex) {
            throw ex;

        }



    }




}



