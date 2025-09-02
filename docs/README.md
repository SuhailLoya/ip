# Lumina the Task Manager

### Sneak Peek at Lumina
![lumina preview](Ui.png)

Meet Lumina: your personal task management assistant. Free your mind from all the organizing, and focus on accomplishing what truly matters.

## Key Features

### 1. Listing All Tasks

You can list all tasks by using the command:

- **Command Format:** `list`

This will display all the tasks that you currently have in this format

```
[type][status] <description> (additional details)
```
- **type:** `[T]` for Todos, `[D]` for Deadlines, `[E]` for Events.
- **status:** `[X]` for completed tasks, `[]` for incomplete ones

#### Example Output:
```
[T][] personalize ip
[D][X] submit cs2103t ip (by: Sep 25 2024)
[E][] ip showcase (from: Sep 27 2024 to: Oct 2 2024)
[D][] submit cs2100 lab (by: Sep 27 2024)
```

### 2. Adding a Deadline Task

You can add a deadline task by following this format:

- **Command Format:** `deadline <description> /by <date>`
- **Date Format:** `YYYY-MM-DD` (e.g., 2024-04-03)

Be sure to include `/by` to indicate the deadline date.

#### Example Usage:
```
deadline submit cs2103t ip /by 2024-09-25
```
#### Example Output:
```
[D][] submit cs2103t ip (by: Sep 25 2024)
```

### 3. Adding an Event Task

You can add an event task by following this format:

- **Command Format:** `event <description> /from <date> /to <date>`
- **Date Format:** `YYYY-MM-DD` (e.g., 2024-04-03)

Be sure to include `/from` and `/to` to indicate the start date and end date.

#### Example Usage:
```
event ip showcase /from 2024-09-27 /to 2024-10-02
```
#### Example Output:
```
[E][] ip showcase (from: Sep 27 2024 to: Oct 2 2024)
```

### 4. Adding a Todo Task

You can add a Todo task by following this format:

- **Command Format:** `todo <description>`

#### Example Usage:
```
todo personalize ip
```
#### Example Output:
```
[T][] personalize ip
```

### 5. Marking Task as Done

You can mark a task as done, using the following command:

- **Command Format:** `mark <task index>`

This will update the task at the specified index to a "done" status.

#### Example Usage:
```
mark 2
```
#### Example Output:
```
[D][X] submit cs2103t ip (by: Sep 25 2024)
```

### 6. Unmarking a Task

You can unmark tasks (mark it as not done), using the following command:

- **Command Format:** `unmark <task index>`

This will update the task at the specified index to a "not done" status.

#### Example Usage:
```
unmark 2
```
#### Example Output:
```
[D][] submit cs2103t ip (by: Sep 25 2024)
```

### 7. Finding Tasks

You can find all tasks that contain a specific keyword by using the following command:

- **Command Format:** `find <keyword>`

This will display all tasks whose descriptions contain the keyword.

#### Example Usage:
```
find submit
```
#### Example Output:
```
[D][] submit cs2103t ip (by: Sep 25 2024)
[D][] submit cs2100 lab (by: Sep 27 2024)
```

### 8. Reminders

You can get reminders for all upcoming tasks by using the follwing command

- **Command Format:** `remind

Upcoming tasks are deadline tasks which are due after today or event tasks which start after today.

#### Example Usage:
```
remind
```
#### Example Output:
```
[D][] submit cs2103t ip (by: Sep 25 2024)
[E][] ip showcase (from: Sep 27 2024 to: Oct 2 2024)
[D][] submit cs2100 lab (by: Sep 27 2024)
```

### 9. Delete a Task

You can delete a task, using the following command:

- **Command Format:** `delete <task index>`

This will delete the task at the specified index.

#### Example Usage:
```
delete 1
```
#### Example Output:
```
deletes [T][] personalize ip
```

### 10. Exiting

You can exit Lumina by using the following command:

- **Command Format:** `bye`

This will save the current state of tasks, which will be loaded the next time Lumina starts up again :)

#### Example Usage:
```
bye
```
#### Example Output:
```
Bye. Hope to see you again soon!
```
