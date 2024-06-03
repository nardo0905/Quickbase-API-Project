package org.example.api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.example.model.FreshdeskContact;
import org.example.model.GitHubUser;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

public class FreshdeskAPI {
    private static final String FRESHDESK_API_URL = "/api/v2/";
    private static final String FRESHDESK_API_TOKEN = System.getenv("FRESHDESK_TOKEN");
    private static final int SUCCESS = 200;

    private final HttpClient client;
    private final Gson gson;

    private final String subdomain;
    private final GitHubUser githubUser;

    public FreshdeskAPI(HttpClient client, Gson gson, String subdomain, GitHubUser githubUser) {
        this.client = client;
        this.gson = gson;
        this.subdomain = subdomain;
        this.githubUser = githubUser;
    }

    public FreshdeskContact createOrUpdateContact() throws Exception {
        FreshdeskContact contact = getContact();
        if (contact != null) {
            return updateContact(contact);
        }
        return createContact();
    }

    private FreshdeskContact updateContact(FreshdeskContact contact) throws Exception {
        contact.setName(githubUser.getName());
        contact.setEmail(githubUser.getEmail());
        HttpRequest putRequest = HttpRequest.newBuilder()
                .uri(buildContactsUpdateURI(contact.getId()))
                .header("Content-Type", "application/json")
                .header("Authorization", buildAuthorizationHeader())
                .PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(contact)))
                .build();

        HttpResponse<String> response = client.send(putRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != SUCCESS) {
            throw new Exception("Failed to update contact. Status code: " + response.statusCode());
        }
        return gson.fromJson(response.body(), FreshdeskContact.class);
    }

    public FreshdeskContact createContact() throws Exception {
        JsonObject contact = buildNewContactJson();
        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(buildContactsURI())
                .header("Content-Type", "application/json")
                .header("Authorization", buildAuthorizationHeader())
                .POST(HttpRequest.BodyPublishers.ofString(contact.toString()))
                .build();

        HttpResponse<String> response = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != SUCCESS) {
            throw new Exception("Failed to create contact. Status code: " + response.statusCode());
        }
        return gson.fromJson(response.body(), FreshdeskContact.class);
    }

    public FreshdeskContact getContact() throws Exception {
        String email = githubUser.getEmail();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(buildContactsFilterURI(email))
                .header("Authorization", buildAuthorizationHeader())
                .GET()
                .build();

        HttpResponse<String> response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != SUCCESS) {
            throw new Exception("Failed to get contact. Status code: " + response.statusCode());
        }
        FreshdeskContact[] contacts = gson.fromJson(response.body(), FreshdeskContact[].class);
        if (contacts.length == 0) {
            return null;
        }
        return contacts[0];
    }

    private String buildAuthorizationHeader() {
        String auth = FRESHDESK_API_TOKEN + ":X";
        return "Basic " + Base64.getEncoder().encodeToString(auth.getBytes());
    }

    private URI buildContactsURI() throws URISyntaxException {
        return new URI("https://" + subdomain + ".freshdesk.com" + FRESHDESK_API_URL + "contacts");
    }

    private URI buildContactsUpdateURI(long contactId) throws URISyntaxException {
        return new URI("https://" + subdomain + ".freshdesk.com" + FRESHDESK_API_URL + "contacts/" + contactId);
    }

    private URI buildContactsFilterURI(String email) throws URISyntaxException {
        return new URI("https://" + subdomain + ".freshdesk.com" + FRESHDESK_API_URL + "contacts?email=" + email);
    }

    private JsonObject buildNewContactJson() {
        JsonObject contact = new JsonObject();
        contact.add("name", gson.toJsonTree(githubUser.getName()));
        contact.add("email", gson.toJsonTree(githubUser.getEmail()));
        System.out.println("contact: " + contact);
        return contact;
    }
}
