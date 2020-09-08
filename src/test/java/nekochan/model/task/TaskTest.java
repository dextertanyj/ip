package nekochan.model.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import nekochan.exceptions.NekoException;
import nekochan.exceptions.NekoTaskCreationException;
import nekochan.util.Messages;

public class TaskTest {

    @Test
    public void constructor_success() {
        ConcreteTaskStub task = new ConcreteTaskStub("Description");
        assertEquals("[\u2718] Description", task.toString());
    }

    @Test
    public void constructor_missingDescription_throwsException() {
        NekoException thrown = assertThrows(NekoTaskCreationException.class, () -> {
            ConcreteTaskStub task = new ConcreteTaskStub("");
        });
        assertTrue(thrown.getMessage().contains(Messages.PARSE_TASK_DESCRIPTION_ERROR));
    }

    @Test
    public void setCompleted_success() {
        ConcreteTaskStub task = new ConcreteTaskStub("Description");
        ConcreteTaskStub completedTask = task.setCompleted();
        assertEquals("[\u2713] Description", completedTask.toString());
    }
}
