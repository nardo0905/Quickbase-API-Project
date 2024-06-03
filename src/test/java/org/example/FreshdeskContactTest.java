package org.example;

import org.example.model.FreshdeskContact;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FreshdeskContactTest {

    private static final String NAME = "John Doe";
    private static final String EMAIL = "johndoe@example.com";
    private static final int ID = 12345;

    @Test
    public void testConstructorWithParameters() {
        FreshdeskContact contact = new FreshdeskContact(NAME, EMAIL, ID);

        assertNotNull(contact);
        assertEquals(NAME, contact.getName());
        assertEquals(EMAIL, contact.getEmail());
        assertEquals(ID, contact.getId());
    }

    @Test
    public void testDefaultConstructor() {
        FreshdeskContact contact = new FreshdeskContact();

        assertNotNull(contact);
        assertEquals("", contact.getName());
        assertEquals("", contact.getEmail());
        assertEquals(0, contact.getId());
    }

    @Test
    public void testGetName() {
        FreshdeskContact contact = new FreshdeskContact(NAME, EMAIL, ID);

        assertEquals(NAME, contact.getName());
    }

    @Test
    public void testSetName() {
        FreshdeskContact contact = new FreshdeskContact();
        contact.setName(NAME);

        assertEquals(NAME, contact.getName());
    }

    @Test
    public void testGetEmail() {
        FreshdeskContact contact = new FreshdeskContact(NAME, EMAIL, ID);

        assertEquals(EMAIL, contact.getEmail());
    }

    @Test
    public void testSetEmail() {
        FreshdeskContact contact = new FreshdeskContact();
        contact.setEmail(EMAIL);

        assertEquals(EMAIL, contact.getEmail());
    }

    @Test
    public void testGetId() {
        FreshdeskContact contact = new FreshdeskContact(NAME, EMAIL, ID);

        assertEquals(ID, contact.getId());
    }

    @Test
    public void testToString() {
        FreshdeskContact contact = new FreshdeskContact(NAME, EMAIL, ID);

        String expected = "FreshdeskContact{" +
                "name='" + NAME + '\'' +
                ", email='" + EMAIL + '\'' +
                ", id=" + ID +
                '}';
        assertEquals(expected, contact.toString());
    }
}
