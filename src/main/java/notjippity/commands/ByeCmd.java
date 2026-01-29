package notjippity.commands;

import notjippity.NotJippity;
import notjippity.exceptions.NJException;

/**
 * Handles "bye" command logic and behaviour
 */
public class ByeCmd extends Command {

    private NotJippity notJippity;

    /**
     * Returns a new instance of ByeCmd
     * @param notJippity The NotJippity instance
     */
    public ByeCmd(NotJippity notJippity) {
        super("bye");
        this.notJippity = notJippity;
    }

    /**
     * Triggers the shutdown sequence of the bot
     * @param cmdStr The command string
     * @param argStr The string of arguments
     */
    @Override
    public void execute(String cmdStr, String argStr) throws NJException {
        notJippity.stopMainLoop();
    }

}
