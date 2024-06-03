package org.example;

import com.google.gson.Gson;
import org.example.api.GithubAPI;
import org.example.model.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GithubAPITest {

    private static final String TEST_USERNAME = "octocat";
    private static final String TEST_TOKEN = "test_token";

    @Mock
    private HttpClient mockClient;

    private Gson gson;
    private GithubAPI githubAPI;

    @BeforeEach
    public void setUp() {
        mockClient = mock(HttpClient.class);
        gson = new Gson();
        githubAPI = new GithubAPI(mockClient, gson, TEST_USERNAME);

        // Set the environment variable for the test
        System.setProperty("GITHUB_TOKEN", TEST_TOKEN);
    }

    @Test
    public void testGetUserInfoSuccess() throws Exception {
        String jsonResponse = "{\"login\":\"octocat\", \"name\":\"The Octocat\", \"email\":\"octocat@github.com\"}";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        GitHubUser user = githubAPI.getUserInfo();

        assertEquals("octocat", user.getLogin());
        assertEquals("The Octocat", user.getName());
        assertEquals("octocat@github.com", user.getEmail());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testGetUserInfoFailure() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Exception exception = assertThrows(Exception.class, () -> {
            githubAPI.getUserInfo();
        });

        assertEquals("Failed to get user info. Status code: 404", exception.getMessage());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }
}
