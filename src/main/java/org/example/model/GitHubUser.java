package org.example.model;

public class GitHubUser {
    private final String login;
    private final String name;
    private final String email;
    private final String created_at;

    public GitHubUser(String login, String name, String email, String createdAt) {
        this.login = login;
        this.name = name;
        created_at = createdAt;
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "GitHubUser{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
