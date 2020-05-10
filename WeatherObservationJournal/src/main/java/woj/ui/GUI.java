package woj.ui;

import java.io.FileInputStream;
import woj.domain.*;
import woj.dao.*;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

/**
 * Graphical user interface for Weather Observation Journal application.
 */
public class GUI extends Application {

    private JournalService service;

    private Stage window;

    private Scene logInScene;
    private Scene sitesScene;
    private Scene createUserScene;
    private Scene createSiteScene;
    private Scene createObservationScene;

    private final int windowHeight = 700;
    private final int windowWidth = 1000;

    private Site selectedSite;

    private boolean propertiesOK;

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        Properties properties = new Properties();
        String dbUrl = "jdbc:sqlite:woj.db";
        try {
            properties.load(new FileInputStream("config.properties"));
            if (!properties.getProperty("database").equals("wojTest.db") && !properties.getProperty("database").isBlank()) {
                propertiesOK = true;
                dbUrl = "jdbc:sqlite:" + properties.getProperty("database");
            }
        } catch (Exception e) {
            propertiesOK = false;
        }

        UserDao userDao = new SQLiteUserDao(dbUrl);
        SiteDao siteDao = new SQLiteSiteDao(dbUrl);
        ObservationDao observationDao = new SQLiteObservationDao(dbUrl);
        this.service = new JournalService(userDao, siteDao, observationDao);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws Exception {
        if (!propertiesOK) {
            PopUpBox.show("Error in configuration", "Problem with config.properties detected. Default configuration will be used.");
        }
        window = stage;

        createLogInScene();
        createNewUserScene();
        window.setScene(logInScene);

        stage.setScene(logInScene);
        stage.setHeight(windowHeight);
        stage.setWidth(windowWidth);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createLogInScene() {
        Label header = createWojHeader();

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        Button loginButton = createButton("Log in", Color.LIGHTBLUE, true);
        loginButton.setOnAction(e -> login(usernameField));

        Region region1 = new Region();
        region1.setMinHeight(10);
        Region region2 = new Region();
        region2.setMinHeight(60);

        Button createNewUserButton = createButton("+ Create new user", Color.LIGHTGREEN, true);
        createNewUserButton.setOnAction(e -> {
            usernameField.clear();
            window.setScene(createUserScene);
        });

        Button exitButton = createButton("Exit", Color.LIGHTGREY, true);
        exitButton.setOnAction(e -> {
            service.logout();
            window.close();
        });

        VBox centerLayout = new VBox(15);
        centerLayout.setMaxSize(windowWidth / 3.0, windowHeight / 1.8);
        setBackgroundFill(centerLayout, new Color(1, 1, 1, 0.5), 10, -20);
        centerLayout.getChildren().addAll(header, region1, usernameField, loginButton, createNewUserButton, region2, exitButton);

        BorderPane loginLayout = new BorderPane();
        loginLayout.setCenter(centerLayout);
        BorderPane.setAlignment(centerLayout, Pos.CENTER);
        setBackground(loginLayout);

        logInScene = new Scene(loginLayout);

        //In order to prevent autofocus to username field
        loginLayout.requestFocus();
    }

    private void login(TextField field) {
        String input = field.getText();
        field.clear();
        if (input.isBlank()) {
            PopUpBox.show("Failed to log in", "Please enter username.");
            return;
        }
        if (service.login(input)) {
            createSitesScene();
            window.setScene(sitesScene);
            PopUpBox.show("Logging in successful", "Logged in as " + service.getLoggedUser().getName() + " (" + input + ").");
            return;
        }
        PopUpBox.show("Failed to log in", "Please use existing username.");
    }

    private void createNewUserScene() {
        Label header = createWojHeader();

        Region region1 = new Region();
        region1.setMinHeight(10);
        Region region2 = new Region();
        region2.setMinHeight(60);

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter new username");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter actual name of the user");

        Button createUserButton = createButton("+ Create new user", Color.LIGHTGREEN, true);
        createUserButton.setOnAction(e -> createUser(usernameField, nameField));

        Button cancelButton = createButton("Cancel", Color.LIGHTCORAL, true);
        cancelButton.setOnAction(e -> {
            usernameField.clear();
            nameField.clear();
            window.setScene(logInScene);
        });

        VBox centerLayout = new VBox(15);
        centerLayout.setMaxSize(windowWidth / 3.0, windowHeight / 1.8);
        setBackgroundFill(centerLayout, new Color(1, 1, 1, 0.5), 10, -20);
        centerLayout.getChildren().addAll(header, region1, usernameField, nameField, createUserButton, region2, cancelButton);

        BorderPane createUserLayout = new BorderPane();
        createUserLayout.setCenter(centerLayout);
        BorderPane.setAlignment(centerLayout, Pos.CENTER);
        setBackground(createUserLayout);

        createUserScene = new Scene(createUserLayout);

        //Disable autofocus to the first textfield.
        createUserLayout.requestFocus();
    }

