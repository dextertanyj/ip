package duke.command;

import duke.exceptions.IncompleteDukeCommandException;
import duke.storage.Storage;
import duke.task.TaskList;

/**
 * The {@code DeleteAllCommand} class represents a command to remove all entries in a {@link TaskList}.
 */
public class DeleteAllCommand extends Command {

    /**
     * Executes this {@code DeleteAllCommand}.
     * Deletes all contents in the specified {@code list}.
     *
     * @param list    the currently loaded {@link TaskList} object.
     * @param storage the currently loaded {@link Storage} object.
     */
    @Override
    public void execute(TaskList list, Storage storage) {
        list.clearList();
        storage.save(list);
        super.completed = true;
    }

    /**
     * Prints a feedback confirming the execution of this {@code DeleteAllCommand}.
     *
     * @throws IncompleteDukeCommandException if this {@code DeleteAllCommand} was not executed.
     */
    @Override
    public String feedback() throws IncompleteDukeCommandException {
        if (super.completed) {
            return "I've cleared all your tasks.\nYou sure are efficient.";
        } else {
            throw new IncompleteDukeCommandException("Delete all command was not completed.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExit() {
        return false;
    }
}
