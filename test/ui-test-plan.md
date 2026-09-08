# UI Test Plan

## Test environment

- Java version: 25
- Compile command: `javac -d _temp/ui-test-classes src/main/java/bogos/*.java`
- Run command: `java -cp _temp/ui-test-classes bogos.Bogos`

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

Verify that to-dos, deadlines, and events are added with their type-specific details and shown in the list.

### Inputs

```text
todo borrow book
deadline return book /by 2026-09-05
event project meeting /from 2026-09-05 /to 2026-09-06
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
         Boom! Bullet born: 
           [T][ ] borrow book
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
         1.[T][ ] borrow book
         2.[D][ ] return book (by: Sep 05 2026)
         3.[E][ ] project meeting (from: Sep 05 2026 to: Sep 06 2026)
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
           [T][X] borrow book
____________________________________________________________
____________________________________________________________
         Bet! Bogos blanked box:
           [T][ ] borrow book
____________________________________________________________
____________________________________________________________
         Brilliant! Bye bye bullet:
           [D][ ] return book (by: Sep 05 2026)
         2 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Behold bulleted board:
         1.[T][ ] borrow book
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
