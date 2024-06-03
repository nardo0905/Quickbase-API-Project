package org.example;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.api.GithubAPI;
import org.example.model.FreshdeskContact;
import org.example.system.GithubFreshdeskConnection;

import java.net.http.HttpClient;

public class App 
{
    public static void main( String[] args )
    {
        try {
            GithubFreshdeskConnection connection = new GithubFreshdeskConnection("nardo0905",
                    "example");
            FreshdeskContact contact = connection.connect();
        } catch (Exception e) {
            System.out.println("Failed to connect to Github and Freshdesk: " + e.getMessage());
        }
    }
}
