package woj;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author toniramo
 */
import woj.ui.*;
import javafx.application.Application;

public class Main {

    public static void main(String[] args) {
        //Scanner scanner = new Scanner(System.in);
//        UserDao userDao = new SQLiteUserDao();
//        SiteDao siteDao = new SQLiteSiteDao();
//        ObservationDao observationDao = new SQLiteObservationDao();

//        JournalService service = new JournalService(userDao, siteDao, observationDao);
        //TextUI ui = new TextUI(service, scanner);
        //TextUI (woj.ui.TextUI.java) replaced with Graphical UI (class woj.ui.GUI.java)

        Application.launch(GUI.class, args);
//        Application.launch(TimeSeriesChartFXDemo1.class, args);
    }
}
