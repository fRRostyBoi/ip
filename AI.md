# The Vibecode <sup>(not really)</sup> Logbook

A documentation of the AI-assisted code enhancements used in this project.

## Week 6

### 1. A-AiAssisted IP Increment

```text
// Claude Sonnet 4.5 Prompt:
This is an SWE school project focusing on application of proper SWE principles and conventions.
Find enhancements to the code (e.g. abstractions of common logic) which improve adherence to SWE principles, including but not limited to:
- KISS (Keep it simple, stupid)
- SLAP (Single layer of abstraction principle
- Avoid arrowhead programming (guideline not more than 3 indentations)
- Prefer ease of readability over optimisation (e.g. in multi-clause boolean conditional)
```

General Changes:
- Cleaned several multi-clause boolean conditionals for better readability
- Abstracted out common logic into utility methods or classes
- Modified method names for better clarity

Some Changes Include:
1. Task Classes (ToDo, Event, Deadline):
    - Extracted out individual subtype-factory methods into 1 utility factory method using generics + functional interface [Abstraction]
2. DialogBox:
    - Extracted out shared FXML loading behaviour into 1 method [SLAP]
3. I/O (NoteStorage, TaskStorage):
    - Extracted out shared string parser into utility class using functional interface [Abstraction]
4. DoneCmd, UndoCmd, ToggleCmd, DeleteCmd, NoteCmd:
    - Extracted shared task index validation [Abstraction]
5. DeadlineCmd, EventCmd:
    - Extracted out shared flag validator into utility method [Abstraction]
    - Extracted out shared date parsers into utility method [Abstraction]