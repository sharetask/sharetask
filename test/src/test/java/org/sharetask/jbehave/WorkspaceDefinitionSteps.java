package org.sharetask.jbehave;

import net.thucydides.core.annotations.Steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.sharetask.steps.WorkspaceSteps;

public class WorkspaceDefinitionSteps {

    @Steps
    WorkspaceSteps workspace;

    @Given("Open add task dialog")
    public void givenOpenAddTaskDIalog() {
    	workspace.open_add_task_dialog();
    }

    @Given("Fill task name: $taskName")
    public void givenFillTaskName(@Named("taskName") final String taskName) {
    	workspace.fill_task_name(taskName);
    }

    @When("Pushing add task button")
    public void whenPushingAddTaskButton() {
        workspace.submit_task();
    }

    @Then("Check if new task with name: $taskName exists")
    public void checkIfTaskExists(@Named("taskName") final String taskName) {
    	workspace.check_if_task_exists(taskName);
    }

    @Given("Fill comment: $comment")
    public void givenFillComment(@Named("comment") final String comment) {
    	workspace.fill_comment(comment);
    }

    @When("Pushing add comment button")
    public void whenPushingAddCommentButton() {
        workspace.submit_comment();
    }

    @Then("Check if new comment with text: $comment exists")
    public void checkIfCommentExists(@Named("comment") final String comment) {
    	workspace.check_if_comment_exists(comment);
    }
}
