package fi.sauli.view.error;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;

// Customoitu näkymä käyttöoikeusvirheelle (Access Denied)
@PermitAll
public class CustomAccessDeniedError extends VerticalLayout
        implements HasErrorParameter<AccessDeniedException> {

    // --- Konstruktor ---
    public CustomAccessDeniedError() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSpacing(true);
    }

    // Näyttää virheviestin ja ohjaa takaisin etusivulle
    @Override
    public int setErrorParameter(BeforeEnterEvent event,
                                 ErrorParameter<AccessDeniedException> parameter) {
        removeAll();

        H2 title = new H2("Pääsy estetty");
        Paragraph message = new Paragraph("Sinulla ei ole oikeuksia avata tätä sivua.");

        Button backButton = new Button("Takaisin etusivulle", e ->
                event.getUI().navigate("")
        );

        add(title, message, backButton);

        return HttpServletResponse.SC_FORBIDDEN;
    }
}