    private void createUser(TextField usernameField, TextField nameField) {
        String username = usernameField.getText();
        String name = nameField.getText();

        if (username.length() < 2) {
            usernameField.clear();
            PopUpBox.show("Failed to create user", "Please enter username that is at least 2 characters long.");
            return;
        }
        if (name.isBlank()) {
            PopUpBox.show("Failed to create user", "Please enter actual name of the user.");
            return;
        }
        if (service.createUser(username, name)) {
            usernameField.clear();
            nameField.clear();
            window.setScene(logInScene);
            PopUpBox.show("User created succesfully", "User " + username + " (" + name + ") created.");
            return;
        }
        usernameField.clear();
        PopUpBox.show("Failed to create user", "User " + username + " (" + name + ") cannot be created.\nPlease ensure that you are using unique username.");
    }

    private void createSitesScene() {
        selectedSite = null;
        ComboBox<String> dropdownMenu = new ComboBox<>();
        dropdownMenu.getItems().addAll(
                "Log out",
                "About",
                "Exit");
        dropdownMenu.setPromptText("≡");
        dropdownMenu.setStyle(" -fx-background-color:LIGHTGREY");
        dropdownMenu.setOnAction(e -> performMenuAction(dropdownMenu));

        Region region = new Region();
        region.setMinHeight(50);

        ListView sitesList = new ListView();
        sitesList.getItems().addAll(service.getSitesOfLoggedUser());

        sitesList.setCellFactory(new Callback<ListView<Site>, ListCell<Site>>() {
            @Override
            public ListCell<Site> call(ListView<Site> p) {
                ListCell<Site> cell = new ListCell<Site>() {
                    @Override
                    protected void updateItem(Site s, boolean b) {
                        super.updateItem(s, b);
                        if (s != null) {
                            setText(getSiteAsText(s));
                        }
                    }
                };
                return cell;
            }
        });

        Label selectedSiteLabel = new Label("Selected site: No site selected.");
        selectedSiteLabel.setMinWidth(420);
        selectedSiteLabel.setMaxWidth(420);
        ChartViewer chartViewer = new ChartViewer(createObservationsChart());
        TableView tableView = new TableView();

        Button chooseSiteButton = createButton("Choose site", Color.LIGHTBLUE, true);

        Button createSiteButton = createButton("+ Create new site", Color.LIGHTGREEN, true);
        createSiteButton.setOnAction(e -> {
            createNewSiteScene();
            window.setScene(createSiteScene);
        });

        VBox sitesPane = new VBox(15);
        sitesPane.setPadding(new Insets(20));
        setBackgroundFill(sitesPane, new Color(1, 1, 1, 0.5), 10, 0);
        sitesPane.getChildren().addAll(dropdownMenu, sitesList, chooseSiteButton, region, createSiteButton);

        Button createObservationButton = createButton("+ Create observation", Color.LIGHTGREEN, false);
        createObservationButton.setMaxWidth(260);
        createObservationButton.setOnAction(e -> {
            if (selectedSite == null) {
                PopUpBox.show("Failed to create observation", "Please select site first.");
                return;
            };
            createNewObservationScene();
            window.setScene(createObservationScene);
        });

        Button changeViewButton = createButton("Change to table", Color.LIGHTGREY, false);

        HBox siteHeader = new HBox(20);
        siteHeader.setPadding(new Insets(5, 20, 20, 20));
        siteHeader.setAlignment(Pos.CENTER);
        siteHeader.getChildren().addAll(selectedSiteLabel, changeViewButton);

        BorderPane siteSpecificPane = new BorderPane();
        siteSpecificPane.setTop(siteHeader);
        siteSpecificPane.setCenter(chartViewer);
        siteSpecificPane.setBottom(createObservationButton);
        setBackgroundFill(siteSpecificPane, new Color(1, 1, 1, 0.5), 10, 0);
        siteSpecificPane.setPadding(new Insets(20));
        BorderPane.setMargin(selectedSiteLabel, new Insets(10));
        BorderPane.setMargin(tableView, new Insets(10));
        BorderPane.setMargin(chartViewer, new Insets(10));
        BorderPane.setMargin(createObservationButton, new Insets(10));

        changeViewButton.setOnAction(e -> changeView(siteSpecificPane, chartViewer, tableView, changeViewButton));
        chooseSiteButton.setOnAction(e -> chooseSite(sitesList, selectedSiteLabel, siteSpecificPane));

        BorderPane sitesSceneLayout = new BorderPane();
        setBackground(sitesSceneLayout);
        sitesSceneLayout.setLeft(sitesPane);
        sitesSceneLayout.setCenter(siteSpecificPane);
        BorderPane.setMargin(sitesPane, new Insets(20));
        BorderPane.setMargin(siteSpecificPane, new Insets(20, 20, 20, 0));

        sitesScene = new Scene(sitesSceneLayout);
    }

