package org.sharetask.pages;

import net.thucydides.core.annotations.DefaultUrl;
import net.thucydides.core.annotations.WhenPageOpens;
import net.thucydides.core.annotations.findby.FindBy;
import net.thucydides.core.pages.PageObject;
import net.thucydides.core.pages.WebElementFacade;

@DefaultUrl("http://localhost:8080/sharetask/signin")
public class SigninPage extends PageObject {

	@FindBy(name = "username")
	private WebElementFacade username;

	@FindBy(name = "password")
	private WebElementFacade password;

	@FindBy(xpath = "//form[@name=\"formLogin\"]/button")
	private WebElementFacade loginButton;

    @WhenPageOpens
    public void waitUntilUserNameAppears() {
    	element(username).waitUntilVisible();
    }
	
    public void enter_username(final String username) {
    	this.username.type(username);
    }

    public void enter_password(final String password) {
    	this.password.type(password);
    }

    public void login() {
    	loginButton.click();
    }
}