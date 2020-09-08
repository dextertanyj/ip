package nekochan.command;

import java.util.List;

import nekochan.exceptions.IncompleteNekoCommandException;
import nekochan.exceptions.NekoException;
import nekochan.exceptions.NekoSimilarTaskException;
import nekochan.model.NekoHistory;
import nekochan.model.task.Deadline;
import nekochan.model.task.Event;
import nekochan.model.task.Task;
import nekochan.model.task.TaskType;
import nekochan.model.task.ToDo;
import nekochan.storage.Storage;
import nekochan.util.Messages;

/**
 * The {@code CompleteCommand} class represents a command to create a new {@link Task}.
 */
public class AddCommand extends Command {

    private static final boolean IS_EXIT = false;

    private Task createdTask;
    private int remainingTaskCount;
    private List<Task> similarTasks;

    /**
     * Constructs an {@code AddCommand} with the specified type and specified task details.
     *
     * @param type           the type of {@code Task} to be added.
     * @param taskDetail     the details of the task to be added.
     * @throws NekoException if the specified type is null.
     */
    public AddCommand(TaskType type, String taskDetail) throws NekoException {
        switch (type) {
        case DEADLINE:
            createdTask = Deadline.createTask(taskDetail);
            break;
        case EVENT:
            createdTask = Event.createTask(taskDetail);
            break;
        case TODO:
            createdTask = ToDo.createTask(taskDetail);
            break;
        default:
            throw new NekoException(Messages.INVALID_TASK_TYPE_ERROR);
        }
    }

    /**
     * Executes this {@code AddCommand} by adding the created {@code Task} to the specified {@code list}.
     * @param history the currently loaded {@link NekoHistory} object.
     * @param storage the currently loaded {@link Storage} object.
     */
    @Override
    public void execute(NekoHistory history, Storage storage) {
        try {
            history.addTask(createdTask);
        } catch (NekoSimilarTaskException e) {
            similarTasks = e.getSimilarTask();
        } finally {
            remainingTaskCount = history.getCurrentTaskCount();
            history.save(storage);
            super.isCompleted = true;
        }
    }

    /**
     * Returns a {@link Response} from the execution of this {@code AddCommand}.
     *
     * @return a {@code Response} object containing the result of executing this {@code AddCommand}.
     * @throws IncompleteNekoCommandException if this {@code AddCommand} was not executed.
     */
    @Override
    public Response getResponse() throws IncompleteNekoCommandException {
        if (!super.isCompleted) {
            throw new IncompleteNekoCommandException(Messages.INCOMPLETE_ADD_COMMAND);
        }

        assert createdTask != null : "created task should not be null";

        String responseMessage = Messages.MESSAGE_ADD + createdTask.toString() + "\n"
                + Messages.getTotalTaskMessage(remainingTaskCount);;
        if (similarTasks != null && !similarTasks.isEmpty()) {
            responseMessage += "\n" + Messages.SIMILAR_TASK_ERROR
                    + similarTasks.stream().map(Task::toString).reduce("", (str1, str2) -> str1 + str2 + "\n");
        }
        return new Response(IS_EXIT, responseMessage);
    }
}
