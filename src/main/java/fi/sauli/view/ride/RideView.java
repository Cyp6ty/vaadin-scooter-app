package fi.sauli.view.ride;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Ride;
import fi.sauli.entity.Scooter;
import fi.sauli.entity.Station;
import fi.sauli.filter.RideFilter;
import fi.sauli.service.RideService;
import fi.sauli.service.ScooterService;
import fi.sauli.service.StationService;
import jakarta.annotation.security.PermitAll;

@Route(value = "rides", layout = MainLayout.class)
@Menu(title = "Ajot", icon = "road")
@PermitAll
public class RideView extends VerticalLayout {

    private final RideService rideService;
    private final ScooterService scooterService;
    private final StationService stationService;

    private Grid<Ride> grid = new Grid<>(Ride.class, false);
    private RideForm form = new RideForm();
    private Button addNewButton = new Button("Lisää uusi ajo");

    // Hakukentät + Buttonit
    private final TextField keyword = new TextField("Hakusana");
    private final ComboBox<String> status = new ComboBox<>("Status");
    private final ComboBox<Station> startStation = new ComboBox<>("Lähtöasema");
    private final DatePicker fromDate = new DatePicker("Ajon aloituspäivä");
    private final DatePicker toDate = new DatePicker("Ajon lopetuspäivä");
    private final ComboBox<String> sortBy = new ComboBox<>("Lajittelu");
    private final Button searchButton = new Button("Hae");
    private final Button clearButton = new Button("Tyhjennä");

    // --- Konstruktor ---
    public RideView(RideService rideService,
                    ScooterService scooterService,
                    StationService stationService) {
        this.rideService = rideService;
        this.scooterService = scooterService;
        this.stationService = stationService;

        // // Tyylit ja ulkoasu - Tehtävä 2-B
        addNewButton.getStyle().set("align-self", "flex-start");
        addNewButton.getStyle().set("margin-bottom", "1rem");
        addNewButton.getStyle().set("width", "220px");

        configureGrid();
        configureForm();

        // --- Hakukenttien asetukset ---
        // Status
        status.setItems(
                "COMPLETED",
                "CANCELLED",
                "FAILED",
                "INTERRUPTED"
        );
        status.setPlaceholder("Valitse status");
        status.setClearButtonVisible(true);

        // Asemat
        startStation.setItems(stationService.findAll());
        startStation.setItemLabelGenerator(station ->
                station.getCity() + " - " + station.getName());
        startStation.setPlaceholder("Valitse lähtöasema");
        startStation.setClearButtonVisible(true);

        // Päivämäärät
        fromDate.setClearButtonVisible(true);
        toDate.setClearButtonVisible(true);

        // Lajittelu
        sortBy.setItems(
                "startTimeAsc",
                "startTimeDesc",
                "priceAsc",
                "priceDesc"
        );
        sortBy.setPlaceholder("Valitse lajittelu");
        sortBy.setClearButtonVisible(true);

        // Hakusana
        keyword.setPlaceholder("Hae esim. scooter, asema...");
        keyword.setClearButtonVisible(true);

        // Käynnistää haun
        searchButton.addClickListener(event ->
                grid.setItems(rideService.searchRides(buildFilter())));

        // Tyhjentää haun
        clearButton.addClickListener(event -> clearFilters());

        add(addNewButton, getFilterLayout());
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        content.getStyle().set("padding-bottom", "80px");
        add(content);

        setSizeFull();
        updateList();
        closeEditor();

        addNewButton.addClickListener(click -> addRide());
    }

    // Määrittää hakulistan
    private HorizontalLayout getFilterLayout() {
        HorizontalLayout filters = new HorizontalLayout(
                keyword,
                status,
                startStation,
                fromDate,
                toDate,
                sortBy,
                searchButton,
                clearButton
        );
        // Tyylit ja ulkoasu - Tehtävä 2-A
        filters.addClassName("ride-filter-bar");
        keyword.addClassName("ride-keyword-field");

        filters.setWidthFull();
        filters.setAlignItems(FlexComponent.Alignment.END);
        return filters;
    }

    // Rakentaa hakuehdot
    private RideFilter buildFilter() {
        RideFilter filter = new RideFilter();

        filter.setKeyword(keyword.getValue());
        filter.setStatus(status.getValue());
        filter.setStartStation(startStation.getValue());
        filter.setFromDate(fromDate.getValue());
        filter.setToDate(toDate.getValue());
        filter.setSortBy(sortBy.getValue());
        return filter;
    }

    // Tyhjentää haun ja palauttaa listan
    private void clearFilters() {
        keyword.clear();
        status.clear();
        startStation.clear();
        fromDate.clear();
        toDate.clear();
        sortBy.clear();

        grid.setItems(rideService.findAll());
    }

    // Määrittää ajolistan
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
