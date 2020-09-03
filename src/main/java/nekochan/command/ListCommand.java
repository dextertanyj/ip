package nekochan.command;

import java.util.ArrayList;
import java.util.List;

import nekochan.exceptions.IncompleteNekoCommandException;
import nekochan.storage.Storage;
import nekochan.task.Task;
import nekochan.task.TaskList;

/**
 * The {@code ListCommand} class represents a command to print all contents of a {@link TaskList}.
 * The output of the {@code ListCommand} retains the same order of tasks in the {@code TaskList}.
 */
public class ListCommand extends Command {

    private List<Task> existingTasks;

    /**
     * Executes this {@code ListCommand}.
     *
     * @param list    the currently loaded {@link TaskList} object.
     * @param storage the currently loaded {@link Storage} object.
     */
    @Override
    public void execute(TaskList list, Storage storage) {
        existingTasks = new ArrayList<>();
        for (Task task : list) {
            existingTasks.add(task);
        }
        super.isCompleted = true;
    }

    /**
     * Prints the result of executing this {@code ListCommand}.
     *
     * @throws IncompleteNekoCommandException if this {@code ListCommand} was not executed.
     */
    @Override
    public String feedback() throws IncompleteNekoCommandException {
        if (!super.isCompleted) {
            throw new IncompleteNekoCommandException("List command was not completed.");
        }
        String printout = "";
        if (existingTasks.size() == 0) {
            printout = "Congratulations! You don't have any tasks left to do.";
        } else {
            printout = "Here are the tasks in your list:\n";
            for (int i = 0; i < existingTasks.size(); i++) {
                printout += String.format("%d.%s\n", i + 1, existingTasks.get(i).toString());
            }
        }
        return printout;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return false;
    }

}