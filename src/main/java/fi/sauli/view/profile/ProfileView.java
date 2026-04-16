package fi.sauli.view.profile;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fi.sauli.base.ui.MainLayout;
import fi.sauli.service.UserService;
import fi.sauli.user.AppUser;
import jakarta.annotation.security.PermitAll;
import java.nio.file.Path;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;


@Route(value = "profile", layout = MainLayout.class)
@Menu(title = "Profiili", icon = "user")
@PageTitle("Profiili")
@PermitAll
public class ProfileView extends VerticalLayout {

    private final UserService userService;
    private final Image profileImage = new Image();

    // --- Konstruktor ---
    // Luo upload-komponentin ja käsittelee kuvan tallennuksen ja näyttämisen
    public ProfileView(UserService userService) {
        this.userService = userService;

        setAlignItems(Alignment.CENTER);
        setSpacing(true);
        H2 title = new H2("Oma Profiili");

        Upload upload = new Upload();
        MemoryBuffer buffer = new MemoryBuffer();
        upload.setReceiver(buffer);
        upload.setAcceptedFileTypes("image/png", "image/jpeg", "image/jpg", "image/gif");
        upload.setMaxFiles(1);

        profileImage.setAlt("Profiilikuva");
        profileImage.setWidth("200px");
        profileImage.setHeight("200px");
        profileImage.getStyle().set("border-radius", "50%");
        profileImage.getStyle().set("object-fit", "cover");
        profileImage.setVisible(false);

        loadExistingProfileImage();

        // Tallentaa ladatun kuvan tiedostoon ja päivittää käyttäjän profiilikuvan
        upload.addSucceededListener(event -> {
            try {
                AppUser user = userService.getCurrentUser();

                String originalFileName = event.getFileName();
                String extension = getFileExtension(originalFileName);
                String savedFileName = "user-" + user.getId() + extension;

                Path uploadDir = Path.of("uploaded-profile-images");
                Files.createDirectories(uploadDir);
                Path filePath = uploadDir.resolve(savedFileName);

                try (InputStream inputStream = buffer.getInputStream()) {
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }

                String imageUrl = "/profile-images/" + savedFileName;
                userService.saveProfileImagePath(imageUrl);
                profileImage.setSrc(imageUrl);
                profileImage.setVisible(true);
                Notification.show("Profiilikuva ladattu onnistuneesti");

            } catch (IOException e) {
                Notification.show("Kuvan tallennus epäonnistui");
            }
        });

        upload.addFileRejectedListener(event ->
                Notification.show("Vain kuvatiedostot ovat sallittuja"));
        add(title, profileImage, upload);
    }

    // Lataa ja näyttää aijemmin tallennetun kuvan
    private void loadExistingProfileImage() {
        AppUser user = userService.getCurrentUser();

        if (user.getProfileImagePath() != null && !user.getProfileImagePath().isBlank()) {
            profileImage.setSrc(user.getProfileImagePath());
            profileImage.setVisible(true);
        }
    }

    // Palauttaa kuvatiedoston tiedostopäätteen
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");

        if (dotIndex == -1) {
            return ".png";
        }
        return fileName.substring(dotIndex);
    }
}