    private void changeView(BorderPane pane, ChartViewer chart, TableView table, Button button) {
        if (pane.getCenter() instanceof ChartViewer) {
            table = createTable();
            pane.setCenter(table);
            button.setText("Change to chart");
        } else {
            chart.setChart(createObservationsChart());
            pane.setCenter(chart);
            button.setText("Change to table");
        }
    }

    private TableView createTable() {
        TableView<Observation> table = new TableView<>();
        List<Observation> observations = FXCollections.observableArrayList();
        if (selectedSite != null) {
            observations.addAll(service.getObservationsOfLoggedUserAndChosenSite(selectedSite));
        }

        TableColumn<Observation, String> timestampCol = new TableColumn<>("Timestamp\n(year-month-day)");
        timestampCol.setMinWidth(150);
        timestampCol.setCellValueFactory(
                observation -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    property.setValue(dateFormat.format(observation.getValue().getTimestamp().getTime()));
                    return property;
                });

        TableColumn<Observation, Double> temperatureCol = new TableColumn<>("Temperature\n(°C)");
        temperatureCol.setMinWidth(130);
        temperatureCol.setCellValueFactory(new PropertyValueFactory<>("temperature"));

        TableColumn<Observation, Double> rhCol = new TableColumn<>("Relative humidity\n(%RH)");
        rhCol.setMinWidth(150);
        rhCol.setCellValueFactory(new PropertyValueFactory<>("rh"));

        TableColumn<Observation, Double> rainfallCol = new TableColumn<>("Rainfall\n(mm)");
        rainfallCol.setMinWidth(130);
        rainfallCol.setCellValueFactory(new PropertyValueFactory<>("rainfall"));

        TableColumn<Observation, Double> pressureCol = new TableColumn<>("Pressure\n(mbar)");
        pressureCol.setMinWidth(130);
        pressureCol.setCellValueFactory(new PropertyValueFactory<>("pressure"));

        TableColumn<Observation, String> descriptionCol = new TableColumn<>("Description");
        descriptionCol.setMinWidth(130);
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<Observation, String> commentCol = new TableColumn<>("Comment");
        commentCol.setMinWidth(200);
        commentCol.setCellValueFactory(new PropertyValueFactory<>("comment"));

