package org.sharetask.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.sharetask.pages.SigninPage;
import org.sharetask.pages.WorkspacePage;

public class LoginSteps extends ScenarioSteps {

    SigninPage signinPage;

    WorkspacePage workspacePage;
    
    @Step
    public void open_loginPage() {
        signinPage.open();
    }
    
    @Step
    public void enter_username(final String name) {
        signinPage.enter_username(name);
    }

    @Step
    public void enter_password(final String password) {
        signinPage.enter_password(password);
    }

    @Step
    public void login() {
        signinPage.login();
    }
    
    @Step
    public void should_see_task_list() {
        workspacePage.checkMessage("Pending Tasks");
    }
    
}
