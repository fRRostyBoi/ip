package notjippity.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;

import notjippity.notes.Note;
import notjippity.tasks.Task;
import notjippity.tasks.ToDo;

/**
 * Contains JUnit tests for the ListFormatter class.
 */
public class ListFormatterTest {

    /**
     * Tests that formatTaskMap returns correct format with header.
     */
    @Test
    public void formatTaskMap_singleTask_correctFormat() {
        HashMap<Integer, Task> tasks = new HashMap<>();
        tasks.put(1, new ToDo("Test task"));

        List<String> result = ListFormatter.formatTaskMap(tasks, "Header:");

        assertEquals(2, result.size());
        assertEquals("Header:", result.get(0));
        assertTrue(result.get(1).contains("1."));
        assertTrue(result.get(1).contains("Test task"));
    }

    /**
     * Tests that formatTaskMap handles index alignment.
     */
    @Test
    public void formatTaskMap_multipleTasksUnevenDigits_correctAlignment() {
        HashMap<Integer, Task> tasks = new HashMap<>();
        tasks.put(1, new ToDo("Task 1"));
        tasks.put(2, new ToDo("Task 2"));
        tasks.put(10, new ToDo("Task 10"));

        List<String> result = ListFormatter.formatTaskMap(tasks, "Tasks:");

        assertEquals(4, result.size());
        assertEquals("Tasks:", result.get(0));
        assertTrue(result.get(1).startsWith("1. "));
        assertTrue(result.get(2).startsWith("2. "));
        assertTrue(result.get(3).startsWith("10."));
    }

    /**
     * Tests that formatTaskMap handles empty task map.
     */
    @Test
    public void formatTaskMap_emptyMap_returnsOnlyHeader() {
        List<String> result = ListFormatter.formatTaskMap(new HashMap<>(), "Empty, nothing to see here");

        assertEquals(1, result.size());
        assertEquals("Empty, nothing to see here", result.get(0));
    }

    /**
     * Tests that formatNoteList returns correct format with header.
     */
    @Test
    public void formatNoteList_singleNote_correctFormat() {
        List<Note> notes = List.of(new Note(LocalDate.of(2026, 2, 18), "Test note"));

        List<String> result = ListFormatter.formatNoteList(notes, "Notes:");

        assertEquals(2, result.size());
        assertEquals("Notes:", result.get(0));
        assertTrue(result.get(1).contains("1."));
        assertTrue(result.get(1).contains("Test note"));
    }

    /**
     * Tests that formatNoteList handles multiple notes.
     */
    @Test
    public void formatNoteList_multipleNotes_correctFormat() {
        List<Note> notes = List.of(
                new Note(LocalDate.of(2026, 2, 18), "Note 1"),
                new Note(LocalDate.of(2026, 2, 18), "Note 2"),
                new Note(LocalDate.of(2026, 2, 18), "Note 3")
        );

        List<String> result = ListFormatter.formatNoteList(notes, "All notes:");

        assertEquals(4, result.size());
        assertEquals("All notes:", result.get(0));
    }

    /**
     * Tests that formatNoteList handles empty note list.
     */
    @Test
    public void formatNoteList_emptyList_returnsOnlyHeader() {
        List<String> result = ListFormatter.formatNoteList(List.of(), "Empty, nothing to see here");

        assertEquals(1, result.size());
        assertEquals("Empty, nothing to see here", result.get(0));
    }

}
