package org.sharetask.jbehave;

import net.thucydides.core.annotations.Steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.sharetask.steps.LoginSteps;

public class LoginDefinitionSteps {

    @Steps
    LoginSteps login;

    @Given("Open login page")
    public void givenLongin() {
    	login.open_loginPage();
    }

    @Given("User $userName with password $password")
    public void givenUserNameAndPassword(@Named("userName") final String userName, @Named("password") final String password) {
    	login.enter_username(userName);
    	login.enter_password(password);
    }

    @When("Pushing login button")
    public void whenPushingLoginButton() {
        login.login();
    }

    @Then("User is logged in")
    public void loggedOn() {
    	login.should_see_task_list();
    }

    @Given("Login user $userName with password $password")
    public void givenLoginUser(@Named("userName") final String userName, @Named("password") final String password) {
    	login.open_loginPage();
    	login.enter_username(userName);
    	login.enter_password(password);
        login.login();
    	login.should_see_task_list();
    }
    
}
