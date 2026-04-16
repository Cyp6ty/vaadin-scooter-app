package fi.sauli.view.feature;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AccessDeniedException;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AccessDeniedErrorRouter;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Feature;
import fi.sauli.service.FeatureService;
import com.vaadin.flow.component.notification.Notification;
import jakarta.annotation.security.RolesAllowed;


@Route(value = "features", layout = MainLayout.class)
@Menu(title = "Ominaisuudet", icon = "cog")
@RolesAllowed( { "ADMIN" } )
@AccessDeniedErrorRouter
public class FeatureView extends VerticalLayout {

    private final FeatureService featureService;

    private Grid<Feature> grid = new Grid<>(Feature.class, false);
    private FeatureForm form = new FeatureForm();
    private Button addNewButton = new Button("Lisää ominaisuus");

    public FeatureView(FeatureService featureService) {
        this.featureService = featureService;

        configureGrid();
        configureForm();
        add(addNewButton);

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        add(content);

        setSizeFull();
        updateList();
        closeEditor();

        addNewButton.addClickListener(click -> addFeature());
    }

    private void configureGrid() {
        grid.addColumn(Feature::getId).setHeader("ID");
        grid.addColumn(Feature::getName).setHeader("Ominaisuuden nimi");
        grid.addColumn(Feature::getDescription).setHeader("Kuvaus");
        grid.addColumn(Feature::getCategory).setHeader("Kategoria");
        grid.addColumn(feature -> Boolean.TRUE.equals(feature.getActive())
                        ? "Aktiivinen"
                        : "Ei aktiivinen")
                .setHeader("Aktiivisuus");
        grid.addColumn(Feature::getInstallationCost).setHeader("Asennuksen hinta (€)");

        /* Tämä ei toimi vielä oikein.
        H2-console näyttää tiedot, mutta ei näy UI:ssa. (Lazy loading ongelma)

        grid.addColumn(feature -> {
            try {
                return feature.getScooters().size();
            } catch (Exception e) {
                return 0;
            }
        }).setHeader("Käytössä (kpl)");
*/
        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(event -> editFeature(event.getValue()));
    }

    // Määrittää buttonit
    private void configureForm() {
        form.setWidth("25em");

        form.getSaveButton().addClickListener(event -> saveFeature());
        form.getDeleteButton().addClickListener(event -> deleteFeature());
        form.getCancelButton().addClickListener(event -> closeEditor());
    }

    // Hakee tiedot tietokannasta
    private void updateList() {
        grid.setItems(featureService.findAll());
    }

    // Lisää uuden
    private void addFeature() {
        grid.asSingleSelect().clear();
        form.setFeature(new Feature());
        form.setVisible(true);
    }

    // Avaa valitun muokattavaki
    private void editFeature(Feature feature) {
        if (feature == null) {
            closeEditor();
        } else {
            form.setFeature(feature);
            form.setVisible(true);
        }
    }

    // Tallentaa muutokset
    private void saveFeature() {
        Feature feature = form.getFeature();

        if (feature != null && form.writeFeature()) {
            featureService.save(feature);
            updateList();
            closeEditor();
            Notification.show("Muutokset tallennettu");
        } else {
            Notification.show("Tarkista lomakkeen tiedot");
        }
    }

    // Poistaa valitun
    private void deleteFeature() {
        Feature feature = form.getFeature();

        if (feature != null && feature.getId() != null) {
            featureService.delete(feature);
            updateList();
            closeEditor();
            Notification.show("Ominaisuus poistettu");
        }
    }

    // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setFeature(null);
        form.setVisible(false);
    }
}
