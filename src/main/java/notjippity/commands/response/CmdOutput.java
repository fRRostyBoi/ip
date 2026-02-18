package notjippity.commands.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the output of a command execution
 */
public class CmdOutput {

    private final boolean isError;
    private final List<String> reply;

    /**
     * Returns a new CmdOutput instance.
     *
     * @param isError Whether the output is an error message.
     * @param reply   The list of strings representing the bot's reply.
     */
    public CmdOutput(boolean isError, List<String> reply) {
        this.isError = isError;
        this.reply = reply;
    }

    /**
     * Returns whether the output is an error message.
     *
     * @return True if the output is an error message, false otherwise.
     */
    public boolean isError() {
        return isError;
    }

    /**
     * Returns a cloned list of strings representing the bot's reply.
     *
     * @return The cloned list of strings representing the bot's reply.
     */
    public List<String> getReply() {
        return new ArrayList<>(reply);
    }

}
