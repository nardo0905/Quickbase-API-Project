package org.example.system;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.api.GithubAPI;
import org.example.model.FreshdeskContact;
import org.example.model.GitHubUser;

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
        // System.out.println(githubAPI.getUserInfo());
        addUserToDatabase(githubAPI.getUserInfo());

        try {
            FreshdeskAPI freshdeskAPI = new FreshdeskAPI(client, gson, freshdeskSubdomain, githubAPI.getUserInfo());
            return freshdeskAPI.createOrUpdateContact();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void addUserToDatabase(GitHubUser githubUser) {
        String login = githubUser.getLogin();
        String name = githubUser.getName();
        String createdAt = githubUser.getCreatedAt();
        try {
            DatabaseIntegration databaseIntegration = new DatabaseIntegration();
            databaseIntegration.connectToDatabase();
            databaseIntegration.addUser(login, name, createdAt);
            databaseIntegration.closeConnection();
        } catch (Exception e) {
            System.out.println("Failed to add user to database: " + e.getMessage());
        }

    }
}
