package fi.sauli.view.feature;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import fi.sauli.entity.Feature;

public class FeatureForm extends FormLayout {

    // Kentät
    private ComboBox<String> name = new ComboBox<>("Ominaisuus");
    private TextArea description = new TextArea("Kuvaus");
    private TextField category = new TextField("Kategoria");
    private ComboBox<Boolean> active = new ComboBox<>("Aktiivisuus");
    private NumberField installationCost = new NumberField("Asennuskustannus");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<Feature> binder = new Binder<>(Feature.class);

    // --- Konstruktor ---
    public FeatureForm() {
        setWidth("25em");
        add(
                name,
                description,
                category,
                active,
                installationCost,
                save,
                delete,
                cancel
        );

        // --- Valittavat kentät ---

        // Ominaisuuden nimi
        name.setItems(
                "GPS Tracking",
                "Alarm system",
                "NFC unlock",
                "LED Display",
                "Bluetooth Lock"
        );
        name.setPlaceholder("Valitse ominaisuus");
        name.setClearButtonVisible(true);

        active.setItems(
                true,
                false
        );
        active.setItemLabelGenerator(value -> value
                ? "Aktiivinen"
                : "Ei aktiivinen");
        active.setPlaceholder("Onko ominaisuus aktiivinen?");
        active.setClearButtonVisible(true);

        description.setHelperText("Annetaan automaattisesti tai voit muokata tekstiä");

        category.setReadOnly(true);
        category.setHelperText("Valitaan automaattisesti");


        // --- Automaattinen Kategoria ---
        name.addValueChangeListener(event -> {
            String selected = event.getValue();

            if (selected == null) {
                category.clear();
                description.clear();
                installationCost.clear();
                return;
            }

            switch (selected) {
                case "GPS Tracking" -> {
                    category.setValue("Navigation");
                    if (description.isEmpty()) {
                        description.setValue("Mahdollistaa potkulaudan sijainnin seurannan.");
                    }
                    if (installationCost.isEmpty()) {
                        installationCost.setValue(49.90);
                    }
                }
                case "Alarm system" -> {
                    category.setValue("Security");
                    if (description.isEmpty()) {
                        description.setValue("Hälytysjärjestelmä suojaa potkulautaa varkauksilta.");
                    }
                    if (installationCost.isEmpty()) {
                        installationCost.setValue(59.90);
                    }
                }
                case "NFC unlock" -> {
                    category.setValue("Access");
                    if (description.isEmpty()) {
                        description.setValue("Potkulaudan lukitus voidaan avata NFC-toiminnolla.");
                    }
                    if (installationCost.isEmpty()) {
                        installationCost.setValue(39.90);
                    }
                }
                case "LED Display" -> {
                    category.setValue("Display");
                    if (description.isEmpty()) {
                        description.setValue("Näyttää ajotiedot LED-näytöllä.");
                    }
                    if (installationCost.isEmpty()) {
                        installationCost.setValue(19.90);
                    }
                }
                case "Bluetooth Lock" -> {
                    category.setValue("Security");
                    if (description.isEmpty()) {
                        description.setValue("Lukitus toimii Bluetooth-yhteyden avulla.");
                    }
                    if (installationCost.isEmpty()) {
                        installationCost.setValue(29.90);
                    }
                }
            }
        });


        // --- Tarkistukset ---
        // (ilmoitus käyttäjälle jos arvot väärin)
        binder.forField(name)
                .asRequired("Syötä ominaisuus")
                .bind(Feature::getName,
                        Feature::setName);

        binder.forField(description)
                .asRequired("Ominaisuudelle ei löytynyt kuvausta")
                .bind(Feature::getDescription,
                        Feature::setDescription);

        binder.forField(category)
                .asRequired("Ominaisuudelle ei löytynyt kategoriaa")
                .bind(Feature::getCategory,
                        Feature::setCategory);

        binder.forField(active)
                .asRequired("Valitse aktiivisuus")
                .bind(Feature::getActive,
                        Feature::setActive);

        binder.forField(installationCost)
                .asRequired("Syötä asennuskustannus")
                .withValidator(value -> value != null && value >= 0,
                        "Asennuskustannus ei voi olla negatiivinen")
                .bind(Feature::getInstallationCost,
                        Feature::setInstallationCost);

    }

    private Feature feature;

    public void setFeature(Feature feature) {
        this.feature = feature;

        if (feature != null) {
            // siirtää entityn formille
            binder.readBean(feature);
        } else {
            // siirtää formin entitylle
            binder.readBean(new Feature());
        }
    }

    public Feature getFeature() {
        return feature;
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

    public boolean writeFeature() {
        try {
            binder.writeBean(feature);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
