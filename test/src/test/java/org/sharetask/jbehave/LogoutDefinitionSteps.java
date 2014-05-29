package org.sharetask.jbehave;

import net.thucydides.core.annotations.Steps;

import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.sharetask.steps.LogoutSteps;

public class LogoutDefinitionSteps {

    @Steps
    LogoutSteps logout;

    @Given("Open workspace page")
    public void givenLongedIn() {
    	logout.open_workspacePage();
    }

    @When("Pushing logout button")
    public void whenPushingLogoutButton() {
        logout.logout();
    }

    @Then("User is logged out")
    public void loggedOn() {
    	logout.should_see_sign_in_button();
    }

    @Given("Logout user")
    public void givenLogoutUser() {
    	logout.open_workspacePage();
        logout.logout();
    	logout.should_see_sign_in_button();
    }
}
