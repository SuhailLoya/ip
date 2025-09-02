package lumina.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import lumina.exception.LuminaException;
import lumina.task.DeadlineTask;
import lumina.task.EventTask;
import lumina.task.Task;
import lumina.task.TodoTask;

/**
 * Utility class for parsing dates and task data.
 */
public class Parser {

    /**
     * Parses a date string into a LocalDate object.
     *
     * @param date the date string to parse
     * @return the parsed LocalDate object
     * @throws LuminaException if the date string is in an invalid format
     */
    public LocalDate parseDateString(String date) throws LuminaException {
        LocalDate ret = null;

        try {
            ret = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new LuminaException("Invalid date format! Please try again");
        }

        return ret;
    }

    /**
     * Parses a data line from the storage file into a Task object.
     * The data line should be in the format: "`type` | `done` | `description` | `additionalData`"
     * The type can be "T" for TodoTask, "D" for DeadlineTask, or "E" for EventTask.
     *
     * @param line the data line to parse
     * @return the parsed Task object, or null if the line is invalid
     */
    public Task parseDataLine(String line) {
        String[] parts = line.split(" \\| ");
        assert parts.length > 1 : "Line should have atleast 2 parts";
        Task task = null;
        try {
            assert parts.length >= 3 : "line should have atleast 3 parts";
            String type = parts[0].trim();
            boolean isDone = parts[1].trim().equals("1");
            String description = parts[2].trim();
            switch (type) {
            case "T":
                assert parts.length == 3 : "Todo task should have 3 parts";
                task = new TodoTask(description, isDone);
                break;
            case "D":
                assert parts.length == 4 : "Deadline task should have 4 parts";
                LocalDate byDateObject = parseDateString(parts[3].trim());
                task = new DeadlineTask(description, byDateObject, isDone);
                break;
            case "E":
                assert parts.length == 5 : "Event task should have 5 parts";
                LocalDate startDateObject = parseDateString(parts[3].trim());
                LocalDate endDateObject = parseDateString(parts[4].trim());
                task = new EventTask(description, startDateObject, endDateObject, isDone);
                break;
            default:
                throw new LuminaException(
                        String.format("Corrupt data entry: %s", line)
                );
            }
        } catch (LuminaException e) {
            System.err.println(e.getMessage());
        }
        return task;
    }
}
