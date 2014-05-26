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
}