package org.sharetask.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/sharetask/webapp#/tasks")
public class WorkspacePage extends PageObject {

	@FindBy(xpath = "//div[@class=\"header\"]/div[@class=\"queue-name\"]/div[@class=\"queue-name-inner\"]/div")
	private WebElementFacade message;

	@FindBy(xpath = "//div/div[@class=\"navbar-inner\"]/div[@class=\"container\"]/ul/li/a[@id=\"dropUser\"]")
	private WebElementFacade userButton;
	
	@FindBy(xpath = "//div/div[@class=\"navbar-inner\"]/div[@class=\"container\"]/ul/li/ul/li/a[@ng-click=\"logout()\"]")
	private WebElementFacade logoutButton;

	@FindBy(xpath = "//button[@ng-click=\"setEditMode('NEW-TASK')\"]")
	private WebElementFacade openAddTaskWindowButton;

	@FindBy(xpath = "//button[@ng-click=\"addTask()\"]")
	private WebElementFacade submitTaskButton;

	@FindBy(id = "inputTaskAdd")
	private WebElementFacade taskName;
	
	@WhenPageOpens
    public void waitUntilUserNameAppears() {
    	element(message).waitUntilVisible();
    }
	
    public void checkMessage(final String message) {
    	assertThat("Must contains text", this.message.getTextValue(), containsString(message));
    }
    
    public void logout() {
    	userButton.click();
    	logoutButton.click();
    }
    
    public void open_add_task_dialog() {
    	openAddTaskWindowButton.click();
    }
    
    public void fill_new_task(final String taskName) {
    	this.taskName.type(taskName);
    }
    
    public void submit_task() {
    	submitTaskButton.click();
    }

	public void check_if_task_Eexists(final String taskName2) {
		// TODO Auto-generated method stub
	}
}