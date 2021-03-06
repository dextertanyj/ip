package nekochan.command;

import nekochan.exceptions.IncompleteNekoCommandException;
import nekochan.model.NekoHistory;
import nekochan.model.task.Task;
import nekochan.model.task.TaskList;
import nekochan.storage.Storage;
import nekochan.util.Messages;

/**
 * The {@code CompleteCommand} class represents a command to mark a {@link Task} in a {@link TaskList} as complete.
 */
public class CompleteCommand extends Command {

    private static final boolean IS_EXIT = false;

    private int index;
    private Task completedTask;

    /**
     * Constructs a {@code CompleteCommand} with the specified {@code index}.
     *
     * @param index the index of the {@code Task} to mark as complete.
     */
    public CompleteCommand(int index) {
        this.index = index;
    }

    /**
     * Executes this {@code CompleteCommand}.
     * Marks the {@code Task} in the specified {@code list} at the stored {@code index} as complete.
     * @param history the currently loaded {@link NekoHistory} object.
     * @param storage the currently loaded {@link Storage} object.
     */
    @Override
    public void execute(NekoHistory history, Storage storage) {
        completedTask = history.markAsComplete(index);
        history.save(storage);
        super.isCompleted = true;
    }

    /**
     * Returns a {@link Response} from the execution of this {@code CompleteCommand}.
     *
     * @return @return a {@code Response} object containing the result of executing this {@code CompleteCommand}.
     * @throws IncompleteNekoCommandException if this {@code CompleteCommand} was not executed.
     */
    @Override
    public Response getResponse() throws IncompleteNekoCommandException {
        if (!super.isCompleted) {
            throw new IncompleteNekoCommandException(Messages.INCOMPLETE_COMPLETE_COMMAND);
        }

        assert completedTask != null : "completed task should not be null";

        String responseMessage = Messages.MESSAGE_COMPLETE + completedTask.toString() + "\n";
        return new Response(IS_EXIT, responseMessage);
    }
}