        table.getColumns().addAll(timestampCol, temperatureCol, rhCol, rainfallCol, pressureCol, descriptionCol, commentCol);
        table.getItems().addAll(observations);
        return table;
    }

    private void performMenuAction(ComboBox<String> menu) {
        String action = menu.getValue();
        if (action.equals("Log out")) {
            service.logout();
            window.setScene(logInScene);
            return;
        }
        if (action.equals("About")) {
            PopUpBox.show("About", "Weather Observation Journal\n"
                    + "Version: Release 1.1\n\n"
                    + "Logged in as: "
                    + service.getLoggedUser().getUsername()
                    + " (" + service.getLoggedUser().getName() + ")");
            return;
        }
        if (action.equals("Exit")) {
            service.logout();
            window.close();
        }
    }

    private String getSiteAsText(Site s) {
        String siteText = s.getSitename() + ":\n" + s.getAddress();
        if (!s.getDescription().isBlank()) {
            siteText += " (" + s.getDescription() + ")";
        }
        return siteText;
    }

    private void chooseSite(ListView sitesView, Label label, BorderPane pane) {
        selectedSite = (Site) sitesView.getSelectionModel().getSelectedItem();
        if (selectedSite != null) {
            label.setText("Selected site: " + selectedSite.getSitename());
            updateSitePane(pane);
        }
    }

    private void updateSitePane(BorderPane pane) {
        Object o = pane.getCenter();
        if (o instanceof ChartViewer) {
            ((ChartViewer) o).setChart(createObservationsChart());
        } else {
            pane.setCenter(createTable());
        }
    }

    private JFreeChart createObservationsChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries s1 = new TimeSeries("Temperature (°C)");
        TimeSeries s2 = new TimeSeries("Relative humidity (%RH)");
        TimeSeries s3 = new TimeSeries("Rainfall (mm)");
        TimeSeries s4 = new TimeSeries("Pressure (mbar)");

        if (selectedSite != null) {
            List<Observation> observations = service.getObservationsOfLoggedUserAndChosenSite(selectedSite);
            if (observations != null) {
                for (Observation o : observations) {
                    Date rawTimestamp = new Date(o.getTimestamp().getTime());
                    s1.addOrUpdate(new Minute(rawTimestamp), o.getTemperature());
                    s2.addOrUpdate(new Minute(rawTimestamp), o.getRh());
                    s3.addOrUpdate(new Minute(rawTimestamp), o.getRainfall());
                    s4.addOrUpdate(new Minute(rawTimestamp), o.getPressure());
                }
            }

        }

        dataset.addSeries(s1);
        dataset.addSeries(s2);
        dataset.addSeries(s3);
        dataset.addSeries(s4);

        JFreeChart chart = ChartFactory.createTimeSeriesChart("Observations", "Date", "Value", dataset, true, true, true);
        chart.setBackgroundPaint(new java.awt.Color(0, 0, 0, 0));
        chart.getLegend().setBackgroundPaint(new java.awt.Color(0, 0, 0, 0));
        XYPlot plot = (XYPlot) chart.getXYPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);

        plot.setBackgroundPaint(new java.awt.Color(255, 255, 255, 140));
        plot.setDomainGridlinePaint(new java.awt.Color(0, 0, 0, 50));
        plot.setRangeGridlinePaint(new java.awt.Color(0, 0, 0, 35));
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
        XYLineAndShapeRenderer renderer
                = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        renderer.setSeriesShapesVisible(2, true);
        renderer.setSeriesShapesVisible(3, true);

        return chart;
    }

    private void createNewSiteScene() {
        Label header = createWojHeader();

        Region region1 = new Region();
        region1.setMinHeight(10);
        Region region2 = new Region();
        region2.setMinHeight(40);

        Label createNewSiteLabel = new Label("Create new site");

        TextField sitenameField = new TextField();
        sitenameField.setPromptText("Enter new sitename");

        TextField addressField = new TextField();
        addressField.setPromptText("Enter address of the site");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter description of the site (optional)");

        Button createSiteButton = createButton("+ Create new site", Color.LIGHTGREEN, true);
        createSiteButton.setOnAction(e -> {
            boolean siteCreated = createSite(sitenameField, addressField, descriptionField);
            if (siteCreated) {
                createSitesScene();
                window.setScene(sitesScene);
            }
        });

        Button cancelButton = createButton("Cancel", Color.LIGHTCORAL, true);
        cancelButton.setOnAction(e -> {
            sitenameField.clear();
            addressField.clear();
            descriptionField.clear();
            createSitesScene();
            window.setScene(sitesScene);
        });

        VBox centerLayout = new VBox(15);
        centerLayout.setMaxSize(windowWidth / 3.0, windowHeight / 1.6);
        setBackgroundFill(centerLayout, new Color(1, 1, 1, 0.5), 10, -20);
        centerLayout.getChildren().addAll(
                header, region1, createNewSiteLabel,
                sitenameField, addressField, descriptionField,
                createSiteButton, region2, cancelButton);

        BorderPane createSiteLayout = new BorderPane();
        createSiteLayout.setCenter(centerLayout);
        BorderPane.setAlignment(centerLayout, Pos.CENTER);
        setBackground(createSiteLayout);

        createSiteScene = new Scene(createSiteLayout);

        //Disable autofocus to the first textfield.
        createSiteLayout.requestFocus();
    }

    private boolean createSite(TextField sitenameField, TextField addressField, TextField descriptionField) {
        String sitename = sitenameField.getText();
        String address = addressField.getText();
        String description = descriptionField.getText();

        if (sitename.length() < 1) {
            sitenameField.clear();
            PopUpBox.show("Creating new site failed", "Please ensure that you have entered valid sitename (at least 1 character long).");
            return false;
        }

        if (address.length() < 3) {
            addressField.clear();
            PopUpBox.show("Creating new site failed", "Please ensure that you have entered valid address (at least 3 character long).");
            return false;
        }

        Site site = new Site();
        site.setSitename(sitename);
        site.setAddress(address);
        site.setDescription(description);

        if (service.createSite(site)) {
            PopUpBox.show("Created new site successfully", "Site " + sitename + ", " + address + " (" + description + ") created.");
            sitenameField.clear();
            addressField.clear();
            descriptionField.clear();
            return true;
        }
        PopUpBox.show("Creating new site failed", "Site " + sitename + " could not be created. Please ensure used sitename is unique.");
        sitenameField.clear();
        return false;
    }

    private void createNewObservationScene() {
        Label header = createWojHeader();

        Region region1 = new Region();
        region1.setMinHeight(3);
        Region region2 = new Region();
        region2.setMinHeight(15);

        Label createNewObservationLabel = new Label("Create new observation for site\n" + selectedSite.getSitename());

        DatePicker datepicker = new DatePicker();
        datepicker.setPromptText("Select date (if not today)");
        datepicker.setMaxWidth(Double.MAX_VALUE);

        int fieldWidth = 270;

        TextField temperatureField = new TextField();
        temperatureField.setPromptText("Enter temperature (-60...+60 °C)");
        temperatureField.setMinWidth(fieldWidth);
        Label temperatureUnit = new Label("°C");
        HBox temperatureBox = new HBox(10);
        temperatureBox.getChildren().addAll(temperatureField, temperatureUnit);
        temperatureBox.setAlignment(Pos.CENTER_LEFT);

        TextField rhField = new TextField();
        rhField.setPromptText("Enter relative humidity (0...100 %RH)");
        rhField.setMinWidth(fieldWidth);
        Label rhUnit = new Label("%RH");
        HBox rhBox = new HBox(10);
        rhBox.getChildren().addAll(rhField, rhUnit);
        rhBox.setAlignment(Pos.CENTER_LEFT);

        TextField rainfallField = new TextField();
        rainfallField.setPromptText("Enter rainfall (>= 0 mm)");
        rainfallField.setMinWidth(fieldWidth);
        Label rainfallUnit = new Label("mm");
        HBox rainfallBox = new HBox(10);
        rainfallBox.getChildren().addAll(rainfallField, rainfallUnit);
        rainfallBox.setAlignment(Pos.CENTER_LEFT);

        TextField pressureField = new TextField();
        pressureField.setPromptText("Enter pressure (> 0 mbar)");
        pressureField.setMinWidth(fieldWidth);
        Label pressureUnit = new Label("mbar");
        HBox pressureBox = new HBox(10);
        pressureBox.getChildren().addAll(pressureField, pressureUnit);
        pressureBox.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> descriptionMenu = new ComboBox<>();
        descriptionMenu.setPromptText("Choose description");
        descriptionMenu.getItems().addAll("Sunny", "Cloudy", "Rainy", "Storm");
        descriptionMenu.setMaxWidth(Double.MAX_VALUE);

        TextField commentField = new TextField();
        commentField.setPromptText("Enter comment (optional)");

        Button createObservationButton = createButton("+ Create new observation", Color.LIGHTGREEN, true);
        createObservationButton.setOnAction(e -> {
            createObservation(datepicker, temperatureField, rhField, rainfallField, pressureField, descriptionMenu, commentField);
        });

        Button backButton = createButton("Back", Color.LIGHTCORAL, true);
        backButton.setOnAction(e -> {
            temperatureField.clear();
            rhField.clear();
            rainfallField.clear();
            commentField.clear();
            datepicker.getEditor().clear();
            Pane sitesPane = (Pane) sitesScene.getRoot();
            updateSitePane((BorderPane) sitesPane.getChildren().get(1));
            window.setScene(sitesScene);
        });

        VBox centerLayout = new VBox(15);
        centerLayout.setMaxSize(windowWidth / 3.0, windowHeight / 1.2);
        setBackgroundFill(centerLayout, new Color(1, 1, 1, 0.5), 10, -20);
        centerLayout.getChildren().addAll(
                header, region1, createNewObservationLabel, datepicker,
                temperatureBox, rhBox, rainfallBox, pressureBox,
                descriptionMenu, commentField, createObservationButton, region2, backButton);

        BorderPane createObservationLayout = new BorderPane();
        createObservationLayout.setCenter(centerLayout);
        BorderPane.setAlignment(centerLayout, Pos.CENTER);
        setBackground(createObservationLayout);

        createObservationScene = new Scene(createObservationLayout);

        //Disable autofocus to the first textfield.
        createObservationLayout.requestFocus();
    }

    private void createObservation(DatePicker datepicker, TextField temperatureField, TextField rhField, TextField rainfallField, TextField pressureField, ComboBox descriptionMenu, TextField commentField) {
        String errorMessage = "Please ensure that the inputs of the following fields are correct:";
        Boolean error = false;
        Double temperature;
        Double rh;
        Double rainfall;
        Double pressure;
        String description = "";
        String comment;
        String style = temperatureField.getStyle();
        String descriptionStyle = descriptionMenu.getStyle();

        try {
            temperature = Double.parseDouble(temperatureField.getText());
        } catch (NumberFormatException e) {
            temperature = null;
        }
        if (temperature == null || temperature < -60 || temperature > 60) {
            errorMessage += "\n- Temperature should be a decimal number between -60 and 60 °C";
            temperatureField.setStyle("-fx-background-color:SALMON");
            temperatureField.clear();
            error = true;
        }

        try {
            rh = Double.parseDouble(rhField.getText());
        } catch (NumberFormatException e) {
            rh = null;
        }
        if (rh == null || rh < 0 || rh > 100) {
            errorMessage += "\n- Relative humidity (RH) should be a decimal number between 0 and 100 %RH";
            rhField.setStyle("-fx-background-color:SALMON");
            rhField.clear();
            error = true;
        }

        try {
            rainfall = Double.parseDouble(rainfallField.getText());
        } catch (NumberFormatException e) {
            rainfall = null;
        }
        if (rainfall == null || rainfall < 0) {
            errorMessage += "\n- Rainfall should be a decimal number equal or greater than 0 mm";
            rainfallField.setStyle("-fx-background-color:SALMON");
            rainfallField.clear();
            error = true;
        }

        try {
            pressure = Double.parseDouble(pressureField.getText());
        } catch (NumberFormatException e) {
            pressure = null;
        }
        if (pressure == null || pressure < 0) {
            errorMessage += "\n- Pressure should be a decimal number equal or greater than 0 mbar";
            pressureField.setStyle("-fx-background-color:SALMON");
            pressureField.clear();
            error = true;
        }

        if (descriptionMenu.getValue() == null) {
            errorMessage += "\n- Weather description should be chosen from the available options";
            descriptionMenu.setStyle("-fx-background-color:SALMON");
            error = true;
        } else {
            description = descriptionMenu.getValue().toString();
        }
        comment = commentField.getText();

        if (error) {
            PopUpBox.show("Failed to create new observation", errorMessage);
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            temperatureField.setStyle(style);
            rhField.setStyle(style);
            rainfallField.setStyle(style);
            pressureField.setStyle(style);
            descriptionMenu.setStyle(descriptionStyle);
            return;
        }

        Timestamp timestamp;

        LocalDate date = datepicker.getValue();
        if (date != null) {
            timestamp = Timestamp.valueOf(date.atTime(LocalTime.MIDNIGHT));

        } else {
            timestamp = Timestamp.valueOf(LocalDate.now().atTime(LocalTime.MIDNIGHT));
        }

        Observation observation = new Observation();
        observation.setTimestamp(timestamp);
        observation.setTemperature(temperature);
        observation.setRh(rh);
        observation.setRainfall(rainfall);
        observation.setPressure(pressure);
        observation.setDescription(description);
        observation.setComment(comment);
        observation.setObservationSite(selectedSite);

        service.createObservation(observation);
        PopUpBox.show("New observation created succesfully", "Observation for " + selectedSite.getSitename() + " on " + timestamp.toString() + " created.");

        temperatureField.clear();
        rhField.clear();
        rainfallField.clear();
        pressureField.clear();
        descriptionMenu.getSelectionModel().clearSelection();

        commentField.clear();
    }

    private void setBackgroundFill(Pane pane, Color color, int cornerRadii, int inset) {
        BackgroundFill bf = new BackgroundFill(
                new Color(1, 1, 1, 0.5),
                new CornerRadii(cornerRadii), new Insets(inset));
        pane.setBackground(new Background(bf));
    }

    private void setBackground(Pane pane) {
        BackgroundImage backgroundImage = new BackgroundImage(new Image("file:images/background.png", windowWidth, 0, true, true),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        pane.setBackground(new Background(backgroundImage));
    }

    private Button createButton(String message, Color color, boolean useMaximumWidth) {

        Button button = new Button(message);

        if (useMaximumWidth) {
            button.setMaxWidth(Double.MAX_VALUE);
        }

        BackgroundFill buttonFill = new BackgroundFill(color, new CornerRadii(10), Insets.EMPTY);
        BackgroundFill buttonPressedFill = new BackgroundFill(color.darker(), new CornerRadii(10), Insets.EMPTY);
        BackgroundFill mouseOverFill = new BackgroundFill(color.brighter(), new CornerRadii(10), Insets.EMPTY);

        button.setBackground(new Background(buttonFill));
        button.setOnMousePressed(e -> button.setBackground(new Background(buttonPressedFill)));
        button.setOnMouseReleased(e -> button.setBackground(new Background(buttonFill)));
        button.setOnMouseEntered(e -> button.setBackground(new Background(mouseOverFill)));
        button.setOnMouseExited(e -> button.setBackground(new Background(buttonFill)));
        return button;
    }

    private Label createWojHeader() {
        Label woj = new Label("Weather Observation Journal");
        woj.setFont(Font.font("Helvetica", FontWeight.BOLD, 32));
        woj.setTextAlignment(TextAlignment.CENTER);
        woj.setWrapText(true);
        return woj;
    }
}
