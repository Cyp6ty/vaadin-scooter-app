package fi.sauli.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@Menu(title = "Etusivu", icon = "home")
public class HomeView extends VerticalLayout {

    public HomeView() {
        addClassName("home-view");
        H1 title = new H1("Sähköpotkulauta kaluston hallinta");
        Paragraph text = new Paragraph("Hallinnoi potkulautoja, niiden ominaisuuksia, asemia ja ajoja");

        Button rides = new Button("Hallitse ajoja");
        rides.addClickListener(e -> rides.getUI().ifPresent(ui -> ui.navigate("rides")));

        Button scooters = new Button("Hallitse potkulautoja");
        scooters.addClickListener(e -> scooters.getUI().ifPresent(ui -> ui.navigate("scooters")));

        add(title, text, rides, scooters);
    }
}
