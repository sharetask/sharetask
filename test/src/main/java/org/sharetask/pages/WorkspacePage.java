package org.sharetask.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/sharetask/signin")
public class WorkspacePage extends PageObject {

	@FindBy(xpath = "//div[@class=\"header\"]/div[@class=\"queue-name\"]/div[@class=\"queue-name-inner\"]/div")
	private WebElementFacade message;

    @WhenPageOpens
    public void waitUntilUserNameAppears() {
    	element(message).waitUntilVisible();
    }
	
    public void checkMessage(final String message) {
    	assertThat("Must contains text", this.message.getTextValue(), containsString(message));
    }
}