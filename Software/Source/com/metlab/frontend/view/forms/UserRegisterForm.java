package com.metlab.frontend.view.forms;

import com.metlab.frontend.ICallbackFunction;
import com.vaadin.server.Page;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.VerticalLayout;



public class UserRegisterForm extends VerticalLayout
{
    public UserRegisterForm(ICallbackFunction registerCallback, ICallbackFunction enterLoginFormCallback)
    {
        Page.getCurrent().setTitle("Registrieren");
        textFieldFirstName.setCaption("Vorname:");
        textFieldLastName.setCaption("Nachname:");
        textFieldEmail.setCaption("E-Mail:");
        textFieldPassword_1.setCaption("Passwort:");
        textFieldPassword_2.setCaption("Passwort wiederholen:");
        textFieldCompany.setCaption("Firmenbezeichner:");
        buttonRegister.addClickListener((Button.ClickEvent event) ->
        {
            registerCallback.execute(new String[]{
                    textFieldEmail.getValue(),
                    textFieldPassword_1.getValue(),
                    textFieldFirstName.getValue(),
                    textFieldLastName.getValue(),
                    textFieldCompany.getValue()
            });
        });
        buttonLogin.addClickListener((Button.ClickEvent event) ->
        {
            enterLoginFormCallback.execute(null);
        });
        this.addComponents(textFieldFirstName, textFieldLastName, textFieldEmail,
                textFieldPassword_1, textFieldPassword_2, textFieldCompany,
                buttonLogin, buttonRegister);
    }

    private final TextField textFieldFirstName = new TextField();
    private final TextField textFieldLastName = new TextField();
    private final TextField textFieldEmail = new TextField();
    private final PasswordField textFieldPassword_1 = new PasswordField();
    private final PasswordField textFieldPassword_2 = new PasswordField();
    private final TextField textFieldCompany = new TextField();
    private final Button buttonLogin =  new Button("Anmelden");
    private final Button buttonRegister = new Button("Registrieren");
}