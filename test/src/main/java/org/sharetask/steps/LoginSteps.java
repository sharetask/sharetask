package org.sharetask.steps;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;

import org.sharetask.pages.SigninPage;

public class LoginSteps extends ScenarioSteps {

    SigninPage signinPage;

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
}
