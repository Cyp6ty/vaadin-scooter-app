package fi.sauli.view.scooter;

import com.vaadin.flow.component.button.Button;
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
    private TextField status = new TextField("Status");
    private IntegerField manufactureYear = new IntegerField("Valmistusvuosi");

    // Buttonit
    private Button save = new Button("Tallenna");
    private Button delete = new Button("Poista");
    private Button cancel = new Button("Peruuta");

    private Binder<Scooter> binder = new Binder<>(Scooter.class);

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

        binder.bindInstanceFields(this);
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
