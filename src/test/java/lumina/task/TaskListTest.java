package lumina.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import lumina.parser.Parser;
import lumina.ui.Ui;

public class TaskListTest {

    @Test
    public void toLines_successful() {
        TaskList taskList = new TaskList(new Ui(), new Parser());
        taskList.addTask(new TodoTask("a todo task"));
        taskList.addTask(new EventTask("an event task",
                LocalDate.parse("2024-01-01"), LocalDate.parse("2024-01-02")));
        taskList.addTask(new DeadlineTask("a deadline task",
                LocalDate.parse("2024-01-03")));
        ArrayList<String> expectedOutput = new ArrayList<>();
        expectedOutput.add("T | 0 | a todo task");
        expectedOutput.add("E | 0 | an event task | 2024-01-01 | 2024-01-02");
        expectedOutput.add("D | 0 | a deadline task | 2024-01-03");
        assertEquals(expectedOutput, taskList.toLines());
    }

    @Test
    public void toLines_testEmptyList() {
        TaskList taskList = new TaskList(new Ui(), new Parser());
        assertEquals(0, taskList.toLines().size());
    }
}
