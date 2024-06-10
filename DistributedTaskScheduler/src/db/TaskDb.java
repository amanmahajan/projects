package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDb {


    public static final String SaveQuery = "INSERT INTO tasks (id, name, scheduled_at, command, completed_at, picket_at, ended_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private Connection connection;

    public void TaskDb(Connection connection) {
        this.connection = connection;
    }


    public TaskDao save(TaskDao task) throws Exception {

        if (this.connection == null) {
            throw new Exception("No connection is found");
        }
        try {
            PreparedStatement statement = connection.prepareStatement(SaveQuery);
            statement.setString(1, task.getId());
            statement.setString(2, task.getName());
            statement.setLong(3, task.getScheduledAt());
            statement.setString(4, task.getCommand());
            statement.setLong(5, task.getCompletedAt());
            statement.setLong(6, task.getPicketAt());
            statement.setLong(7, task.getEndedAt());

            statement.executeUpdate();
            System.out.println("Task saved to database successfully.");
        } catch (SQLException e) {
            System.out.println("Error saving task to database: " + e.getMessage());

        }
        return task;
    }

    public TaskDao getTask(final String id) throws SQLException {

        TaskDao task = null;
        String query = "SELECT * FROM tasks WHERE id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(query);
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                task = new TaskDao();
                task.setId(resultSet.getString("id"));
                task.setName(resultSet.getString("name"));
                task.setScheduledAt(resultSet.getLong("scheduled_at"));
                task.setCommand(resultSet.getString("command"));
                task.setCompletedAt(resultSet.getLong("completed_at"));
                task.setPicketAt(resultSet.getLong("picket_at"));
                task.setEndedAt(resultSet.getLong("ended_at"));
            }
        } catch (Exception ex) {
            System.out.println("Error retrieving task from database: " + ex.getMessage());
            throw ex;
        }


        return task;
    }

    public void executeTransactionalTaskUpdate() {
        try {
            // Begin transaction

            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();

            // Step 1: Select tasks scheduled 30 seconds ago with picket_at null
            final String selectQuery = new StringBuilder()
                    .append("SELECT id FROM tasks ")
                    .append("WHERE scheduled_at <= UNIX_TIMESTAMP(NOW() - INTERVAL 30 SECOND) ")
                    .append("AND picket_at IS NULL ")
                    .append("FOR UPDATE SKIP LOCKED;")
                    .toString();

            var resultSet = statement.executeQuery(selectQuery);

            // Store the IDs of selected tasks
            List<String> selectedIds = new ArrayList<>();
            while (resultSet.next()) {
                selectedIds.add(resultSet.getString("id"));
            }

            // Step 2: Update picket_at for the selected tasks
            StringBuilder updateQuery = new StringBuilder("UPDATE tasks SET picket_at = UNIX_TIMESTAMP(NOW()) WHERE id IN (");
            for (int i = 0; i < selectedIds.size(); i++) {
                updateQuery.append("'").append(selectedIds.get(i)).append("'");
                if (i < selectedIds.size() - 1) {
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


}



