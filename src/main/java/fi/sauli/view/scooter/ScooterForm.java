package fi.sauli.view.scooter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import fi.sauli.entity.Scooter;

public class ScooterForm extends FormLayout {

    // Kentät
    private TextField model = new TextField("Malli");
    private TextField serialNumber = new TextField("Sarjanumero");
    private IntegerField batteryLevel = new IntegerField("Akun varaustaso");
    private ComboBox<String> status = new ComboBox<>("Status");
    private IntegerField manufactureYear = new IntegerField("Valmistusvuosi");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<Scooter> binder = new Binder<>(Scooter.class);

    // --- Konstruktor ---
    public ScooterForm() {
        setWidth("25em");
        add(
                model,
                serialNumber,
                batteryLevel,
                status,
                manufactureYear,
                save,
                delete,
                cancel
        );

        // Statuksen valittavat tilat
        status.setItems("" +
                "AVAILABLE",
                "IN_USE",
                "MAINTENANCE",
                "OUT_OF_SERVICE"
        );
        status.setPlaceholder("Valitse status");
        status.setClearButtonVisible(true);

        // --- Tarkistukset ---
        // (ilmoitus käyttäjälle jos arvot väärin)
        binder.forField(model)
                .asRequired("Syötä malli")
                .withValidator(text -> text.trim().length() >= 5,
                        "Mallin tulee olla vähintään 5 merkkiä")
                .bind(Scooter::getModel,
                        Scooter::setModel);

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
    }

    private Scooter scooter;

    public void setScooter(Scooter scooter) {
        this.scooter = scooter;

        if (scooter != null) {
            // siirtää entityn formille
            binder.readBean(scooter);
        } else {
            // siirtää formin entitylle
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
