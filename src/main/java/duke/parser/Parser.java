package duke.parser;

import duke.command.AddCommand;
import duke.command.Command;
import duke.command.CompleteCommand;
import duke.command.DeleteAllCommand;
import duke.command.DeleteCommand;
import duke.command.ExitCommand;
import duke.command.ListCommand;
import duke.command.SearchCommand;
import duke.exceptions.ParseDukeCommandException;
import duke.task.TaskType;

/**
 * The {@code Parser} class provides methods for parsing user input.
 */
public class Parser {

    /**
     * An enum containing keywords used by the user for program control.
     */
    enum Keyword {
        EXIT("exit"),
        LIST("list"),
        COMPLETE("complete"),
        TODO("todo"),
        DEADLINE("deadline"),
        EVENT("event"),
        DELETE("delete"),
        SEARCH("search");

        private final String input;

        Keyword(String keyword) {
            input = keyword;
        }

        /**
         * Finds and returns the {@code Keyword} matching the specified input.
         *
         * @param keyword the input to match against.
         * @return the {@code keyword} corresponding to the specified input.
         * @throws ParseDukeCommandException if there is no matching {@code Keyword}.
         */
        public static Keyword findKeyword(String keyword) throws ParseDukeCommandException {
            for (Keyword k : values()) {
                if (keyword.toLowerCase().equals(k.input)) {
                    return k;
                }
            }
            throw new ParseDukeCommandException("Wakarimasen~");
        }
    }

    /**
     * Parses the specified user input and returns its corresponding {@link Command}.
     *
     * @param fullCommand the user input to be parsed.
     * @return a {@code Command} object to be executed.
     * @throws ParseDukeCommandException if the user input is not recognised.
     */
    public static Command parse(String fullCommand) throws ParseDukeCommandException {
        String[] inputs = fullCommand.split(" ", 2);
        // By spec, inputs is guaranteed to have at least one element.
        String userInput = inputs[0];
        Keyword keyword = Keyword.findKeyword(userInput);
        switch (keyword) {
        case EXIT:
            return new ExitCommand();
        case LIST:
            return new ListCommand();
        case COMPLETE:
            try {
                int index = Integer.parseInt(fullCommand.split(" ")[1]) - 1;
                return new CompleteCommand(index);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("What did you complete exaclty?");
            } catch (NumberFormatException e) {
                throw new ParseDukeCommandException("This isn't harry potter, please use only integers.");
            }
        case DELETE:
            try {
                if (fullCommand.toLowerCase().contains("all")) {
                    return new DeleteAllCommand();
                } else {
                    int index = Integer.parseInt(fullCommand.split(" ")[1]) - 1;
                    return new DeleteCommand(index);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("What do you want to remove exactly?");
            } catch (NumberFormatException e) {
                throw new ParseDukeCommandException("This isn't harry potter, please use only integers.");
            }
        case DEADLINE:
            try {
                return new AddCommand(TaskType.DEADLINE, inputs[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("What are you rushing for? To wait?");
            }
        case TODO:
            try {
                return new AddCommand(TaskType.TODO, inputs[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("I know your life is empty but your todo can't be empty.");
            }
        case EVENT:
            try {
                return new AddCommand(TaskType.EVENT, inputs[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("Are you going to attend a nameless event?");
            }
        case SEARCH:
            try {
                return new SearchCommand(inputs[1]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new ParseDukeCommandException("I don't have the answer to every or anything");
            }
        default:
            throw new ParseDukeCommandException("Wakarimasen~");
        }
    }
}
