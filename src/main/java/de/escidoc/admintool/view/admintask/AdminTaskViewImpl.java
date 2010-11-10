package de.escidoc.admintool.view.admintask;

import com.google.common.base.Preconditions;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;

import de.escidoc.admintool.service.ServiceContainer;
import de.escidoc.admintool.view.ViewConstants;
import de.escidoc.admintool.view.admintask.Style.H1;
import de.escidoc.admintool.view.admintask.Style.H2;
import de.escidoc.admintool.view.admintask.Style.Ruler;

public class AdminTaskViewImpl extends CustomComponent implements AdminTaskView {

    private static final long serialVersionUID = -8816703708910119233L;

    private final CssLayout margin = new CssLayout();

    private Label text = new Label(ViewConstants.LOAD_EXAMPLE_TITLE,
        Label.CONTENT_XHTML);

    private final VerticalLayout mainLayout = new VerticalLayout();

    private final Window mainWindow;

    private final ServiceContainer services;

    public AdminTaskViewImpl(final Window mainWindow,
        final ServiceContainer services) {
        checkForNull(mainWindow, services);
        this.mainWindow = mainWindow;
        this.services = services;
        init();
    }

    private void checkForNull(
        final Window mainWindow, final ServiceContainer services) {
        Preconditions.checkNotNull(mainWindow,
            "mainWindow can not be null. %s", mainWindow);
        Preconditions.checkNotNull(services, "services can not be null. %s",
            services);
    }

    private void init() {
        setCompositionRoot(layout());
    }

    private VerticalLayout layout() {
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);
        mainLayout.setCaption("Welcome");
        mainLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        margin.setMargin(true);
        margin.setWidth("100%");
        mainLayout.addComponent(margin);

        final H1 title = new H1(ViewConstants.ADMIN_TASK_VIEW_TITLE);
        margin.addComponent(title);
        margin.addComponent(new Ruler());

        final HorizontalLayout texts = new HorizontalLayout();
        texts.setSpacing(true);
        texts.setWidth("100%");
        texts.setMargin(false, false, true, false);
        margin.addComponent(texts);

        addLoadExampleViewLab();
        addFilteringResourceView();
        addReindexResourceView();
        addRepoInfoView();
        return mainLayout;
    }

    private void addReindexResourceView() {
        text = new H2(ViewConstants.REINDEX_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        margin.addComponent(text);
        margin.addComponent(new Ruler());

        text = new Label(ViewConstants.REINDEX_TEXT, Label.CONTENT_XHTML);
        margin.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");
        final ReindexResourceView reindexResourceView =
            new ReindexResourceViewImpl(services.getAdminService(), mainWindow);
        hLayout.addComponent(reindexResourceView);

        margin.addComponent(hLayout);
    }

    private void addLoadExampleViewLab() {
        text = new H2(ViewConstants.LOAD_EXAMPLES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);
        margin.addComponent(text);
        margin.addComponent(new Ruler());
        text =
            new Label(
                "<p>Loads a set of example objects into the framework.</p>",
                Label.CONTENT_XHTML);
        margin.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");

        final Component loadExampleView =
            new LoadExampleViewImpl(mainWindow, services.getAdminService());
        hLayout.addComponent(loadExampleView);
        margin.addComponent(hLayout);
    }

    private void addFilteringResourceView() {
        text = new H2(ViewConstants.FILTERING_RESOURCES_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        margin.addComponent(text);
        margin.addComponent(new Ruler());

        text = new Label(ViewConstants.FILTER_TEXT, Label.CONTENT_XHTML);
        margin.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight(500, UNITS_PIXELS);

        final FilterResourceListener listener =
            new FilterResourceListener(mainWindow, services);
        final FilterResourceView filterResourceView =
            new FilterResourceView(listener, services.getAdminService(),
                mainWindow);
        hLayout.addComponent(filterResourceView);

        margin.addComponent(hLayout);
    }

    private void addRepoInfoView() {
        text = new H2(ViewConstants.REPOSITORY_INFORMATION_TITLE);
        text.setContentMode(Label.CONTENT_XHTML);

        margin.addComponent(text);
        margin.addComponent(new Ruler());

        text = new Label(ViewConstants.REPO_INFO_TEXT, Label.CONTENT_XHTML);
        margin.addComponent(text);

        final HorizontalLayout hLayout = new HorizontalLayout();
        hLayout.setWidth("100%");
        hLayout.setHeight("250px");
        margin.addComponent(hLayout);

        final RepoInfoClickListener listener =
            new RepoInfoClickListener(mainWindow, services.getAdminService());
        final RepositoryInfoView repositoryInfoView =
            new RepositoryInfoView(listener);

        hLayout.addComponent(repositoryInfoView);
    }
}