package org.example;

import org.example.model.GitHubUser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GitHubUserTest {

    private static final String LOGIN = "octocat";
    private static final String NAME = "The Octocat";
    private static final String EMAIL = "octocat@github.com";
    private static final String CREATED_AT = "2011-01-25T18:44:36Z";

    @Test
    public void testConstructor() {
        GitHubUser user = new GitHubUser(LOGIN, NAME, EMAIL, CREATED_AT);

        assertNotNull(user);
        assertEquals(LOGIN, user.getLogin());
        assertEquals(NAME, user.getName());
        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testGetLogin() {
        GitHubUser user = new GitHubUser(LOGIN, NAME, EMAIL, CREATED_AT);

        assertEquals(LOGIN, user.getLogin());
    }

    @Test
    public void testGetName() {
        GitHubUser user = new GitHubUser(LOGIN, NAME, EMAIL, CREATED_AT);

        assertEquals(NAME, user.getName());
    }

    @Test
    public void testGetEmail() {
        GitHubUser user = new GitHubUser(LOGIN, NAME, EMAIL, CREATED_AT);

        assertEquals(EMAIL, user.getEmail());
    }

    @Test
    public void testToString() {
        GitHubUser user = new GitHubUser(LOGIN, NAME, EMAIL, CREATED_AT);

        String expected = "GitHubUser{" +
                "login='" + LOGIN + '\'' +
                ", name='" + NAME + '\'' +
                ", email='" + EMAIL + '\'' +
                ", created_at='" + CREATED_AT + '\'' +
                '}';
        assertEquals(expected, user.toString());
    }
}
