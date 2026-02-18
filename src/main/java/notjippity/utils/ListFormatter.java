package notjippity.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import notjippity.notes.Note;
import notjippity.tasks.Task;

/**
 * Utility class for formatting lists of tasks and notes.
 */
public class ListFormatter {

    /**
     * Formats a map of tasks with aligned indices.
     *
     * @param tasks     The map of indices to tasks.
     * @param headerMsg The header message.
     * @return A list of formatted strings.
     */
    public static List<String> formatTaskMap(HashMap<Integer, Task> tasks, String headerMsg) {
        List<String> messages = new ArrayList<>();
        messages.add(headerMsg);

        int maxIndex = getMaxIndex(tasks);
        int maxDigits = calculateDigits(maxIndex);

        for (int index : tasks.keySet()) {
            Task task = tasks.get(index);
            String formattedLine = formatLine(index, task.toString(), maxDigits);
            messages.add(formattedLine);
        }

        return messages;
    }

    /**
     * Formats a list of notes with aligned indices.
     *
     * @param notes     The list of notes.
     * @param headerMsg The header message.
     * @return A list of formatted strings.
     */
    public static List<String> formatNoteList(List<Note> notes, String headerMsg) {
        List<String> messages = new ArrayList<>();
        messages.add(headerMsg);

        int maxIndex = notes.size();
        int maxDigits = calculateDigits(maxIndex);

        for (int i = 0; i < notes.size(); i++) {
            int displayIndex = i + 1;
            Note note = notes.get(i);
            String formattedLine = formatLine(displayIndex, note.toString(), maxDigits);
            messages.add(formattedLine);
        }

        return messages;
    }

    private static int getMaxIndex(HashMap<Integer, Task> tasks) {
        int max = -1;
        for (int index : tasks.keySet()) {
            if (index > max) {
                max = index;
            }
        }
        return max;
    }

    private static int calculateDigits(int number) {
        if (number <= 0) {
            return 1;
        }
        return 1 + (int) Math.floor(Math.log10(number));
    }

    private static String formatLine(int index, String content, int maxDigits) {
        int currentDigits = calculateDigits(index);
        int padding = maxDigits - currentDigits;

        StringBuilder builder = new StringBuilder(index + ". ");
        for (int i = 0; i < padding; i++) {
            builder.append(" ");
        }
        builder.append(content);

        return builder.toString();
    }
}

