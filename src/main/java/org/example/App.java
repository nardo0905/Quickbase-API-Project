package org.example;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.api.GithubAPI;

import java.net.http.HttpClient;

public class App 
{
    public static void main( String[] args )
    {
        HttpClient client = HttpClient.newHttpClient();
        Gson gson = new Gson();
        GithubAPI githubAPI = new GithubAPI(client, gson, "nardo0905");
        try {
            System.out.println(githubAPI.getUserInfo());
            FreshdeskAPI freshdeskAPI = new FreshdeskAPI(client, gson, "nardo0905", githubAPI.getUserInfo());
            freshdeskAPI.createOrUpdateContact();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
