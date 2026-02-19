# The Vibecode <sup>(not really)</sup> Logbook

A documentation of the AI-assisted code enhancements used in this project. All AI-generated code changes are manually reviewed and modified where needed before accepted.

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

### 2. A-MoreTesting IP Increment

Used tab autocomplete on suggestions from IntelliJ to autofill similar test case logic and method patterns, specifically on the following:

Within each method, a common test pattern is the sequence of flag checks for:
- Spaces before, after and/or between arguments, then
- Missing/re-ordered arguments, then
- Null flags.

Tab autocomplete saves a lot of time from copy-pasting from previous test methods and changing it for the current test method. 

For methods, the test methodName_validCase_expectedReturn is written first, followed by methodName_invalidCase_expectedException.<br>
Within each test method, some test suggestions are also taken, but a significant portion is also added manually.<br>
Additionally, the validCase naming is widely taken from autocomplete suggestions as it generates more apt names than what I would come up with.

### 3. General Cleanup before submission

```text
// Claude Sonnet 4.5 Prompt:
This is an SWE school project focused on following good SWE principles and conventions.
Analyse the entire codebase's comments and header comments only, inferring the SWE principles from existing code.
Clean up typos and grammar mistakes, add missing header comments and improve sentence structure to enhance readability.
```

General Changes:
- Corrected some minor grammar errors and typos, added some missing header comments
- Reworded a number of header comments for better clarity 