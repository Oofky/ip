package bogos;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    @Test
    public void parseTask_todo_success() throws BogosException {
        Parser parser = new Parser();

        Task task = parser.parseTask("todo CS3241 assignment");

        Todo todo = assertInstanceOf(Todo.class, task);
        assertEquals("CS3241 assignment", todo.getDescription());
        assertFalse(todo.isDone());
    }

    @Test
    public void parseTask_nonsense_exceptionThrown() {
        Parser parser = new Parser();

        try {
            Task task = parser.parseTask("todooles homework");
            fail(); // the test should not reach this line
        } catch (BogosException e) {
            assertEquals("bwhat", e.getMessage());
        }
    }

}
