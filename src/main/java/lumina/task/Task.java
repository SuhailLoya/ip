package lumina.task;

/**
 * Abstract class representing a task
 */
public abstract class Task {
    private String description;
    private boolean isDone;

    /**
     * Constructor for Task
     *
     * @param description description of the task
     */
    public Task(String description) {
        description = description.trim();
        this.description = description;
        this.isDone = false;
    }

    /**
     * Constructor for Task
     *
     * @param description description of the task
     * @param isDone status of task
     */
    public Task(String description, boolean isDone) {
        description = description.trim();
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns string representation of status and description for save file
     *
     * @return string representation of status and description for save file
     */
    public String getStatusAndDescription() {
        return (this.isDone ? "1" : "0") + " | " + this.description;
    }

    /**
     * Returns a string representation of the task suitable for saving
     *
     * @return string representation of the task in data file
     */
    public abstract String saveString();

    /**
     * Returns status icon of a given task
     *
     * @return status icon which is 'X' if done else ' '
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }



    /**
     * Marks the status of task as done
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * marks the staus of task as not done
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Overrides the toString method to give appropriate string representation of
     * a task which is status icon in square brackets followed by the description
     *
     * @return string representation of a task
     */
    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Checks whether task has searchStr in their description
     *
     * @param searchStr search string
     * @return true if searchStr is found in desc; else false
     */
    public boolean findInDescription(String searchStr) {
        return this.description.toLowerCase().contains(searchStr.toLowerCase());
    }

    /**
     * Checks whether a task is upcoming
     *
     * @return true if the task is upcoming; else false
     */
    public abstract boolean isUpcoming();

    /**
     * Checks for equality of two tasks
     *
     * @param obj object to be checked
     * @return true if objects are equal; else false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Task) {
            Task task = (Task) obj;
            return task.description.equals(this.description) && task.isDone == this.isDone;
        }
        return false;
    }
}
