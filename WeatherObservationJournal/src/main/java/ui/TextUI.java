/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.util.*;
import domain.*;

public class TextUI {

    private final JournalService service;
    private final Scanner scanner;

    public TextUI(JournalService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
    }

    public void start() {
        while (true) {
            System.out.println("Enter command: 0=close application, 1=log in, 2=create new user");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Shutting down service.");
                break;
            }

            if (input.equals("1")) {
                System.out.println("Enter username");
                String username = scanner.nextLine();
                boolean loggedIn = service.login(username);
                if (loggedIn) {
                    System.out.println("Logging in successful.");
                    System.out.println("Logged in as " + service.getLoggedUser().getName() + " (" + service.getLoggedUser().getUsername() + ")\n");
                    loggedIn();
                } else {
                    System.out.println("Could not find user.");
                }

            } else if (input.equals("2")) {
                System.out.println("Enter new usename");
                String username = scanner.nextLine();
                System.out.println("Enter name of the user");
                String name = scanner.nextLine();

                boolean userCreated = service.createUser(username, name);
                if (userCreated) {
                    System.out.println("New user created.");
                } else {
                    System.out.println("User creation failed.");
                }

            } else {
                System.out.println("Unknown command.");
            }
            System.out.println("");
        }
    }

    public void loggedIn() {
        while (true) {

            System.out.println("Enter command. 0=log out, 1=list sites, 2=choose site (TODO), 3=create new site");
            String input = scanner.nextLine();

            if (input.equals("0")) {
                System.out.println("Logging out.");
                service.logout();
                return;
            } else if (input.equals("1")) {
                System.out.println("List user sites:");
                List<Site> sites = service.getSitesOfLoggedUser();
                if (sites.isEmpty()) {
                    System.out.println("No sites found for current user.");
                } else {
                    for (Site site: sites) {
                        System.out.println(site.toString());
                    }
                }
            } else if (input.equals("2")) {
                System.out.println("TODO list site specific information and commands");

            } else if (input.equals("3")) {
                System.out.println("Create new site.");
                System.out.println("Enter unique name for the site:");
                String sitename = scanner.nextLine();
                System.out.println("Enter address of the site:");
                String address = scanner.nextLine();
                System.out.println("Enter description of the site (optional):");
                String description = scanner.nextLine();

                Site site = new Site();
                site.setSitename(sitename);
                site.setAddress(address);
                site.setDescription(description);
                site.setCreatedBy(service.getLoggedUser().getUsername());

                boolean siteCreated = service.createSite(site);
                if (siteCreated) {
                    System.out.println("Site created.");
                } else {
                    System.out.println("Site creation failed.");
                }
            } else {
                System.out.println("Unknown command.");
            }
            System.out.println("");
        }
    }
}
