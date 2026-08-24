# UI Test Plan

## Test environment

- Java version: 25
- Compile command: `javac -d _temp/ui-test-classes src/main/java/Task.java src/main/java/Bogos.java`
- Run command: `java -cp _temp/ui-test-classes Bogos`

Run the planned sessions from the repository root with:

```powershell
python .codex/skills/test-ui/scripts/run_ui_tests.py
```

Each inputs block is a separate console session. Expected output is the complete standard output for that session; keep its visible spaces and line breaks exact.

## Test case: Exit immediately

### Aim

Verify that the application welcomes the user and exits cleanly when given the `bye` command.

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

## Test case: Add and list a bullet

### Aim

Verify that a new bullet is acknowledged and then appears as unfinished in the bullet list.

### Inputs

```text
read book
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
         bullet born: read book
____________________________________________________________
____________________________________________________________
         Behold bullets:
         1.[ ] read book
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Mark and unmark a bullet

### Aim

Verify that a bullet can be marked complete, marked incomplete again, and shown as incomplete in the list.

### Inputs

```text
write essay
mark 1
unmark 1
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
         bullet born: write essay
____________________________________________________________
____________________________________________________________
         Bravo! Bogos boxed bullet:
           [X] write essay
____________________________________________________________
____________________________________________________________
         Bet! Bogos blanked box:
           [ ] write essay
____________________________________________________________
____________________________________________________________
         Behold bullets:
         1.[ ] write essay
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Reject a non-numeric mark command

### Aim

Verify that a mark command with a non-numeric bullet number is rejected without ending the session.

### Inputs

```text
mark first
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
         Bogus. Bring Bogos base-ten. :[
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```

## Test case: Reject an out-of-range mark command

### Aim

Verify that a mark command for a bullet that does not exist is rejected.

### Inputs

```text
mark 1
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
         Bummer. Bullet beyond bounds. :[
____________________________________________________________
____________________________________________________________
         Bye bye! :]
____________________________________________________________
```
