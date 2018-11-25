package io.rty.incub.authentication;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

/**
 * UI content when the user is not logged in yet.
 */
@Route("Login")
@PageTitle("Login")
@StyleSheet("css/shared-styles.css")
public class LoginScreen extends FlexLayout {

    private TextField username;
    private PasswordField password;
    private Button login;
    private Button forgotPassword;
    private AccessControl accessControl;

    /**
     * Default constructor
     */
    public LoginScreen() {
        accessControl = AccessControlFactory.getInstance().createAccessControl();
        buildUI();
        username.focus();
    }

    /**
     * Set up the UI
     */
    private void buildUI() {
        setSizeFull();
        setClassName("login-screen");

        // login form, centered in the available part of the screen
        final Component loginForm = buildLoginForm();

        // layout to center login form when there is sufficient screen space
        final FlexLayout centeringLayout = new FlexLayout();
        centeringLayout.setSizeFull();
        centeringLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        centeringLayout.setAlignItems(Alignment.CENTER);
        centeringLayout.add(loginForm);

        // information text about logging in
        final Component loginInformation = buildLoginInformation();

        add(loginInformation);
        add(centeringLayout);
    }

    private Component buildLoginForm() {
        final FormLayout loginForm = new FormLayout();

        loginForm.setWidth("310px");

        loginForm.addFormItem(username = new TextField(), "Username");
        username.setWidth("15em");
        username.setValue("admin");
        loginForm.add(new Html("<br/>"));
        loginForm.addFormItem(password = new PasswordField(), "Password");
        password.setWidth("15em");

        HorizontalLayout buttons = new HorizontalLayout();
        loginForm.add(new Html("<br/>"));
        loginForm.add(buttons);

        buttons.add(login = new Button("Login"));
        login.addClickListener(event -> login());
        loginForm.getElement().addEventListener("keypress", event -> login()).setFilter("event.key == 'Enter'");
        login.getElement().getThemeList().add("success primary");

        buttons.add(forgotPassword = new Button("Forgot password?"));
        forgotPassword.addClickListener(event -> showNotification(new Notification("Hint: try anything")));
        forgotPassword.getElement().getThemeList().add("tertiary");

        return loginForm;
    }

    private Component buildLoginInformation() {
        final VerticalLayout loginInformation = new VerticalLayout();
        loginInformation.setClassName("login-information");

        final H1 loginInfoHeader = new H1("Waldogm");
        final Span loginInfoText = new Span("Welcome to the dashboard, pelase login");
        loginInformation.add(loginInfoHeader);
        loginInformation.add(loginInfoText);

        return loginInformation;
    }

    private void login() {
        login.setEnabled(false);
        try {
            if (accessControl.signIn(username.getValue(), password.getValue())) {
                getUI().get().navigate("");
            } else {
                showNotification(new Notification("Login failed. " +
                        "Please check your username and password and try again."));
                username.focus();
            }
        } finally {
            login.setEnabled(true);
        }
    }

    private void showNotification(Notification notification) {
        // keep the notification visible a little while after moving the
        // mouse, or until clicked
        notification.setDuration(2000);
        notification.open();
    }
}
