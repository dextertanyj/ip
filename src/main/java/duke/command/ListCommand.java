package duke.command;

import duke.exceptions.DukeException;
import duke.Storage;
import duke.Ui;
import duke.exceptions.IncompleteDukeCommandException;
import duke.task.TaskList;

public class ListCommand extends Command {

    private String printout;

    public void execute(TaskList list, Storage storage) {
        this.printout = list.listItems();
        super.completed = true;
    }

    public void printFeedback(Ui ui) throws DukeException {
        if (super.completed) {
            ui.formattedPrint(ui.prependIndent(this.printout, 1));
        } else {
            throw new IncompleteDukeCommandException("List command was not completed.");
        }
    }

    public boolean isExit() {
        return false;
    }

}
