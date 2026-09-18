# UI Test Plan

## Test environment

- Java version: 25
- Compile command: `javac -d _temp/ui-test-classes src/main/java/bogos/Bogos.java src/main/java/bogos/BogosException.java src/main/java/bogos/Deadline.java src/main/java/bogos/Event.java src/main/java/bogos/Parser.java src/main/java/bogos/Storage.java src/main/java/bogos/Task.java src/main/java/bogos/TaskList.java src/main/java/bogos/TaskType.java src/main/java/bogos/Todo.java src/main/java/bogos/Ui.java`
- Run command: `java -ea -cp _temp/ui-test-classes bogos.Bogos`

Run the planned sessions from the repository root with:

```powershell
python .codex/skills/test-ui/scripts/run_ui_tests.py
```

Before running the full plan, delete `data/bogos.txt` so the first session starts
with no saved tasks. The test cases run in the listed order because later sessions
verify tasks saved by earlier sessions.

## Test case: List an empty task list

### Aim

Verify that listing before any task is added gives the expected empty-list error.

### Inputs

```text
list
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         But board be blank...
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Add and list task types

### Aim

Verify that whitespace is normalized for task creation, all task types are added
with their type-specific details, and duplicate tasks are rejected.

### Inputs

```text
  todo   borrow   book   #reading   #Fun
deadline return book /by 2026-09-05
event project meeting /from 2026-09-05 /to 2026-09-06
list
find book
todo borrow book #Fun #reading
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         Boom! Bullet born: 
           [T][ ] borrow book #reading #Fun
         1 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Boom! Bullet born: 
           [D][ ] return book (by: Sep 05 2026)
         2 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Boom! Bullet born: 
           [E][ ] project meeting (from: Sep 05 2026 to: Sep 06 2026)
         3 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Behold bulleted board:
         1.[T][ ] borrow book #reading #Fun
         2.[D][ ] return book (by: Sep 05 2026)
         3.[E][ ] project meeting (from: Sep 05 2026 to: Sep 06 2026)
____________________________________________________________
____________________________________________________________
         Bogos brings befitting bullets:
         1.[T][ ] borrow book #reading #Fun
         2.[D][ ] return book (by: Sep 05 2026)
____________________________________________________________
____________________________________________________________
         bwhat buplicate bullet
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Reject invalid task tags

### Aim

Verify that blank, duplicate, and description-only tag commands are rejected without creating tasks.

### Inputs

```text
todo watch movie #
todo watch movie #fun #fun
todo #fun #weekend
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         bwhat tag
____________________________________________________________
____________________________________________________________
         bwhat buplicate tag
____________________________________________________________
____________________________________________________________
         bwhat body
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Mark, unmark, and delete tasks

### Aim

Verify that a task can be marked, unmarked, and deleted, and that the saved task list reflects each change.

### Inputs

```text
mark 1
unmark 1
delete 2
list
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         Bravo! Bogos boxed bullet:
           [T][X] borrow book #reading #Fun
____________________________________________________________
____________________________________________________________
         Bet! Bogos blanked box:
           [T][ ] borrow book #reading #Fun
____________________________________________________________
____________________________________________________________
         Brilliant! Bye bye bullet:
           [D][ ] return book (by: Sep 05 2026)
         2 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Behold bulleted board:
         1.[T][ ] borrow book #reading #Fun
         2.[E][ ] project meeting (from: Sep 05 2026 to: Sep 06 2026)
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Exit immediately

### Aim

Verify that the application exits cleanly when given the `bye` command.

### Inputs

```text
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Reject repeated date parameters

### Aim

Verify that commands with repeated date parameters or same-day event dates are
rejected without creating tasks.

### Inputs

```text
deadline submit report /by 2026-09-15 /by 2026-09-16
event project meeting /from 2026-09-15 /from 2026-09-16 /to 2026-09-17
event project meeting /from 2026-09-15 /to 2026-09-16 /to 2026-09-17
event same day /from 2026-09-17 /to 2026-09-17
bye
```

### Expected output

```text
      ___             __ _
     | _ )    ___    / _` |   ___     ___
     | _ \   / _ \   \__, |  / _ \   (_-<
     |___/   \___/   |___/   \___/   /__/_
   _|"""""|_|"""""|_|"""""|_|"""""|_|"""""|
   "`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'"`-0-0-'
____________________________________________________________
Blessings! Bogos beckons. Bring Bogos business? :]
____________________________________________________________
         bwhat buplicate /by
____________________________________________________________
____________________________________________________________
         bwhat buplicate /from
____________________________________________________________
____________________________________________________________
         bwhat buplicate /to
____________________________________________________________
____________________________________________________________
         Bro be breathing backwards??
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```
