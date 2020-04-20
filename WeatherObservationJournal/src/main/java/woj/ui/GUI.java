/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.ui;

import woj.domain.*;
import woj.dao.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 *
 * @author toniramo
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

    @Override
    public void init() {
        UserDao userDao = new SQLiteUserDao();
        SiteDao siteDao = new SQLiteSiteDao();
        ObservationDao observationDao = new SQLiteObservationDao();
        this.service = new JournalService(userDao, siteDao, observationDao);
    }

    @Override
    public void start(Stage stage) throws Exception {
        window = stage;

        createLogInScene();
        createNewUserScene();
        createNewObservationScene();
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
        usernameField.clear();
        nameField.clear();

        if (username.length() < 2) {
            PopUpBox.show("Failed to create user", "Please enter username that is at least 2 characters long.");
            return;
        }
        if (name.isBlank()) {
            PopUpBox.show("Failed to create user", "Please enter actual name of the user.");
            return;
        }
        if (service.createUser(username, name)) {
            window.setScene(logInScene);
            PopUpBox.show("User created succesfully", "User " + username + " (" + name + ") created.");
            return;
        }
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
        ChartViewer chartViewer = new ChartViewer(createObservationsChart());

        Button chooseSiteButton = createButton("Choose site", Color.LIGHTBLUE, true);
        chooseSiteButton.setOnAction(e -> chooseSite(sitesList, selectedSiteLabel, chartViewer));

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
            window.setScene(createObservationScene);
        });

        BorderPane siteSpecificScene = new BorderPane();
        siteSpecificScene.setTop(selectedSiteLabel);
        siteSpecificScene.setCenter(chartViewer);
        siteSpecificScene.setBottom(createObservationButton);
        setBackgroundFill(siteSpecificScene, new Color(1, 1, 1, 0.5), 10, 0);
        siteSpecificScene.setPadding(new Insets(20));
        BorderPane.setMargin(selectedSiteLabel, new Insets(10));
        BorderPane.setMargin(chartViewer, new Insets(10));
        BorderPane.setMargin(createObservationButton, new Insets(10));

        BorderPane sitesSceneLayout = new BorderPane();
        setBackground(sitesSceneLayout);
        sitesSceneLayout.setLeft(sitesPane);
        sitesSceneLayout.setCenter(siteSpecificScene);
        BorderPane.setMargin(sitesPane, new Insets(20));
        BorderPane.setMargin(siteSpecificScene, new Insets(20, 20, 20, 0));

        sitesScene = new Scene(sitesSceneLayout);
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
                    + "Version: Release 1.0\n\n"
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

    private void chooseSite(ListView sitesView, Label label, ChartViewer viewer) {
        selectedSite = (Site) sitesView.getSelectionModel().getSelectedItem();
        if (selectedSite != null) {
            label.setText("Selected site: " + selectedSite.getSitename());
            viewer.setChart(createObservationsChart());
        }
    }

    private JFreeChart createObservationsChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        //dataset.setDomainIsPointsInTime(true);
        TimeSeries s1 = new TimeSeries("Temperature (°C)");
        TimeSeries s2 = new TimeSeries("Relative humidity (%RH)");
        TimeSeries s3 = new TimeSeries("Rainfall (mm)");
        TimeSeries s4 = new TimeSeries("Pressure (mbar)");

        if (selectedSite != null) {
            List<Observation> observations = service.getObservationsOfChosenSite(selectedSite);
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
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainPannable(true);
        plot.setRangePannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
        plot.getDomainAxis().setLowerMargin(0.0);
        plot.setBackgroundAlpha(0);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setHorizontalAlignment(HorizontalAlignment.CENTER);
        XYLineAndShapeRenderer renderer =
        (XYLineAndShapeRenderer) plot.getRenderer();
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
            createSite(sitenameField, addressField, descriptionField);
            createSitesScene();
            window.setScene(sitesScene);
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

    private void createSite(TextField sitenameField, TextField addressField, TextField descriptionField) {
        String sitename = sitenameField.getText();
        String address = addressField.getText();
        String description = descriptionField.getText();

        sitenameField.clear();
        addressField.clear();
        descriptionField.clear();

        if (sitename.length() < 1 || address.length() < 3) {
            PopUpBox.show("Creating new site failed", "Please ensure that you have entered sitename and address\n(at least 1 and 3 characters long, respectively).");
            return;
        }

        Site site = new Site();
        site.setSitename(sitename);
        site.setAddress(address);
        site.setDescription(description);

        if (service.createSite(site)) {
            PopUpBox.show("Created new site successfully", "Site " + sitename + ", " + address + " (" + description + ") created.");
            return;
        }
        PopUpBox.show("Creating new site failed", "Site " + sitename + " could not be created. Please ensure used sitename is unique.");
    }

    private void createNewObservationScene() {
        Label header = createWojHeader();

        Region region1 = new Region();
        region1.setMinHeight(5);
        Region region2 = new Region();
        region2.setMinHeight(25);

        Label createNewObservationLabel = new Label("Create new Observation");

        DatePicker datepicker = new DatePicker();
        datepicker.setPromptText("Select date (if not today)");
        datepicker.setMaxWidth(Double.MAX_VALUE);
        
        TextField temperatureField = new TextField();
        temperatureField.setPromptText("Enter temperature (-60...+60 °C)");

        TextField rhField = new TextField();
        rhField.setPromptText("Enter relative humidity (0...100 %RH)");

        TextField rainfallField = new TextField();
        rainfallField.setPromptText("Enter rainfall (>= 0 mm)");

        TextField pressureField = new TextField();
        pressureField.setPromptText("Enter pressure (> 0 mbar)");

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
            createSitesScene();
            datepicker.getEditor().clear();
            window.setScene(sitesScene);
        });

        VBox centerLayout = new VBox(15);
        centerLayout.setMaxSize(windowWidth / 3.0, windowHeight / 1.2);
        setBackgroundFill(centerLayout, new Color(1, 1, 1, 0.5), 10, -20);
        centerLayout.getChildren().addAll(
                header, region1, createNewObservationLabel, datepicker,
                temperatureField, rhField, rainfallField, pressureField,
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

        try {
            Double temperature = Double.parseDouble(temperatureField.getText());
            Double rh = Double.parseDouble(rhField.getText());
            Double rainfall = Double.parseDouble(rainfallField.getText());
            Double pressure = Double.parseDouble(pressureField.getText());
            String description = descriptionMenu.getValue().toString();
            String comment = commentField.getText();

            if (temperature < -60 || temperature > 60 || rh < 0 || rh > 100 || rainfall < 0 || pressure < 0 || description.isBlank()) {
                PopUpBox.show("Failed to create new observation", "Please ensure that you are entering valid input for necessary fields.");
                temperatureField.clear();
                rhField.clear();
                rainfallField.clear();
                pressureField.clear();
                descriptionMenu.getSelectionModel().clearSelection();
                commentField.clear();
                return;
            }

            Timestamp timestamp;

            LocalDate date = datepicker.getValue();
            if (date != null) {
                timestamp = Timestamp.valueOf(date.atTime(LocalTime.MIDNIGHT));
            } else {
                timestamp = new Timestamp(System.currentTimeMillis());
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
            PopUpBox.show("New observation created succesfully", "Observation for " + selectedSite.getSitename() + " on " + timestamp.toString() + " created." );

        } catch (Exception e) {
            PopUpBox.show("Failed to create new observation", "Please check the format of your input.");
        }
        
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

        button.setBackground(new Background(buttonFill));
        button.setOnMousePressed(e -> button.setBackground(new Background(buttonPressedFill)));
        button.setOnMouseReleased(e -> button.setBackground(new Background(buttonFill)));

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
