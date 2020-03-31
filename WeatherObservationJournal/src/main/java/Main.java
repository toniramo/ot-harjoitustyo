/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author toniramo
 */

import java.sql.SQLException;
import java.util.Scanner;
import domain.*;
import dao.*;
import ui.*;

public class Main {
        public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserDao userDao = new SQLiteUserDao();
        SiteDao siteDao = new SQLiteSiteDao();
        
        JournalService service = new JournalService(userDao, siteDao);
        TextUI ui = new TextUI(service, scanner);
        
        ui.start();
    }
}
