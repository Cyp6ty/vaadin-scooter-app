package fi.sauli.view.scooter;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Feature;
import fi.sauli.entity.Scooter;
import fi.sauli.service.FeatureService;
import fi.sauli.service.ScooterService;
import com.vaadin.flow.component.notification.Notification;
import fi.sauli.service.StationService;

import java.util.stream.Collectors;


@Route(value = "scooters", layout = MainLayout.class)
@Menu(title = "Potkulaudat")
public class ScooterView extends VerticalLayout {

     private final ScooterService scooterService;

     private Grid<Scooter> grid = new Grid<>(Scooter.class, false);
     private ScooterForm form;
     private Button addNewButton = new Button("Lisää uusi potkulauta");

     public ScooterView(ScooterService scooterService,
                        StationService stationService,
                        FeatureService fetureService) {

         this.scooterService = scooterService;
         this.form = new ScooterForm(stationService,fetureService);

         configureGrid();
         configureForm();
         add(createInfoBox(), addNewButton);

         HorizontalLayout content = new HorizontalLayout(grid, form);
         content.setSizeFull();
         content.getStyle().set("padding-bottom", "80px");
         add(content);

         setSizeFull();
         updateList();
         closeEditor();

         addNewButton.addClickListener(click -> addScooter());
     }

    // Tyylit ja ulkoasu: Tehtävä 4
    private VerticalLayout createInfoBox() {
        H2 title = new H2("Potkulautojen hallinta");
        Paragraph text = new Paragraph("Tässä näkymässä voit lisätä uuden laudan tai poistaa vanhan sekä tarkastella ja muokata olemassa olevia.");
        VerticalLayout infoBox = new VerticalLayout(title, text);
        infoBox.setAlignItems(Alignment.START);

        infoBox.addClassName("glass-border");
        infoBox.addClassNames(
                LumoUtility.MaxWidth.SCREEN_LARGE,
                LumoUtility.BackdropBlur.MEDIUM,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.BoxShadow.SMALL,
                LumoUtility.Border.ALL,
                LumoUtility.BorderColor.CONTRAST_20,
                LumoUtility.BorderRadius.MEDIUM,
                LumoUtility.Margin.SMALL
        );

        title.addClassNames(
                LumoUtility.TextColor.PRIMARY,
                LumoUtility.FontSize.XLARGE
        );

        text.addClassNames(
                LumoUtility.TextColor.SECONDARY,
                LumoUtility.FontSize.LARGE
        );

        return infoBox;
    }

     // Määrittää listan
     private void configureGrid() {
         grid.addColumn(Scooter::getId).setHeader("ID");
         grid.addColumn(Scooter::getSerialNumber).setHeader("Sarjanumero");
         grid.addColumn(Scooter::getModel).setHeader("Malli");
         grid.addColumn(Scooter::getBatteryLevel).setHeader("Akun varaustaso (%)");
         grid.addColumn(Scooter::getStatus).setHeader("Status");
         grid.addColumn(Scooter::getManufactureYear).setHeader("Valmistusvuosi");

         grid.addColumn(scooter ->
                         scooter.getStation() != null
                                 ? scooter.getStation().getCity() + " - " + scooter.getStation().getName()
                                 : "")
                        .setHeader("Asema");

         /*
         * Tämä aiheuttaa "lazy loading" virheen,
           joten featuret eivät näy gridissä,
           mutta ne voi lisätä scooterille UI:ssa

         grid.addColumn(scooter ->
                         scooter.getFeatures() != null
                                 ? scooter.getFeatures().stream()
                                 .map(Feature::getName)
                                 .collect(Collectors.joining(", "))
                                 : "")
                        .setHeader("Ominaisuudet");
        */

         grid.setSizeFull();
         grid.asSingleSelect().addValueChangeListener(
                 event -> editScooter(event.getValue()));
     }

     // Määrittää buttonit
     private void configureForm() {
         form.setWidth("25em");
         form.getSaveButton().addClickListener(event -> saveScooter());
         form.getDeleteButton().addClickListener(event -> deleteScooter());
         form.getCancelButton().addClickListener(event -> closeEditor());
     }

     // Hakee tiedot tietokannasta
     private void updateList() {
         grid.setItems(scooterService.findAll());
     }

     // Lisää uuden
     private void addScooter() {
         grid.asSingleSelect().clear();
         form.setScooter(new Scooter());
         form.setVisible(true);
     }

     // Avaa valitun muokattavaksi
     private void editScooter(Scooter scooter) {
        if (scooter == null) {
             closeEditor();
        } else {
            form.setScooter(scooter);
            form.setVisible(true);
        }
     }

     // Tallentaa muutokset
     private void saveScooter() {
         Scooter scooter = form.getScooter();

         if (scooter != null && form.writeScooter()) {
             scooterService.save(scooter);
             updateList();
             closeEditor();
             Notification.show("Uusi potkulauta tallennettu");
         } else {
             Notification.show("Tarkista lomakkeen tiedot");
         }
     }

     // Poistaa valitun
     private void deleteScooter() {
         Scooter scooter = form.getScooter();

         if (scooter != null && scooter.getId() != null) {
             scooterService.delete(scooter);
             updateList();
             closeEditor();
             Notification.show("Potkulauta poistettu");
         }
     }

     // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setScooter(null);
        form.setVisible(false);
    }
}
