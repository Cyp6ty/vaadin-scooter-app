package fi.sauli.view.ride;

import com.vaadin.flow.component.button.ButtonVariant;
import fi.sauli.entity.Ride;
import fi.sauli.entity.Scooter;
import fi.sauli.entity.Station;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;

import java.time.LocalDateTime;
import java.util.List;


public class RideForm extends FormLayout {

    // Kentät
    private DateTimePicker startTime = new DateTimePicker("Aloitusaika");
    private DateTimePicker endTime = new DateTimePicker("Lopetusaika");
    private NumberField distanceKm = new NumberField("Kilomerimäärä");
    private NumberField price = new NumberField("Hinta");
    private ComboBox<String> status = new ComboBox("Status");
    private ComboBox<Scooter> scooter = new ComboBox<>("Potkulauta");
    private ComboBox<Station> startStation = new ComboBox<>("Lähtöasema");
    private ComboBox<Station> endStation = new ComboBox<>("Pääteasema");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<Ride> binder = new Binder<>(Ride.class);
    private Ride ride;

    // --- Konstruktor ---
    public RideForm() {
        setWidth("25em");
        add(
                startTime,
                endTime,
                distanceKm,
                status,
                price,
                scooter,
                startStation,
                endStation,
                save,
                delete,
                cancel
        );
        // Tyylit ja ulkoasu - Tehtävä 2-C
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        // Kenttien asetukset
        startTime.setStep(java.time.Duration.ofMinutes(1));
        endTime.setStep(java.time.Duration.ofMinutes(1));

        distanceKm.setMin(0.1);
        distanceKm.setStep(0.1);

        price.setMin(0);
        price.setStep(0.1);

        status.setItems("" +
                "COMPLETED",
                "CANCELLED",
                "FAILED",
                "INTERRUPTED");
        status.setPlaceholder("Valitse ajon status");

        scooter.setPlaceholder("Valitse potkulauta");
        scooter.setClearButtonVisible(true);
        scooter.setItemLabelGenerator(s ->
                s.getSerialNumber() + " - " + s.getModel());

        startStation.setPlaceholder("Valitse lähtöasema");
        startStation.setClearButtonVisible(true);
        startStation.setItemLabelGenerator(k ->
                k.getCity() + " - " + k.getName());

        endStation.setPlaceholder("Valitse pääteasema");
        endStation.setClearButtonVisible(true);
        endStation.setItemLabelGenerator(k ->
                k.getCity() + " - " + k.getName());


        // --- Tarkistukset ---
        // (ilmoitus käyttäjälle jos arvot väärin)
        binder.forField(startTime)
                .asRequired("Syötä aloitusaika")
                .withValidator(value -> value == null || !value.isAfter(LocalDateTime.now()),
                        "Aloitusaika ei voi olla tulevaisuudessa")
                .bind(Ride::getStartTime,
                        Ride::setStartTime);

        binder.forField(endTime)
                .asRequired("Syötä lopetusaika")
                .withValidator(value -> value == null || !value.isAfter(LocalDateTime.now()),
                        "Lopetusaika ei voi olla tulevaisuudessa")
                .withValidator(end -> {
                            LocalDateTime start = startTime.getValue();
                            return end == null || start == null || !end.isBefore(start);
                        }, "Lopetusaika ei voi olla ennen aloitusaikaa")
                .bind(Ride::getEndTime,
                        Ride::setEndTime);

        binder.forField(distanceKm)
                .asRequired("Syötä kilometrimäärä")
                .withValidator(value -> value == null || value > 0,
                        "Kilometrimäärän tulee olla positiivinen")
                .bind(Ride::getDistanceKm,
                        Ride::setDistanceKm);

        binder.forField(price)
                .asRequired("Syötä hinta")
                .withValidator(value -> value == null || value > 0,
                        "Hinta tulee olla enemmäin kuin nolla")
                .bind(Ride::getPrice,
                        Ride::setPrice);

        binder.forField(status)
                .asRequired("Valitse status")
                .bind(Ride::getStatus,
                        Ride::setStatus);

        binder.forField(scooter)
                .asRequired("Valitse potkulauta")
                .bind(Ride::getScooter,
                        Ride::setScooter);

        binder.forField(startStation)
                .asRequired("Valitse lähtöasema")
                .bind(Ride::getStartStation,
                        Ride::setStartStation);

        binder.forField(endStation)
                .asRequired("Valitse pääteasema")
                .bind(Ride::getEndStation,
                        Ride::setEndStation);
    }


    public void setScooters(List<Scooter> scooters) {
        scooter.setItems(scooters);
    }

    public void setStations(List<Station> stations) {
        startStation.setItems(stations);
        endStation.setItems(stations);
    }

    public void setRide(Ride ride) {
        this.ride = ride;

        if (ride != null) {
            binder.readBean(ride);
        } else {
            binder.readBean(new Ride());
        }
    }

    public Ride getRide() {
        return ride;
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

    public boolean writeRide() {
        try {
            binder.writeBean(ride);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
