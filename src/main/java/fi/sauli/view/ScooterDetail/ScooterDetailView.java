package fi.sauli.view.ScooterDetail;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.ScooterDetail;
import fi.sauli.service.ScooterDetailService;
import fi.sauli.service.ScooterService;


@Route(value = "scooterdetails", layout = MainLayout.class)
@Menu(title = "Lisätiedot")
public class ScooterDetailView extends VerticalLayout {

    private final ScooterDetailService scooterDetailService;

    private Grid<ScooterDetail> grid = new Grid<>(ScooterDetail.class, false);
    private ScooterDetailForm form;
    private Button addNewButton = new Button("Lisää uusi lisätieto");

    public ScooterDetailView(ScooterDetailService scooterDetailService,
                             ScooterService scooterService) {

        this.scooterDetailService = scooterDetailService;
        this.form = new ScooterDetailForm(scooterService);

        configureGrid();
        configureForm();
        add(addNewButton);

        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setSizeFull();
        add(content);

        setSizeFull();
        updateList();
        closeEditor();

        addNewButton.addClickListener(click -> addDetail());
    }

    // Määrittää listan
    private void configureGrid() {
        grid.addColumn(ScooterDetail::getId).setHeader("ID");
        grid.addColumn(ScooterDetail::getFirmwareVersion).setHeader("Ohjelmistoversio");
        grid.addColumn(ScooterDetail::getMaxSpeed).setHeader("Maksimi nopeus");
        grid.addColumn(ScooterDetail::getLastInspectionDate).setHeader("Viim. tarkastuspäivä");
        grid.addColumn(ScooterDetail::getQrCode).setHeader("QR-koodi");
        grid.addColumn(ScooterDetail::getWeight).setHeader("Paino (kg)");

        grid.addColumn(detail ->
                        detail.getScooter() != null
                                ? detail.getScooter().getSerialNumber() + " - " + detail.getScooter().getModel()
                                : "")
                        .setHeader("Potkulauta");

        grid.setSizeFull();
        grid.asSingleSelect().addValueChangeListener(
                event -> editDetail(event.getValue()));
    }

    // Määrittää buttonit
    private void configureForm() {
        form.setWidth("25em");

        form.getSaveButton().addClickListener(event -> saveDetail());
        form.getDeleteButton().addClickListener(event -> deleteDetail());
        form.getCancelButton().addClickListener(event -> closeEditor());
    }

    // Hakee tiedot tietokannasta
    private void updateList() {
        grid.setItems(scooterDetailService.findAll());
    }

    // Lisää uuden
    private void addDetail() {
        grid.asSingleSelect().clear();
        form.setScooterDetail(new ScooterDetail());
        form.setVisible(true);
    }

    // Avaa valitun muokattavaksi
    private void editDetail(ScooterDetail scooterDetail) {
        if (scooterDetail == null) {
            closeEditor();
        } else {
            form.setScooterDetail(scooterDetail);
            form.setVisible(true);
        }
    }

    // Tallentaa muutokset
    private void saveDetail() {
        ScooterDetail detail = form.getScooterDetail();

        if (detail != null && form.writeScooterDetail()) {
            scooterDetailService.save(detail);
            updateList();
            closeEditor();
            Notification.show("Uusi lisätieto tallennettu");
        }
    }

    // Poistaa valitun
    private void deleteDetail() {
        ScooterDetail detail = form.getScooterDetail();

        if (detail != null && detail.getId() != null) {
            scooterDetailService.delete(detail);
            updateList();
            closeEditor();
            Notification.show("Lisätieto poistettu");
        }
    }

    // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setScooterDetail(null);
        form.setVisible(false);
    }

}
