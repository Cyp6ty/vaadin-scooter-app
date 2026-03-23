package fi.sauli.view.ride;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Ride;
import fi.sauli.entity.Scooter;
import fi.sauli.service.RideService;
import fi.sauli.service.ScooterService;
import fi.sauli.service.StationService;

@Route(value = "rides", layout = MainLayout.class)
@Menu(title = "Ajot")
public class RideView extends VerticalLayout {

    private final RideService rideService;
    private final ScooterService scooterService;
    private final StationService stationService;

    private Grid<Ride> grid = new Grid<>(Ride.class, false);
    private RideForm form = new RideForm();
    private Button addNewButton = new Button("Lisää uusi ajo");

    // --- Konstruktor ---
    public RideView(RideService rideService, ScooterService scooterService, StationService stationService) {
        this.rideService = rideService;
        this.scooterService = scooterService;
        this.stationService = stationService;

        configureGrid();
        configureForm();
        add(addNewButton);

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        add(content);

        setSizeFull();
        updateList();
        closeEditor();

        addNewButton.addClickListener(click -> addRide());
    }

    // Määrittää listan
    private void configureGrid() {
        grid.addColumn(Ride::getId).setHeader("ID");
        grid.addColumn(Ride::getStartTime).setHeader("Aloitusaika");
        grid.addColumn(Ride::getEndTime).setHeader("Lopetusaika");
        grid.addColumn(Ride::getDistanceKm).setHeader("Kilometri määrä");
        grid.addColumn(Ride::getPrice).setHeader("Ajon hinta");
        grid.addColumn(Ride::getStatus).setHeader("Status");
        grid.addColumn(ride -> ride.getScooter().getSerialNumber() +
                        " - " + ride.getScooter().getModel())
                .setHeader("Potkulauta");

        grid.addColumn(ride -> ride.getStartStation().getName())
                .setHeader("Aloitusasema");

        grid.addColumn(ride -> ride.getEndStation().getName())
                .setHeader("Pääteasema");

        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(
                event -> editRide(event.getValue()));
    }

    // Määrittää buttonit
    private void configureForm() {
        form.setWidth("25em");

        form.setScooters(scooterService.findAll());
        form.setStations(stationService.findAll());

        form.getSaveButton().addClickListener(event -> saveRide());
        form.getDeleteButton().addClickListener(event -> deleteRide());
        form.getCancelButton().addClickListener(event -> closeEditor());
    }

    // Hakee tiedot tietokannasta
    private void updateList() {
        grid.setItems(rideService.findAll());
    }

    // Lisää uuden
    private void addRide() {
        grid.asSingleSelect().clear();
        form.setRide(new Ride());
        form.setVisible(true);
    }

    // Avaa valitun muokattavaksi
    private void editRide(Ride ride) {
        if (ride == null) {
            closeEditor();
        } else {
            form.setRide(ride);
            form.setVisible(true);
        }
    }

    // Tallentaa muutokset
    private void saveRide() {
        Ride ride = form.getRide();

        if (ride != null && form.writeRide()) {
            rideService.save(ride);
            updateList();
            closeEditor();
            Notification.show("Ajo tallennettu");
        }
    }

    // Poistaa valitun
    private void deleteRide() {
        Ride ride = form.getRide();

        if (ride != null && ride.getId() != null) {
            rideService.delete(ride);
            updateList();
            closeEditor();
            Notification.show("Ajo poistettu");
        }
    }

    // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setRide(null);
        form.setVisible(false);
    }
}
