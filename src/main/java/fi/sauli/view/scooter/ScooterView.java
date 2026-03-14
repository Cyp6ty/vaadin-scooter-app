package fi.sauli.view.scooter;

import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.entity.Scooter;
import fi.sauli.service.ScooterService;
import com.vaadin.flow.component.notification.Notification;


//@Route("scooters")
@Route(value = "scooters", layout = MainLayout.class)
@Menu(title = "Potkulaudat")
public class ScooterView extends VerticalLayout {

     private final ScooterService scooterService;

     private Grid<Scooter> grid =  new Grid<>(Scooter.class, false);
     private ScooterForm form = new ScooterForm();
     private Button addNewButton = new Button("Lisää uusi potkulauta");

     public ScooterView(ScooterService scooterService) {
         this.scooterService = scooterService;

         configureGrid();
         configureForm();
         add(addNewButton);

         HorizontalLayout content = new HorizontalLayout(grid, form);
         content.setSizeFull();
         add(content);

         setSizeFull();
         updateList();
         closeEditor();

         addNewButton.addClickListener(click -> addScooter());
     }

     // Määrittää listan
     private void configureGrid() {
         grid.addColumn(Scooter::getId).setHeader("ID");
         grid.addColumn(Scooter::getSerialNumber).setHeader("Sarjanumero");
         grid.addColumn(Scooter::getModel).setHeader("Malli");
         grid.addColumn(Scooter::getBatteryLevel).setHeader("Akun varaustaso");
         grid.addColumn(Scooter::getStatus).setHeader("Status");
         grid.addColumn(Scooter::getManufactureYear).setHeader("Valmistusvuosi");

         grid.setSizeFull();
         grid.asSingleSelect().addValueChangeListener(event -> editScooter(event.getValue()));
     }

     // Määrittää buttonit
     private void configureForm() {
         form.setWidth("25em");

         form.getSaveButton().addClickListener(event -> saveScooter());
         form.getDeleteButton().addClickListener(event -> deleteScooter());
         form.getCancelButton().addClickListener(event -> closeEditor());
     }

     // Hakee tiedot tietokannasta
     private void updateList() {
         grid.setItems(scooterService.findAll());
     }

     // Lisää uuden
     private void addScooter() {
         grid.asSingleSelect().clear();
         form.setScooter(new Scooter());
         form.setVisible(true);
     }

     // Avaa valitun muokattavaksi
     private void editScooter(Scooter scooter) {
        if (scooter == null) {
             closeEditor();
        } else {
            form.setScooter(scooter);
            form.setVisible(true);
        }
     }

     // Tallentaa muutokset
     private void saveScooter() {
         Scooter scooter = form.getScooter();

         if (scooter != null && form.writeScooter()) {
             scooterService.save(scooter);
             updateList();
             closeEditor();
             Notification.show("Uusi potkulauta tallennettu");
         }
     }

     // Poistaa valitun
     private void deleteScooter() {
         Scooter scooter = form.getScooter();

         if (scooter != null && scooter.getId() != null) {
             scooterService.delete(scooter);
             updateList();
             closeEditor();
             Notification.show("Potkulauta poistettu");
         }
     }

     // Sulkee ja siivoaa formin
    private void closeEditor() {
        form.setScooter(null);
        form.setVisible(false);
    }
}
