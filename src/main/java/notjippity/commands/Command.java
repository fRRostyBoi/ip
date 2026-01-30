package notjippity.commands;

import notjippity.exceptions.NJException;

/**
 * Handles behaviour and implementation for recognised NotJippity commands
 */
public abstract class Command {

    protected String cmdName;

    /**
     * Instantiates and returns a new Command instance. Must only be
     * called by Command subtypes through super()
     * @param cmdName The string to trigger this command
     */
    protected Command(String cmdName) {
        this.cmdName = cmdName;
    }

    /**
     * Executes the command logic with the given inputs
     * @param cmdStr The command string
     * @param argStr The string of arguments
     * @throws NJException If any execution error occurs
     */
    public abstract void execute(String cmdStr, String argStr) throws NJException;

    public String getCmdName() {
        return this.cmdName;
    }

}
