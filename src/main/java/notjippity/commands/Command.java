package notjippity.commands;

import notjippity.exceptions.NJException;

/**
 * Handles behaviour and implementation for recognised NotJippity commands
 */
public abstract class Command {

    protected String name;

    /**
     * Returns a new Command instance
     *
     * @param name The string to trigger this command
     */
    protected Command(String name) {
        this.name = name;
    }

    /**
     * Executes the command logic with the given inputs
     *
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @throws NJException If any execution error occurs
     */
    public abstract void execute(String cmdStr, String argStr) throws NJException;

    /**
     * Returns the string that triggers this Command
     *
     * @return The command name
     */
    public String getName() {
        return this.name;
    }

}
