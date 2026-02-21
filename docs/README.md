# NotJippity User Guide

![Screenshot of NotJippity GUI](Ui.png)

NotJippity is a lightweight and powerful task manager app to help users stay organised and productive. With a unique messenger app-like interface, NotJippity allows users to easily create, manage, and track their tasks, events and deadlines. NotJippity is specially designed for fast typists, enabling them to quickly input and manage their tasks without the need for mouse interactions.

# Commands
`<>` represents a required argument following, while `[]` represents an optional argument.<br>
Commands are case-insensitive, but arguments must be in order.

## 1. ToDo

Adds a todo task to the list of tasks with the given name.

Syntax: `todo <Task>`

Example: `todo Buy groceries for next week`<br>
Expected Outcome:
```
A new todo task "Buy groceries for next week" is added to the task list, and a confirmation message is displayed.
```

## 2. Deadline

Adds a deadline task to the list of tasks with the given name and due date.

Syntax: `deadline <Task> --by <dd/MM/yyyy HHmm>`

Example: `deadline Submit assignment --by 30/06/2024 2359`<br>
Expected Outcome:
```
A new deadline task "Submit assignment" due 30/6/24 2359h is added to the tasklist, and a confirmation message is displayed.
```

## 3. Event

Adds an event task to the list of tasks with the given name and event date.

Syntax: `event <Task> --from <dd/MM/yyyy HHmm> --to <dd/MM/yyyy HHmm>`

Example: `event Team meeting --from 1/7/24 1300 --to 1/7/24 1500`<br>
Expected Outcome:
```
A new event task "Team meeting" starting 1/7/25 1300h and ending 1/7/25 1500h is added to the task list, and a confirmation message is displayed.
```

## 4. List

Without Flags: Displays the list of all tasks, including their names, types, and statuses.

Syntax: `list`

Example: `list`<br>
Expected Outcome:
```
Displays list of all tasks, showing their names, types, and completion statuses.
If the task list is empty, a message indicating that there are no tasks is displayed.
```

<br>
With `--date` Flag: Displays the list of tasks that are due on a specific date, including their names, types, and statuses.

Syntax: `list --date <dd/MM/yyyy>`

Example: `list --date 30/06/2024`<br>
Expected Outcome:
```
The user is presented with a list of all tasks that are associated with 30/6/24, showing their names, types, and completion status.
If no tasks are associated with that date, a message indicating that there are no tasks associated with that date is displayed.
```

## 5. Done

Marks a task as completed based on its index in the list.

Syntax: `done <TaskIndex>`

Example: `done 2`<br>
Expected Outcome:
```
The task at index 2 of the list is marked as completed, and a confirmation message is displayed.
If task index 2 does not exist, an error message is displayed indicating that the task does not exist.
```

## 6. Undo

Marks a task as incomplete based on its index in the list.

Syntax: `undo <TaskIndex>`

Example: `undo 2`<br>
Expected Outcome:
```
The task at index 2 in the list is marked as incomplete, and a confirmation message is displayed.
If task index 2 does not exist, an error message is displayed indicating that the task does not exist.
```

## 7. Toggle

Toggles the completion status of a task based on its index in the list.

Syntax: `toggle <TaskIndex>`

Example: `toggle 2`<br>
Expected Outcome:
```
The completion status of the task at index 2 of the task list is toggled (if it was completed, it becomes incomplete, and vice versa), and a confirmation message is displayed.
If task index 2 does not exist, an error message is displayed indicating that the task does not exist.
```

## 8. Delete

Deletes a task from the list based on its index.

Syntax: `delete <TaskIndex>`

Example: `delete 2`<br>
Expected Outcome:
```
The task at index 2 of the task list is deleted, and a confirmation message is displayed.
If task index 2 does not exist, an error message is displayed indicating that the task does not exist.
```

## 9. Find

Searches for tasks that contain the specified keyword(s) in their names. Case-insensitive.

Syntax: `find <Keywords>`

Example: `find meeT JoHn`<br>
Expected Outcome:
```
Displays the filtered task list of tasks containing the case-insensitive sequence "meeT JoHn" in their names.
If no tasks match the search criteria, a message indicating that no matching tasks were found is displayed.
```

## 10. Notes

Without Flags: Displays all notes in the notes list.

Syntax: `notes`

Example: `notes`<br>
Expected Outcome:
```
Displays the note list. If the note list is empty, a message indicating that there are no notes is displayed.
```

<br>
With `Add` Flag: Adds a note to the notes list.

Syntax: `notes add <Note>`

Example: `notes add Remember to bring the report`<br>
Expected Outcome:
```
A new note "Remember to bring the report" is added to the note list, and a confirmation message is displayed.
```

<br>
With `Delete` Flag: Deletes a note from the notes list.

Syntax: `notes delete <NoteIndex>`

Example: `notes delete 1`<br>
Expected Outcome:
```
The note at index 1 of the notes list is deleted, and a confirmation message is displayed.
If notes list index 1 does not exist, an error message is displayed indicating that the note does not exist.
```

## 11. Bye

Saves all changes and exits the NotJippity application.

Syntax: `bye`

Example: `bye`<br>
Expected Outcome:
```
The NotJippity application is closed, and the user is returned to their desktop or command line interface.
The files data.txt and notes.txt are updated with the latest changes to the tasks and notes.
```