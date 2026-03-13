package fi.sauli.view.station;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import fi.sauli.entity.Station;

public class StationForm extends FormLayout {

    // Kentät
    private TextField name = new TextField("Aseman nimi");
    private TextField city = new TextField("Kaupunki");
    private TextField address = new TextField("Osoite");
    private IntegerField capacity = new IntegerField("Kapasiteetti");
    private TextField area = new TextField("Area");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    // Yhdistää: form field <-> entity field
    private Binder<Station> binder = new Binder<>(Station.class);

    public StationForm() {
        add(
                name,
                city,
                address,
                capacity,
                area,
                save,
                delete,
                cancel
        );

        // Automaattinen sidonta
        binder.bindInstanceFields(this);
    }

    // tallennetaan se station jota formissa muokataan
    private Station station;

    // Asettaa formille muokattavan olion ja näyttää sen tiedot kentissä
    public void setStation(Station station) {
        this.station = station;

        if (station != null) {
            binder.readBean(station);
        } else {
            binder.readBean(new Station());
        }
    }

    public Station getStation() {
        return station;
    }

    public Button getSaveButton() {
        return save;
    }

    public Button getDeleteButton() {
        return delete;
    }

    public Button getCancelButton() {
        return cancel;
    }

    // Kirjoittaa kenttien arvot takaisin station-olioon
    public boolean writeStation() {
        try {
            binder.writeBean(station); // Lukee arvot kenttiin
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
