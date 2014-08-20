package org.sharetask.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.By;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.pages.WebElementFacade;

import org.openqa.selenium.WebElement;

@DefaultUrl("http://localhost:8080/sharetask/webapp#/tasks")
public class WorkspacePage extends PageObject {

	@FindBy(xpath = "//div[@class=\"header\"]/div[@class=\"queue-name\"]/div[@class=\"queue-name-inner\"]/div")
	private WebElementFacade message;

	@FindBy(xpath = "//*[@id=\"dropUser\"]")
	private WebElementFacade userButton;
	
	@FindBy(xpath = "//a[@data-ng-click=\"logout()\"]")
	private WebElementFacade logoutButton;

	@FindBy(xpath = "//button[@ng-click=\"setEditMode('NEW-TASK')\"]")
	private WebElementFacade openAddTaskWindowButton;

	@FindBy(id = "inputTaskAdd")
	private WebElementFacade taskNameInput;
	
	@FindBy(xpath = "//button[@ng-click=\"addTask()\"]")
	private WebElementFacade submitTaskButton;

	@FindBy(id = "inputTaskAddComment")
	private WebElementFacade taskCommentInput;
	
	@FindBy(xpath = "//button[@ng-click=\"addTaskComment()\"]")
	private WebElementFacade submitCommentButton;
	
	@FindBy(xpath = "//div[@class=\"task-list-item ng-scope ui-droppable selected\"]/div/table/tbody/tr/td[3]/strong")
	private WebElementFacade selectedTask;

	@WhenPageOpens
    public void waitUntilUserNameAppears() {
    	element(message).waitUntilVisible();
    }
	
    public void checkMessage(final String message) {
    	assertThat("Must contains text", this.message.getTextValue(), containsString(message));
    }
    
    public void logout() {
    	userButton.click();
    	element(logoutButton).waitUntilVisible();
    	logoutButton.click();
    }
    
    public void open_add_task_dialog() {
    	openAddTaskWindowButton.click();
    }
    
    public void fill_new_task(final String taskName) {
    	taskNameInput.type(taskName);
    }
    
    public void submit_task() {
    	submitTaskButton.click();
    }

	public void check_if_task_exists(final String taskName) {
		assertThat("Must exists task with text", selectedTask.getTextValue(), equalTo(taskName));
	}
	
    public void fill_new_comment(final String comment) {
    	taskCommentInput.type(comment);
    }
    
    public void submit_comment() {
    	submitCommentButton.click();
    }

	public void check_if_comment_exists(final String comment) {
		final List<WebElement> comments = getDriver().findElements(By.xpath("//div[@id=\"comments\"]/table[@class=\"comment ng-scope\"]/tbody/tr/td/div[2]/small"));
		final List<String> commentStrings = new ArrayList<String>();
		for (final WebElement commentElement : comments) {
			commentStrings.add(commentElement.getText());
		}
		assertThat("Must exists comment with text", commentStrings, hasItem(comment));
	}
}