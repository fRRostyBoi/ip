package notjippity.commands;

import notjippity.commands.response.CmdOutput;
import notjippity.exceptions.NjException;

/**
 * Handles behaviour and implementation for recognised NotJippity commands.
 */
public abstract class Command {

    protected String cmdName;

    /**
     * Returns a new Command instance.
     *
     * @param cmdName The string to trigger this command.
     */
    protected Command(String cmdName) {
        this.cmdName = cmdName;
        boolean isValidCmdName = this.cmdName != null && !this.cmdName.isBlank();
        assert isValidCmdName;
    }

    /**
     * Executes the command logic with the given inputs.
     *
     * @param cmdStr The command string.
     * @param argStr The string of arguments.
     * @return The bot's response.
     * @throws NjException If any execution error occurs.
     */
    public abstract CmdOutput execute(String cmdStr, String argStr) throws NjException;

    /**
     * Returns the string that triggers this Command.
     *
     * @return The command name.
     */
    public String getCmdName() {
        return this.cmdName;
    }

}
