package lumina.task;

/**
 * These are tasks without any date or time attached to them
 */
public class TodoTask extends Task {
    /**
     * Constructor for todo task
     * @param description description of task
     */
    public TodoTask(String description) {
        super(description);
    }

    /**
     * Constructor for todo task
     *
     * @param description description of task
     * @param isDone status of task
     */
    public TodoTask(String description, boolean isDone) {
        super(description, isDone);
    }

    @Override
    public String saveString() {
        return "T | " + super.getStatusAndDescription();
    }

    @Override
    public boolean isUpcoming() {
        return false;
    }

    /**
     * Overrides string representation to show more complete information of
     * Todo task
     *
     * @return string representation of TodoTask
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        if (o instanceof TodoTask) {
            return true;
        }
        return false;
    }

}
