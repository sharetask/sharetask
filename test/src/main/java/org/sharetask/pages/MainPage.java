package org.sharetask.pages;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/sharetask")
public class MainPage extends PageObject {

	@FindBy(xpath = "//div/div[@class=\"navbar-inner\"]/div[@class=\"container\"]/ul/li/a[@href=\"/sharetask/signin\"]")
	private WebElementFacade signInButton;
	
    @WhenPageOpens
    public void waitUntilUserNameAppears() {
    	element(signInButton).waitUntilVisible();
    }
	
    public void checkLogedOut(final String message) {
    	assertThat("Must contains text", signInButton.getTextValue(), containsString(message));
    }
}