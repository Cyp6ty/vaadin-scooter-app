package fi.sauli.view.scooter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import fi.sauli.entity.Feature;
import fi.sauli.entity.Scooter;
import fi.sauli.entity.Station;
import fi.sauli.service.FeatureService;
import fi.sauli.service.StationService;

public class ScooterForm extends FormLayout {

    // Kentät
    private TextField model = new TextField("Malli");
    private TextField serialNumber = new TextField("Sarjanumero");
    private IntegerField batteryLevel = new IntegerField("Akun varaustaso");
    private ComboBox<String> status = new ComboBox<>("Status");
    private IntegerField manufactureYear = new IntegerField("Valmistusvuosi");

    // Relaatio kentät
    private ComboBox<Station> station = new ComboBox<>("Asema");
    private MultiSelectComboBox<Feature> features = new MultiSelectComboBox<>("Ominaisuudet");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<Scooter> binder = new Binder<>(Scooter.class);

    private final StationService stationService;
    private final FeatureService featureService;

    private Scooter scooter;


    // --- Konstruktor ---
    public ScooterForm(StationService stationService,
                       FeatureService featureService) {

        this.stationService = stationService;
        this.featureService = featureService;

        setWidth("25em");
        add(
                model,
                serialNumber,
                batteryLevel,
                status,
                manufactureYear,
                station,
                features,
                save,
                delete,
                cancel
        );

        configureFields();
        configureValidation();
    }

    // Asettaa ComBoxien sisällön
    private void configureFields() {

        // Status
        status.setItems(
                "AVAILABLE",
                "IN_USE",
                "MAINTENANCE",
                "OUT_OF_SERVICE"
        );
        status.setPlaceholder("Valitse status");
        status.setClearButtonVisible(true);

        // Station
        station.setItems(stationService.findAll());
        station.setItemLabelGenerator(s ->
                s.getCity() + " - " + s.getName());
        station.setPlaceholder("Valitse asema");
        station.setClearButtonVisible(true);

        // Features
        features.setItems(featureService.findAll());
        features.setItemLabelGenerator(Feature::getName);
    }


    // --- Tarkistukset ---
    private void configureValidation() {

        binder.forField(model)
                .asRequired("Syötä malli")
                .withValidator(text -> text.trim().length() >= 3,
                        "Mallin tulee olla vähintään 3 merkkiä")
                .bind(Scooter::getModel, Scooter::setModel);

        binder.forField(serialNumber)
                .asRequired("Syötä sarjanumero")
                .withValidator(text -> text.trim().length() >= 4,
                        "Sarjanumeron tulee olla vähintään 4 merkkiä")
                .bind(Scooter::getSerialNumber,
                        Scooter::setSerialNumber);

        binder.forField(batteryLevel)
                .asRequired("Syötä akunvaraustaso")
                .withValidator(number -> number >= 0 && number <= 100,
                        "Varaustaso tulee olla väliltä 0-100")
                .bind(Scooter::getBatteryLevel,
                        Scooter::setBatteryLevel);

        binder.forField(status)
                .asRequired("Valitse status")
                .bind(Scooter::getStatus,
                        Scooter::setStatus);

        binder.forField(manufactureYear)
                .asRequired("Syötä valmistusvuosi")
                .withValidator(number -> number >= 2015 && number <= 2026,
                        "Valmistusvuosi tulee olla väliltä 2015-2026")
                .bind(Scooter::getManufactureYear,
                        Scooter::setManufactureYear);

        binder.forField(station)
                .asRequired("Valitse asema")
                .bind(Scooter::getStation, Scooter::setStation);

        binder.forField(features)
                .bind(Scooter::getFeatures, Scooter::setFeatures);
    }


    public void setScooter(Scooter scooter) {
        this.scooter = scooter;

        // Päivittää kun form avataan
        station.setItems(stationService.findAll());
        features.setItems(featureService.findAll());

        if (scooter != null) {
            binder.readBean(scooter);
        } else {
            binder.readBean(new Scooter());
        }
    }

    public Scooter getScooter() {
        return scooter;
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

    public boolean writeScooter(){
        try {
            binder.writeBean(scooter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
