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
import woj.domain.JournalService;
import woj.ui.GUI;
import woj.dao.SiteDao;
import woj.dao.UserDao;
import woj.dao.SQLiteUserDao;
import woj.dao.SQLiteSiteDao;
import java.util.Scanner;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new SQLiteUserDao();
        SiteDao siteDao = new SQLiteSiteDao();

        JournalService service = new JournalService(userDao, siteDao);
        //TextUI ui = new TextUI(service, scanner);
        //TextUI (woj.ui.TextUI.java) replaced with Graphical UI (class woj.ui.GUI.java)

        Application.launch(GUI.class, args);
    }
}
