package org.example;

import com.google.gson.Gson;
import org.example.api.FreshdeskAPI;
import org.example.model.FreshdeskContact;
import org.example.model.GitHubUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class FreshdeskAPITest {

    private static final String TEST_SUBDOMAIN = "company";
    private static final String TEST_TOKEN = "test_token";
    private static final String TEST_EMAIL = "octocat@github.com";
    private static final String TEST_NAME = "The Octocat";
    private static final String TEST_CREATED_AT = "2021-01-01T00:00:00Z";

    @Mock
    private HttpClient mockClient;

    private FreshdeskAPI freshdeskAPI;

    @BeforeEach
    public void setUp() {
        mockClient = mock(HttpClient.class);
        Gson gson = new Gson();
        GitHubUser githubUser = new GitHubUser(TEST_EMAIL, TEST_NAME, TEST_EMAIL, TEST_CREATED_AT);
        freshdeskAPI = new FreshdeskAPI(mockClient, gson, TEST_SUBDOMAIN, githubUser);

        System.setProperty("FRESHDESK_TOKEN", TEST_TOKEN);
    }

    @Test
    public void testGetContactSuccess() throws Exception {
        String jsonResponse = "[{\"id\":1, \"name\":\"The Octocat\", \"email\":\"octocat@github.com\"}]";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        FreshdeskContact contact = freshdeskAPI.getContact();

        assertEquals(1, contact.getId());
        assertEquals(TEST_NAME, contact.getName());
        assertEquals(TEST_EMAIL, contact.getEmail());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testGetContactNotFound() throws Exception {
        String jsonResponse = "[]";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        FreshdeskContact contact = freshdeskAPI.getContact();

        assertNull(contact);

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testGetContactFailure() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(404);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Exception exception = assertThrows(Exception.class, () -> {
            freshdeskAPI.getContact();
        });

        assertEquals("Failed to get contact. Status code: 404", exception.getMessage());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testCreateContactSuccess() throws Exception {
        String jsonResponse = "{\"id\":1, \"name\":\"The Octocat\", \"email\":\"octocat@github.com\"}";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        FreshdeskContact contact = freshdeskAPI.createContact();

        assertEquals(1, contact.getId());
        assertEquals(TEST_NAME, contact.getName());
        assertEquals(TEST_EMAIL, contact.getEmail());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testCreateContactFailure() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        Exception exception = assertThrows(Exception.class, () -> {
            freshdeskAPI.createContact();
        });

        assertEquals("Failed to create contact. Status code: 500", exception.getMessage());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testUpdateContactSuccess() throws Exception {
        String jsonResponse = "{\"id\":1, \"name\":\"The Octocat\", \"email\":\"octocat@github.com\"}";
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        FreshdeskContact existingContact = new FreshdeskContact(TEST_NAME, TEST_EMAIL, 1);

        FreshdeskContact contact = freshdeskAPI.updateContact(existingContact);

        assertEquals(1, contact.getId());
        assertEquals(TEST_NAME, contact.getName());
        assertEquals(TEST_EMAIL, contact.getEmail());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testUpdateContactFailure() throws Exception {
        HttpResponse<String> mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(500);

        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(mockResponse);

        FreshdeskContact existingContact = new FreshdeskContact();
        existingContact.setName("Old Name");
        existingContact.setEmail(TEST_EMAIL);

        Exception exception = assertThrows(Exception.class, () -> {
            freshdeskAPI.updateContact(existingContact);
        });

        assertEquals("Failed to update contact. Status code: 500", exception.getMessage());

        verify(mockClient, times(1)).send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class));
    }

    @Test
    public void testCreateOrUpdateContactCreate() throws Exception {
        FreshdeskAPI spyApi = spy(freshdeskAPI);
        doReturn(null).when(spyApi).getContact();
        doReturn(new FreshdeskContact()).when(spyApi).createContact();

        spyApi.createOrUpdateContact();

        verify(spyApi, times(1)).getContact();
        verify(spyApi, times(1)).createContact();
        verify(spyApi, never()).updateContact(any(FreshdeskContact.class));
    }

    @Test
    public void testCreateOrUpdateContactUpdate() throws Exception {
        FreshdeskAPI spyApi = spy(freshdeskAPI);
        FreshdeskContact existingContact = new FreshdeskContact();
        doReturn(existingContact).when(spyApi).getContact();
        doReturn(existingContact).when(spyApi).updateContact(any(FreshdeskContact.class));

        spyApi.createOrUpdateContact();

        verify(spyApi, times(1)).getContact();
        verify(spyApi, times(1)).updateContact(any(FreshdeskContact.class));
        verify(spyApi, never()).createContact();
    }
}
