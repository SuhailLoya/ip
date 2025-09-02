package lumina.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This is task with a deadline
 */
public class DeadlineTask extends Task {
    private LocalDate byDateObject;

    /**
     * Constructor for Deadline Task
     *
     * @param description description of task
     * @param byDateObject  deadline of task
     */
    public DeadlineTask(String description, LocalDate byDateObject) {
        super(description);
        this.byDateObject = byDateObject;
    }

    /**
     * Constructor for Deadline Task
     *
     * @param description description of task
     * @param byDateObject  deadline of task
     * @param isDone status of task
     */
    public DeadlineTask(String description, LocalDate byDateObject, boolean isDone) {
        super(description, isDone);
        this.byDateObject = byDateObject;
    }

    @Override
    public String saveString() {
        return "D | " + super.getStatusAndDescription() + " | "
                + byDateObject.toString();
    }

    @Override
    public boolean isUpcoming() {
        return this.byDateObject.isAfter(LocalDate.now());
    }

    /**
     * Overrides string representation to show more complete information of Deadline task
     *
     * @return string representation of Deadline task
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: "
                + byDateObject.format(DateTimeFormatter.ofPattern("MMM d yyyy")) + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (obj instanceof DeadlineTask) {
            DeadlineTask other = (DeadlineTask) obj;
            return this.byDateObject.equals(other.byDateObject);
        }
        return false;
    }
}
