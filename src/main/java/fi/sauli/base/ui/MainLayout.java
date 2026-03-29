package fi.sauli.base.ui;

import com.vaadin.flow.component.applayout.DrawerToggle;
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
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        createNavbar();
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()));
        addToNavbar(createFooter());
    }

    private void createNavbar() {
        var toggle = new DrawerToggle();

        var title = new Span("Scooter Fleet Management");
        title.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var user = new Span("Käyttäjä"); // vai placeholder?
        var logout = new com.vaadin.flow.component.button.Button("Kirjaudu ulos");
        logout.getStyle().set("font-size", "small");

        var header = new HorizontalLayout(toggle, title, user, logout);
        header.setWidthFull();
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        header.expand(title);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.getStyle().set("padding", "0 16px");

        addToNavbar(header);
    }

    private Component createHeader() {
        var appLogo = new Image("images/logo3.png", "Scooter Management");
        appLogo.setHeight("200px");

        var appName = new Span("");
        appName.getStyle().setFontWeight(Style.FontWeight.BOLD);

        var header = new VerticalLayout(appLogo, appName);
        header.setAlignItems(FlexComponent.Alignment.CENTER);
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.addClassNames(LumoUtility.Margin.Horizontal.MEDIUM);
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }

    // FOOTER
    private Component createFooter() {

        // GitHub-linkki
        var githubLink = new Anchor(
                "https://github.com/Cyp6ty/vaadin-scooter-app",
                "GitHub-projekti"
        );
        githubLink.setTarget("_blank");
        githubLink.addClassName("footer-link");

        // Tekstit
        var text1 = new Span("© 2026 Sauli Pitkäkangas");
        var text2 = new Span("Scooter Fleet Management");
        var text3 = new Span("Kurssi: Java Web-ohjelmointi");
        var text4 = new Span("Vaadin harjoitustyö");

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
