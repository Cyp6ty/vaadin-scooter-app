package fi.sauli.view.login;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import fi.sauli.service.UserService;

@Route("register")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    // Rekisteröinti näkymä uusille käyttäjille
    public RegisterView(UserService userService) {

        TextField username = new TextField("Käyttäjätunnus");
        PasswordField password = new PasswordField("Salasana");
        PasswordField confirmPassword = new PasswordField("Vahvista salasana");

        Button registerBtn = new Button("Rekisteröidy", e -> {

            // Tarkistaa syötteen ja luo käyttäjän
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Notification.show("Täytä kaikki kentät");
                return;
            }

            // Varmistaa salasanat
            if(!password.getValue().equals(confirmPassword.getValue())) {
                Notification.show("Salasanat eivät täsmää");
                return;
            }

            try {
                userService.registerUser(
                        username.getValue(),
                        password.getValue()
                );
                Notification.show("Rekisteröinti onnistui");
                getUI().ifPresent(ui -> ui.navigate("login"));
            } catch (IllegalArgumentException ex) {
                Notification.show(ex.getMessage());
            }
        });

        add(username, password, confirmPassword, registerBtn);
        setAlignItems(Alignment.CENTER);
    }
}
