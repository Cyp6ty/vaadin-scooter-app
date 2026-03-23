package fi.sauli.view.ScooterDetail;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import fi.sauli.entity.Scooter;
import fi.sauli.entity.ScooterDetail;
import fi.sauli.service.ScooterService;


public class ScooterDetailForm extends FormLayout {

    // Kentät
    private TextField firmwareVersion = new TextField("Ohjelmisto versio");
    private IntegerField maxSpeed = new IntegerField("Maximi nopeus");
    private DatePicker lastInspectionDate = new DatePicker("Viimeisin tarkastus päivä");
    private TextField qrCode = new TextField("QR-koodi");
    private NumberField weight = new NumberField ("Paino (kg)");

    // Relaatio kenttä
    private ComboBox<Scooter> scooter = new ComboBox<>("Potkulauta");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<ScooterDetail> binder = new Binder<>(ScooterDetail.class);

    private final ScooterService scooterService;

    private ScooterDetail scooterDetail;


    // --- Konstruktor ---
    public ScooterDetailForm(ScooterService scooterService) {
        this.scooterService = scooterService;

        setWidth("25em");
        add(
                firmwareVersion,
                maxSpeed,
                lastInspectionDate,
                qrCode,
                weight,
                scooter,
                save,
                delete,
                cancel
        );

        configureFields();
        configureValidation();
    }

    // Asettaa ComBoxin sisällön
    private void configureFields() {
        scooter.setItems(scooterService.findAll());
        scooter.setItemLabelGenerator(s ->
                s.getSerialNumber() + " - " + s.getModel());
        scooter.setPlaceholder("Valitse potkulauta");
        scooter.setClearButtonVisible(true);
    }

    // --- Tarkistukset ---
    private void configureValidation() {

        binder.forField(firmwareVersion)
                .asRequired("Syötä ohjelmistoverio")
                .withValidator(text -> text.trim().length() >= 5,
                        "Ohjelmistoversion tulee olla vähintään 5 merkkiä")
                .bind(ScooterDetail::getFirmwareVersion,
                        ScooterDetail::setFirmwareVersion);

        binder.forField(maxSpeed)
                .asRequired("Syötä maksiminopeus")
                .withValidator(speed -> speed >= 1 && speed <= 50,
                        "Nopeuden tulee olla väliltä 1–50")
                .bind(ScooterDetail::getMaxSpeed,
                        ScooterDetail::setMaxSpeed);

        binder.forField(lastInspectionDate)
                .asRequired("Syötä päivämäärä")
                .withValidator(date -> !date.isAfter(java.time.LocalDate.now()),
                        "Päivämäärä ei voi olla tulevaisuudessa")
                .bind(ScooterDetail::getLastInspectionDate,
                        ScooterDetail::setLastInspectionDate);

        binder.forField(qrCode)
                .asRequired("Syötä QR-koodi")
                .withValidator(code -> code.trim().length() >= 5,
                        "QR-koodin pitää olla vähintään 5 merkkiä")
                .bind(ScooterDetail::getQrCode,
                        ScooterDetail::setQrCode);

        binder.forField(weight)
                .asRequired("Syötä paino")
                .withValidator(w -> w > 0,
                        "Paino ei voi olla nolla tai pienempi")
                .bind(ScooterDetail::getWeight,
                        ScooterDetail::setWeight);

        binder.forField(scooter)
                .asRequired("Valitse potkulauta")
                .bind(ScooterDetail::getScooter,
                        ScooterDetail::setScooter);
    }


    public void setScooterDetail(ScooterDetail scooterDetail) {
        this.scooterDetail = scooterDetail;

        // Päivittää valinnat kun form avataan
        scooter.setItems(scooterService.findAll());

        if (scooterDetail != null) {
            binder.readBean(scooterDetail);
        } else {
            binder.readBean(new ScooterDetail());
        }
    }

    public ScooterDetail getScooterDetail() {
        return scooterDetail;
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

    public boolean writeScooterDetail() {
        try {
            binder.writeBean(scooterDetail);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
