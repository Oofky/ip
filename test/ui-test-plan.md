# UI Test Plan

## Test environment

- Java version: 25
- Compile command: `javac -d _temp/ui-test-classes src/main/java/*.java`
- Run command: `java -cp _temp/ui-test-classes Bogos`

Run the planned sessions from the repository root with:

```powershell
python .codex/skills/test-ui/scripts/run_ui_tests.py
```

## Test case: Add and list task types

### Aim

Verify that to-dos, deadlines, and events are added with their type-specific details and shown in the list.

### Inputs

```text
todo borrow book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
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
           [D][ ] return book (by: Sunday)
         2 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Boom! Bullet born: 
           [E][ ] project meeting (from: Mon 2pm to: 4pm)
         3 bullet(s) being.
____________________________________________________________
____________________________________________________________
         Behold bullets:
         1.[T][ ] borrow book
         2.[D][ ] return book (by: Sunday)
         3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
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