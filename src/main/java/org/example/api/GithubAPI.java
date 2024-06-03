package org.example.api;

import com.google.gson.Gson;
import org.example.model.GitHubUser;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class GithubAPI {
    private static final String GITHUB_API_URL = "https://api.github.com";
    private static final String GITHUB_API_TOKEN = System.getenv("GITHUB_TOKEN");
    private static final int SUCCESS = 200;

    private final HttpClient client;
    private final Gson gson;

    private final String username;

    public GithubAPI(HttpClient client, Gson gson, String username) {
        this.client = client;
        this.gson = gson;
        this.username = username;
    }

    public GitHubUser getUserInfo() throws Exception {
        // System.out.println("GITHUB_API_TOKEN: " + GITHUB_API_TOKEN);
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(buildURI())
                .header("Accept", "application/vnd.github+json")
                .header("Authorization", buildAuthorizationHeader())
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        // System.out.println(response.body());
        if (response.statusCode() != SUCCESS) {
            throw new Exception("Failed to get user info. Status code: " + response.statusCode());
        }
        return gson.fromJson(response.body(), GitHubUser.class);
    }

    private URI buildURI() throws URISyntaxException {
        return new URI(GITHUB_API_URL + "/users/" + username);
    }

    private String buildAuthorizationHeader() {
        return "Bearer " + GITHUB_API_TOKEN;
    }
}
