/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package woj.ui;

import java.util.List;
import woj.domain.JournalService;
import woj.dao.SiteDao;
import woj.dao.UserDao;
import woj.dao.SQLiteUserDao;
import woj.dao.SQLiteSiteDao;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.Window;
import woj.domain.Site;

/**
 *
 * @author toniramo
 */
public class GUI extends Application {

    private JournalService service;

    private Scene logInScene;
    private Scene loggedInScene;
    private Scene createUserScene;
    private Scene addSiteScene;

    int windowHeight;
    int windowWidth;

//    public GUI(JournalService service) {
//        this.service=service;
//    }
    @Override
    public void init() {
        UserDao userDao = new SQLiteUserDao();
        SiteDao siteDao = new SQLiteSiteDao();

        this.service = new JournalService(userDao, siteDao);
    }

    @Override
    public void start(Stage stage) throws Exception {
        windowHeight = 800;
        windowWidth = 500;

        //Title
        stage.setTitle("Weather Observation Journal");

        //Grids
        GridPane gridpane = createLogInScreenPane();
//        GridPane sitesPane = createSitesScreenPane();
        GridPane newUserPane = createNewUserScreenPane();
        GridPane addSitePane = createNewUserScreenPane();

        //Controls
        addLogInScreenControls(gridpane, stage);
//        addSitesScreenControls(sitesPane, stage);
        addCreateNewUserScreenControls(newUserPane, stage);
        addAddSiteScreenControls(addSitePane, stage);

        //BorderPane
        BorderPane borderpane = new BorderPane();
        borderpane.setCenter(gridpane);
        BorderPane.setMargin(gridpane, new Insets(20, 20, 20, 20));

        logInScene = new Scene(borderpane, windowWidth, windowHeight);
//        loggedInScene = new Scene(sitesPane, windowWidth, windowHeight);
        createUserScene = new Scene(newUserPane, windowWidth, windowHeight);
        addSiteScene = new Scene(addSitePane, windowWidth, windowHeight);

        stage.setScene(logInScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void createSitesScene(Stage stage) {
        GridPane sitesPane = createSitesScreenPane();
        addSitesScreenControls(sitesPane, stage);
        loggedInScene = new Scene(sitesPane, windowWidth, windowHeight);
    }

    private GridPane createLogInScreenPane() {
        GridPane gridpane = new GridPane();

        //Adjustments
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setVgap(15);

        return gridpane;
    }

    private GridPane createSitesScreenPane() {
        GridPane gridpane = new GridPane();

        //gridpane.setAlignment(Pos.CENTER);
        gridpane.setVgap(15);
        gridpane.setHgap(15);

        return gridpane;
    }

    private GridPane createNewUserScreenPane() {
        GridPane gridpane = new GridPane();

        //Adjustments
        gridpane.setAlignment(Pos.CENTER);
        gridpane.setVgap(15);

        return gridpane;
    }

    private void addLogInScreenControls(GridPane gridpane, Stage stage) {
        //Header
        Label headerLabel = new Label("Weather Observation Journal");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        headerLabel.setWrapText(true);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        gridpane.add(headerLabel, 0, 0);

        //Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        gridpane.add(usernameField, 0, 2);

        //Log in button
        Button logInButton = new Button("Log in");
        logInButton.setMaxWidth(Double.MAX_VALUE);
        gridpane.add(logInButton, 0, 3);

        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String username = usernameField.getText();
                usernameField.clear();
                
                if (username.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "Loggin in Error!", "Please enter username");
                    return;
                }

                boolean loggedIn = service.login(username);
                if (loggedIn) {
                    showAlert(Alert.AlertType.INFORMATION, gridpane.getScene().getWindow(), "Logging in Successful", "Welcome " + service.getLoggedUser().getName() + " (" + username + ")!");
                    createSitesScene(stage);
                    stage.setScene(loggedInScene);
                    return;

                } else {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "Loggin In Error", "User not found. Please ensure that you use existing username.");
                    return;
                }

            }
        });

        //New user creation label
        Label createUserLabel = new Label("If you do not have a user account yet:");
        createUserLabel.setTextAlignment(TextAlignment.CENTER);
        createUserLabel.setWrapText(true);
        gridpane.add(createUserLabel, 0, 5);

        //New user creation button
        Button createUserButton = new Button("+ Create new user");
        createUserButton.setMaxWidth(Double.MAX_VALUE);
        gridpane.add(createUserButton, 0, 6);

        createUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.setScene(createUserScene);
            }
        });

    }

    private void addCreateNewUserScreenControls(GridPane gridpane, Stage stage) {
        //Header
        Label headerLabel = new Label("Weather Observation Journal");
        headerLabel.setFont(Font.font("Helvetica", FontWeight.BOLD, 30));
        headerLabel.setWrapText(true);
        headerLabel.setTextAlignment(TextAlignment.CENTER);
        gridpane.add(headerLabel, 0, 0);

        //Create new user label
        Label createNewUserLabel = new Label("Create new user");
        gridpane.add(createNewUserLabel, 0, 1);

        //new username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter unique username (at least 2 characters).");
        gridpane.add(usernameField, 0, 2);

        //new username field
        TextField nameField = new TextField();
        nameField.setPromptText("Enter actual name of the user.");
        gridpane.add(nameField, 0, 3);

        //New user creation button
        Button createUserButton = new Button("+ Create new user");
        createUserButton.setMaxWidth(Double.MAX_VALUE);
        gridpane.add(createUserButton, 0, 4);

        createUserButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String username = usernameField.getText();
                String name = nameField.getText();

                usernameField.clear();
                nameField.clear();

                if (username.isEmpty() || name.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "User Creation Error", "Please enter username and actual name of the user.");
                    return;
                }

                if (username.length() < 2) {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "User Creation Error", "Please use username that is at least 2 characters long.");
                    return;
                }

                boolean userCreated = service.createUser(username, name);
                if (userCreated) {
                    showAlert(Alert.AlertType.INFORMATION, gridpane.getScene().getWindow(), "User Creation Successful", "User account (" + username + ") for user " + name + " created.");
                    stage.setScene(logInScene);
                    return;

                } else {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "User Creation Error", "User creation failed. Please enter unique username.");
                    return;
                }

            }
        });

        //Cancel button
        Button cancelButton = new Button("Cancel user creation");
        cancelButton.setMaxWidth(Double.MAX_VALUE);
        gridpane.add(cancelButton, 0, 5);

        cancelButton.setOnAction((ActionEvent t) -> {
            usernameField.clear();
            nameField.clear();
            stage.setScene(logInScene);
        });
    }

    private void addSitesScreenControls(GridPane gridpane, Stage stage) {
        VBox sitesBox = listSites();
        gridpane.add(sitesBox, 0, 1);

        //Log out button
        Button logOutButton = new Button("Log out");
        gridpane.add(logOutButton, 0, 0);

        logOutButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                showAlert(Alert.AlertType.INFORMATION, gridpane.getScene().getWindow(), "Logged out", "Logged out user " + service.getLoggedUser().getName() + " (" + service.getLoggedUser().getUsername() + ").");
                stage.setScene(logInScene);
            }
        });

        //Create site button
        Button addSiteButton = new Button("+ Add new site");
        gridpane.add(addSiteButton, 0, 2);

        addSiteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                stage.setScene(addSiteScene);
            }

        });
    }

    private void addAddSiteScreenControls(GridPane gridpane, Stage stage) {
        Label addSiteLabel = new Label("Add new site");
        gridpane.add(addSiteLabel, 0, 0);

        TextField sitenameField = new TextField();
        sitenameField.setPromptText("Add unique site name");
        gridpane.add(sitenameField, 0, 1);

        TextField siteAddressField = new TextField();
        siteAddressField.setPromptText("Add site address");
        gridpane.add(siteAddressField, 0, 2);

        TextField siteDescriptionField = new TextField();
        siteDescriptionField.setPromptText("Add site description (optional)");
        gridpane.add(siteDescriptionField, 0, 3);

        Button addSiteButton = new Button("+ Add new site");
        gridpane.add(addSiteButton, 0, 4);

        Button cancelButton = new Button("Cancel adding new site");
        gridpane.add(cancelButton, 0, 5);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                sitenameField.clear();
                siteAddressField.clear();
                siteDescriptionField.clear();
                createSitesScene(stage);
                stage.setScene(loggedInScene);
                return;
            }

        });
        addSiteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                String sitename = sitenameField.getText();
                String siteAddress = siteAddressField.getText();
                String siteDescription = siteDescriptionField.getText();

                sitenameField.clear();
                siteAddressField.clear();
                siteDescriptionField.clear();

                if (sitename.length() < 1 || siteAddress.length() < 3) {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "Site creation Error", "Please ensure you have entered site name of at least 1 character long and site address of at least 3 characters long.");
                    return;
                }
                Site site = new Site();
                site.setSitename(sitename);
                site.setAddress(siteAddress);
                site.setDescription(siteDescription);

                boolean siteCreated = service.createSite(site);

                if (siteCreated) {
                    showAlert(Alert.AlertType.INFORMATION, gridpane.getScene().getWindow(), "Site added Successfully", "Site with following information created. " + site.toString());
                    createSitesScene(stage);
                    stage.setScene(loggedInScene);
                } else {
                    showAlert(Alert.AlertType.ERROR, gridpane.getScene().getWindow(), "Adding new site failed!", "Adding site with following information failed. Ensure that the sitename is unique" + site.toString());
                }

            }

        });
    }

    private VBox listSites() {
        List<Site> sites = service.getSitesOfLoggedUser();

        VBox sitesBox = new VBox();
        for (Site site : sites) {
            Label siteName = new Label(site.getSitename());
            siteName.setFont(Font.font("Helvetica", FontWeight.BOLD, 20));
            Label siteAddress = new Label(site.getAddress());
            Label siteDescription = new Label(site.getDescription());
            VBox siteBox = new VBox();
            siteBox.getChildren().addAll(siteName, siteAddress, siteDescription);
            sitesBox.getChildren().add(siteBox);
        }

        return sitesBox;

    }

    private void showAlert(Alert.AlertType alertType, Window owner, String title, String msg) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initOwner(owner);
        alert.show();
    }

}
