package data.task;

//  Deadlines: data.tasks that need to be done before a specific date/time
//  e.g., submit report by 11/10/2019 5pm

/**
 * Represents a Deadline task in the task list.
 */
import java.time.LocalDateTime;

public class Deadline extends Task {

    protected LocalDateTime deadline;
    protected String deadlineStr;

    public Deadline(String description, LocalDateTime deadline) {
        super(description);
        this.deadline = deadline;
        this.deadlineStr = deadline.format(super.formatter);
    }

    public Deadline(boolean isDone, String description, LocalDateTime deadline) {
        super(isDone, description);
        this.deadline = deadline;
        this.deadlineStr = deadline.format(super.formatter);
    }

    @Override
    public String toString() {
        return String.format("[D]%1$s (by: %2$s)", super.toString(), this.deadlineStr);
    }

    @Override
    public String fileFormat() {
        return String.format("%1$s/%2$s/%3$s/%4$s", "D", super.getStatusIcon(), super.description, this.deadlineStr);
    }

}