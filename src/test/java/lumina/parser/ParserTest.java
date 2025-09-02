package lumina.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import lumina.exception.LuminaException;
import lumina.task.DeadlineTask;
import lumina.task.EventTask;
import lumina.task.Task;
import lumina.task.TodoTask;

public class ParserTest {

    @Test
    public void parseDateString_success() {
        try {
            assertEquals(LocalDate.parse("2024-01-01"),
                    new Parser().parseDateString("2024-01-01"));
        } catch (LuminaException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void parseDateString_exceptionThrown() {
        try {
            assertEquals(LocalDate.parse("2024-01-01"),
                    new Parser().parseDateString(""));
            fail();
        } catch (LuminaException e) {
            assertEquals("Invalid date format! Please try again", e.getMessage());
        }
    }

    @Test
    void parseDataLine_validTodoTask() {
        Parser parser = new Parser();
        String line = "T | 1 | Read book";

        Task task = parser.parseDataLine(line);

        assertNotNull(task, "Task should not be null");
        assertTrue(task instanceof TodoTask, "Task should be of type TodoTask");
        assertEquals(new TodoTask("Read book", true), task);
    }

    @Test
    void parseDataLine_validDeadlineTask() {
        Parser parser = new Parser();
        String line = "D | 0 | Return book | 2024-06-06";

        Task task = parser.parseDataLine(line);

        assertNotNull(task, "Task should not be null");
        assertTrue(task instanceof DeadlineTask, "Task should be of type DeadlineTask");
        assertEquals(new DeadlineTask("Return book",
                LocalDate.parse("2024-06-06"), false), task);
    }

    @Test
    void parseDataLine_validEventTask() {
        Parser parser = new Parser();
        String line = "E | 0 | Project meeting | 2024-08-06 | 2024-08-06";

        Task task = parser.parseDataLine(line);

        assertNotNull(task, "Task should not be null");
        assertTrue(task instanceof EventTask, "Task should be of type EventTask");
        assertEquals(new EventTask("Project meeting",
                LocalDate.parse("2024-08-06"), LocalDate.parse("2024-08-06"),
                false), task);

    }
}
