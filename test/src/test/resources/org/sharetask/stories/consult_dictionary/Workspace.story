Narrative:
xxx
 
Lifecycle:
Before:
Given Login user support@shareta.sk with password password
After:
Given Logout user

Scenario: Add Task
Given Open add task dialog
Given Fill task name: New Task
When Pushing add task button
Then Check if new task with name: New Task exists

Scenario: Add Comment
Given Open add task dialog
Given Fill task name: New Task
When Pushing add task button
Then Check if new task with name: New Task exists
Given Fill comment: My personal comment
When Pushing add comment button
Then Check if new comment with text: My personal comment exists
