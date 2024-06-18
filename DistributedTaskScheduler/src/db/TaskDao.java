package db;

public class TaskDao {

    String id;

    String name;


    long scheduledAt;

    String command;

    long completedAt;

    long picketAt;

    long endedAt;

    long failedAt;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getScheduledAt() {
        return scheduledAt;
    }

    public String getCommand() {
        return command;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public long getPicketAt() {
        return picketAt;
    }

    public long getEndedAt() {
        return endedAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScheduledAt(long scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public void setPicketAt(long picketAt) {
        this.picketAt = picketAt;
    }

    public void setEndedAt(long endedAt) {
        this.endedAt = endedAt;
    }

    public long getFailedAt() {
        return failedAt;
    }

    public void setFailedAt(long failedAt) {
        this.failedAt = failedAt;
    }


}
