package org.sharetask.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.sharetask.pages.MainPage;
import org.sharetask.pages.WorkspacePage;

public class LogoutSteps extends ScenarioSteps {

    MainPage mainPage;

    WorkspacePage workspacePage;
    
    @Step
    public void open_workspacePage() {
        workspacePage.open();
    }
    
    @Step
    public void logout() {
        workspacePage.logout();
    }

    @Step
    public void should_see_sign_in_button() {
        mainPage.checkLogedOut("Sign in");
    }
    
}
