package org.example;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.api.GithubAPI;
import org.example.model.FreshdeskContact;
import org.example.system.ConsoleLineInterface;
import org.example.system.GithubFreshdeskConnection;

import java.net.http.HttpClient;

public class App {
    public static void main( String[] args ) {
        ConsoleLineInterface.run();
    }
}
