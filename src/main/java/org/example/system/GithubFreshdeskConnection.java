package org.example.system;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.api.GithubAPI;
import org.example.model.FreshdeskContact;

import java.net.http.HttpClient;

public class GithubFreshdeskConnection {
    public static final HttpClient client = HttpClient.newHttpClient();
    public static final Gson gson = new Gson();

    public final String githubUsername;
    public final String freshdeskSubdomain;

    public GithubFreshdeskConnection(String githubUsername, String freshdeskSubdomain) {
        this.githubUsername = githubUsername;
        this.freshdeskSubdomain = freshdeskSubdomain;
    }

    public FreshdeskContact connect() throws Exception {
        GithubAPI githubAPI = new GithubAPI(client, gson, githubUsername);
        System.out.println(githubAPI.getUserInfo());
        try {
            FreshdeskAPI freshdeskAPI = new FreshdeskAPI(client, gson, freshdeskSubdomain, githubAPI.getUserInfo());
            return freshdeskAPI.createOrUpdateContact();
        } catch (Exception e) {
            throw new Exception("Failed to connect to Github: " + e.getMessage());
        }
    }
}
