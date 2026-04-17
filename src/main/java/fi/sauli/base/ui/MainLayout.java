package fi.sauli.base.ui;

import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.SvgIcon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import fi.sauli.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;
import java.util.Optional;

@Layout
@AnonymousAllowed
public final class MainLayout extends AppLayout {

    private final transient AuthenticationContext authContext;
    private final UserService userService;

    // --- Konstruktor ---
    MainLayout(AuthenticationContext authContext,
               UserService userService) {
        this.authContext = authContext;
        this.userService = userService;

        addClassName("main-view");
        createNavbar();
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()));
        addToNavbar(createFooter());
    }

    // Dynaaminen Login/Logout button
    private Button createAuthButton() {
        boolean loggedIn = authContext.getAuthenticatedUser(Object.class).isPresent();

        if (loggedIn) {
            var logoutBtn = new Button(getTranslation("nav.button.logout"));
            logoutBtn.addClassName("logout-button");
            logoutBtn.getStyle().set("font-size", "small");
            logoutBtn.addClickListener(event ->
                    authContext.logout());
            return logoutBtn;
        } else {
            var loginBtn = new Button(getTranslation("nav.button.login"));
            loginBtn.getStyle().set("font-size", "small");
            loginBtn.addClickListener(event ->
                    loginBtn.getUI().ifPresent(ui -> ui.navigate("login"))
            );
            return loginBtn;
        }
    }

    // Rekisteröidy
    private Button createRegisterButton() {
        var  registerBtn = new Button(getTranslation("nav.button.register"));
        registerBtn.getStyle().set("font-size", "small");
        registerBtn.addClickListener(event ->
                registerBtn.getUI().ifPresent(ui -> ui.navigate("register"))
        );
        return registerBtn;
    }

    // Hakee käyttäjänimen
    private String getLoggedInUsername() {
        return userService.getCurrentUser().getUsername();
    }

    // Kielen vaihto
    private HorizontalLayout createLanguageSwitcher() {
        Button fiBtn = new Button("FI");
        Button enBtn = new Button("EN");
        fiBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);
        enBtn.addThemeVariants(ButtonVariant.LUMO_SMALL);

        // Aktiivinen kieli
        Locale currentLocale = VaadinSession.getCurrent().getLocale();
        if (currentLocale == null) {
            currentLocale = new Locale("fi");
        }
        if ("fi".equals(currentLocale.getLanguage())) {
            fiBtn.addClassName("selected");
        } else {
            enBtn.addClassName("selected");
        }

        fiBtn.addClickListener(event ->
                getUI().ifPresent(ui -> {
                    Locale locale = new Locale("fi");
                    ui.setLocale(locale);
                    VaadinSession.getCurrent().setLocale(locale);
                    ui.getPage().reload();
                })
        );
        enBtn.addClickListener(event ->
                getUI().ifPresent(ui -> {
                    Locale locale = Locale.ENGLISH;
                    ui.setLocale(locale);
                    VaadinSession.getCurrent().setLocale(locale);
                    ui.getPage().reload();
                })
        );

        HorizontalLayout languageLayout = new HorizontalLayout(fiBtn, enBtn);
        languageLayout.addClassName("language-switcher");
        languageLayout.setSpacing(true);
        return languageLayout;
    }

    // Navigaation kielenvaihto
    private String getMenuTranslationKey(MenuEntry menuEntry) {
        return switch (menuEntry.title()) {
            case "Etusivu" -> "sidenav.home";
            case "Potkulaudat" -> "sidenav.scooters";
            case "Lisätiedot" -> "sidenav.scooterdetails";
            case "Ominaisuudet" -> "sidenav.features";
            case "Ajot" -> "sidenav.rides";
            case "Asemat" -> "sidenav.stations";
            case "Profiili" -> "sidenav.profile";
            default -> null;
        };
    }

    // NAVBAR
    private void createNavbar() {
        var toggle = new DrawerToggle();
        var title = new Span("Scooter Fleet Management");
        title.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var languageSwitcher = createLanguageSwitcher();
        var authBtn = createAuthButton();

        Component authSection;
        if (authContext.isAuthenticated()) {
            var username = new Span(getTranslation("nav.greetings") + " " + getLoggedInUsername());
            username.addClassName("navbar-username");

            var authLayout = new HorizontalLayout(username, authBtn);
            authLayout.setPadding(false);
            authLayout.setSpacing(true);
            authLayout.setAlignItems(FlexComponent.Alignment.CENTER);
            authLayout.addClassName("navbar-auth");

            authSection = authLayout;
        } else {
            authSection = authBtn;
        }

        HorizontalLayout rightSide;

        if (authContext.isAuthenticated()) {
            rightSide = new HorizontalLayout(languageSwitcher, authSection);
        } else {
            rightSide = new HorizontalLayout(languageSwitcher, createRegisterButton(), authSection);
        }
        rightSide.setPadding(false);
        rightSide.setSpacing(true);
        rightSide.setAlignItems(FlexComponent.Alignment.CENTER);

        var header = new HorizontalLayout(toggle, title, rightSide);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.getStyle().set("padding", "0 16px");

        addToNavbar(header);
    }

    // HEADER
    private Component createHeader() {
        Image appLogo = new Image("images/logo3.png", "Scooter Management");
        appLogo.setHeight("200px");

        var appName = new Span("");
        appName.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var header = new VerticalLayout(appLogo, appName);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    // SIDENAV
    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(LumoUtility.Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        String key = getMenuTranslationKey(menuEntry);
        String title = key != null ? getTranslation(key) : menuEntry.title();

        if (menuEntry.icon() != null) {
            return new SideNavItem(title, menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(title, menuEntry.path());
        }
    }

    // FOOTER
    private Component createFooter() {

        // GitHub-linkki
        var githubLink = new Anchor(
                "https://github.com/Cyp6ty/vaadin-scooter-app",
                getTranslation("footer.github")
        );
        githubLink.setTarget("_blank");
        githubLink.addClassName("footer-link");

        // Tekstit
        var text1 = new Span("© 2026 Sauli Pitkäkangas");
        var text2 = new Span("Scooter Fleet Management");
        var text3 = new Span(getTranslation("footer.course"));
        var text4 = new Span(getTranslation("footer.practicework"));

        // Sarakkeet
        var col1 = new VerticalLayout(text1, text2);
        var col2 = new VerticalLayout(text3, text4);
        var col3 = new VerticalLayout(githubLink);
        col1.addClassName("footer-column");
        col2.addClassName("footer-column");
        col3.addClassName("footer-column");
        col1.setPadding(false);
        col1.setSpacing(false);
        col2.setPadding(false);
        col2.setSpacing(false);
        col3.setPadding(false);
        col3.setSpacing(false);

        // Sisältö
        var content = new HorizontalLayout(col1, col2, col3);
        content.addClassName("footer-content");
        content.setPadding(false);
        content.setSpacing(false);

        // Wrapper
        var wrapper = new HorizontalLayout(content);
        wrapper.addClassName("app-footer");
        wrapper.setWidthFull();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);

        return wrapper;
    }
}