package notjippity.commands;

import java.util.List;

import notjippity.NotJippity;
import notjippity.exceptions.NjException;

/**
 * Handles "bye" command logic and behaviour
 */
public class ByeCmd extends Command {

    private NotJippity notJippity;

    /**
     * Returns a new instance of ByeCmd
     *
     * @param notJippity The NotJippity instance
     */
    public ByeCmd(NotJippity notJippity) {
        super("bye");
        this.notJippity = notJippity;
    }

    /**
     * Triggers the shutdown sequence of the bot
     *
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @return The bot's response
     * @throws NjException If any execution error occurs
     */
    @Override
    public List<String> execute(String cmdStr, String argStr) throws NjException {
        notJippity.shutdown();
        return List.of();
    }

}
