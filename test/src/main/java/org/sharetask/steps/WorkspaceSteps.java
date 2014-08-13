package org.sharetask.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.sharetask.pages.WorkspacePage;

public class WorkspaceSteps extends ScenarioSteps {

    WorkspacePage workspacePage;
    
    @Step
    public void open_add_task_dialog() {
        workspacePage.open_add_task_dialog();
    }
    
    @Step
    public void fill_task_name(final String name) {
        workspacePage.fill_new_task(name);
    }

    @Step
    public void submit_task() {
        workspacePage.submit_task();
    }

    @Step
	public void check_if_task_exists(final String taskName) {
		workspacePage.check_if_task_exists(taskName);
	}
    
    @Step
    public void fill_comment(final String comment) {
        workspacePage.fill_new_comment(comment);
    }

    @Step
    public void submit_comment() {
        workspacePage.submit_comment();
    }

    @Step
	public void check_if_comment_exists(final String comment) {
		workspacePage.check_if_comment_exists(comment);
	}
    
}
