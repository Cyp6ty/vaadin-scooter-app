package fi.sauli.view.station;

// todo:
// Grid, jossa näkyvät stationit
// - nappi uuden stationin lisäykseen
// - mahdollisuus valita rivi muokkausta varten
// - poistonappi

/*
* Tähän näkymään tulee yleensä:
- Grid<Station>
- FormLayout tai oma form-komponentti
- Button save
- Button delete
- Button cancel
- Button add new
*/

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import fi.sauli.entity.Station;
import fi.sauli.service.StationService;
import com.vaadin.flow.component.notification.Notification;

//import javax.management.Notification;

@Route("stations")
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

    private void configureGrid() {
        grid.addColumn(Station::getId).setHeader("ID");
        grid.addColumn(Station::getName).setHeader("Nimi");
        grid.addColumn(Station::getCity).setHeader("Kaupunki");
        grid.addColumn(Station::getAddress).setHeader("Osoite");
        grid.addColumn(Station::getCapacity).setHeader("Kapasiteetti");
        grid.addColumn(Station::getArea).setHeader("Alue");

        grid.setSizeFull();

        grid.asSingleSelect().addValueChangeListener(event -> editStation(event.getValue()));
    }

    private void configureForm() {
        form.setWidth("25em");

        form.getSaveButton().addClickListener(event -> saveStation());
        form.getDeleteButton().addClickListener(event -> deleteStation());
        form.getCancelButton().addClickListener(event -> closeEditor());
    }

    private void updateList() {
        grid.setItems(stationService.findAll());
    }

    private void addStation() {
        grid.asSingleSelect().clear();
        form.setStation(new Station());
        form.setVisible(true);
    }

    private void editStation(Station station) {
        if (station == null) {
            closeEditor();
        } else {
            form.setStation(station);
            form.setVisible(true);
        }
    }

    private void saveStation() {
        Station station = form.getStation();

        if (station != null && form.writeStation()) {
            stationService.save(station);
            updateList();
            closeEditor();
            Notification.show("Asema tallennettu");
        }
    }

    private void deleteStation() {
        Station station = form.getStation();

        if (station != null && station.getId() != null) {
            stationService.delete(station);
            updateList();
            closeEditor();
            Notification.show("Asema poistettu");

        }
    }

    private void closeEditor() {
        form.setStation(null);
        form.setVisible(false);
    }
}
