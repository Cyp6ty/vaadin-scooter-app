package fi.sauli.view.station;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Station;
import fi.sauli.service.StationService;
import com.vaadin.flow.component.notification.Notification;


//@Route("stations")
@Route(value = "stations", layout = MainLayout.class)
@Menu(title = "Asemat")
public class StationView extends VerticalLayout {

    private final StationService stationService;

    private Grid<Station> grid = new Grid<>(Station.class, false);
    private StationForm form = new StationForm();
    private Button addNewButton = new Button("Lisää uusi asema");

    public StationView(StationService stationService) {
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

        addNewButton.addClickListener(click -> addStation());
    }

    // Määrittää listan
    private void configureGrid() {
        grid.addColumn(Station::getId).setHeader("ID");
        grid.addColumn(Station::getName).setHeader("Aseman nimi");
        grid.addColumn(Station::getCity).setHeader("Kaupunki");
        grid.addColumn(Station::getAddress).setHeader("Osoite");
        grid.addColumn(Station::getCapacity).setHeader("Kapasiteetti");
        grid.addColumn(Station::getArea).setHeader("Kaupungin osa");

        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(event -> editStation(event.getValue()));
    }

    // Määrittää buttonit
    private void configureForm() {
        form.setWidth("25em");

        form.getSaveButton().addClickListener(event -> saveStation());
        form.getDeleteButton().addClickListener(event -> deleteStation());
        form.getCancelButton().addClickListener(event -> closeEditor());
    }

    // Hakee tiedot tietokannasta
    private void updateList() {
        grid.setItems(stationService.findAll());
    }

    // Lisää uuden
    private void addStation() {
        grid.asSingleSelect().clear();
        form.setStation(new Station());
        form.setVisible(true);
    }

    // Avaa valitun muokattavaksi
    private void editStation(Station station) {
        if (station == null) {
            closeEditor();
        } else {
            form.setStation(station);
            form.setVisible(true);
        }
    }

    // Tallentaa muutokset
    private void saveStation() {
        Station station = form.getStation();

        if (station != null && form.writeStation()) {
            stationService.save(station);
            updateList();
            closeEditor();
            Notification.show("Asema tallennettu");
        }
    }

    // Poistaa valitun
    private void deleteStation() {
        Station station = form.getStation();

        if (station != null && station.getId() != null) {
            stationService.delete(station);
            updateList();
            closeEditor();
            Notification.show("Asema poistettu");
        }
    }

    // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setStation(null);
        form.setVisible(false);
    }
}
