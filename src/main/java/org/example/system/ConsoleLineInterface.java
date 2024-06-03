package org.example.system;

import org.example.model.FreshdeskContact;

import java.util.Scanner;

public class ConsoleLineInterface {
    private final static Scanner scanner = new Scanner(System.in);

    public static void run() {
        System.out.println("Enter a github username: ");
        String githubUsername = readLine();
        System.out.println("Enter a freshdesk subdomain: ");
        String freshdeskSubdomain = readLine();

        try {
            GithubFreshdeskConnection connection = new GithubFreshdeskConnection(githubUsername,
                    freshdeskSubdomain);
            FreshdeskContact contact = connection.connect();
        } catch (Exception e) {
            System.out.println("Failed to connect Github and Freshdesk: " + e.getMessage());
        }
    }

    public static String readLine() {
        return scanner.nextLine();
    }
}
