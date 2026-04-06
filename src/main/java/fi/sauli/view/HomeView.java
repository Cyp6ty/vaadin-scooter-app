package fi.sauli.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;

@Route(value = "", layout = MainLayout.class)
@Menu(title = "Etusivu", icon = "home")
public class HomeView extends VerticalLayout {

    // --- Konstruktor ---
    public  HomeView() {
        addClassName("home-view");
        setWidthFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);
        getStyle().set("padding-bottom", "200px");

        add(createHeroSection(), createStatsSection());
    }


    // HeroSection
    private VerticalLayout createHeroSection() {

        H1 title = new H1("Scooter Fleet Management");
        Paragraph text = new Paragraph(
                "Hallitse potkulautoja, ajoja, asemia ja potkulautojen ominaisuuksia. " +
                        "Seuraa kaluston tilaa ja siirry sovelluksen hallintasivuille."
        );

        Button rides = new Button("Siirry ajoihin");
        rides.addClickListener(e ->
                rides.getUI().ifPresent(ui -> ui.navigate("rides"))
        );

        Button scooters = new Button("Siirry potkulautoihin");
        scooters.addClickListener(e ->
                scooters.getUI().ifPresent(ui -> ui.navigate("scooters"))
        );

        // Button-layout
        HorizontalLayout buttons = new HorizontalLayout(rides, scooters);
        buttons.setJustifyContentMode(JustifyContentMode.CENTER);
        buttons.setSpacing(true);

        VerticalLayout layout = new VerticalLayout(title, text, buttons);
        layout.setAlignItems(Alignment.CENTER);
        layout.setSpacing(true);
        layout.setPadding(false);
        layout.setWidth("100%");
        layout.setMaxWidth("900px");
        layout.getStyle().set("margin-top", "3rem");

        return layout;
    }


    // Stats
    private FlexLayout createStatsSection() {
        FlexLayout stats = new FlexLayout(
                createStatCard("Potkulaudat", "12", "Rekisteröityä kalustoa", "scooters"),
                createStatCard("Asemat", "6", "Käytössä olevaa asemaa", "stations"),
                createStatCard("Ajot", "28", "Tallennettua ajoa", "rides")
        );

        stats.setWidth("100%");
        stats.setMaxWidth("1100px");
        stats.setJustifyContentMode(FlexLayout.JustifyContentMode.CENTER);
        stats.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        stats.getStyle().set("gap", "1rem");
        stats.getStyle().set("margin-top", "2rem");

        return stats;
    }


    // Stats Cards
    private VerticalLayout createStatCard(String titleText, String valueText, String descriptionText, String route) {
        H2 title = new H2(titleText);
        Span value = new Span(valueText);
        Paragraph description = new Paragraph(descriptionText);

        Button open = new Button("Avaa");
        open.addClickListener(e ->
                open.getUI().ifPresent(ui -> ui.navigate(route))
        );

        VerticalLayout card = new VerticalLayout(title, value, description, open);
        card.setAlignItems(Alignment.CENTER);
        card.setSpacing(false);
        card.setPadding(true);
        card.setWidth("260px");

        card.getStyle()
                .set("background", "rgba(255,255,255,0.15)")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 12px rgba(0,0,0,0.15)")
                .set("backdrop-filter", "blur(6px)");

        value.getStyle()
                .set("font-size", "2rem")
                .set("font-weight", "700");

        return card;
    }
}
