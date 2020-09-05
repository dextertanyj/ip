package nekochan.command;

import java.util.ArrayList;
import java.util.List;

import nekochan.exceptions.IncompleteNekoCommandException;
import nekochan.storage.Storage;
import nekochan.task.Task;
import nekochan.task.TaskList;
import nekochan.util.Messages;

/**
 * The {@code SearchCommand} class represents a command that allows the user to search for tasks.
 * The user is able to search for tasks either through their description or
 * their date (if the type of task supports it).
 */
public class SearchCommand extends Command {

    private static final boolean IS_EXIT = false;
    private String searchParameter;
    private List<Task> results;

    /**
     * Constructs a {@code SearchCommand} with the specified {@code searchParameter}.
     *
     * @param searchParameter the string for which to search for.
     */
    public SearchCommand(String searchParameter) {
        this.searchParameter = searchParameter;
        results = new ArrayList<>();
    }

    /**
     * Executes this {@code SearchCommand}.
     *
     * @param list    the currently loaded {@link TaskList} object.
     * @param storage the currently loaded {@link Storage} object.
     */
    @Override
    public void execute(TaskList list, Storage storage) {
        for (Task task : list) {
            if (task.match(searchParameter)) {
                results.add(task);
            }
        }
        super.isCompleted = true;
    }

    /**
     * Prints the result of executing this {@code SearchCommand}.
     *
     * @throws IncompleteNekoCommandException if this {@code DeleteCommand} was not executed.
     */
    @Override
    public Response feedback() throws IncompleteNekoCommandException {
        if (!super.isCompleted) {
            throw new IncompleteNekoCommandException(Messages.INCOMPLETE_SEARCH_COMMAND);
        }
        String responseMessage = "";
        for (Task result : results) {
            if (responseMessage.length() > 0) {
                responseMessage = responseMessage.concat("\n");
            }
            responseMessage = responseMessage.concat(result.toString());
        }
        return new Response(IS_EXIT, responseMessage);
    }
}
