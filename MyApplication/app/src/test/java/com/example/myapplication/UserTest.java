package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.Notification;
import android.content.Context;
import android.content.res.AssetManager;

import androidx.core.app.NotificationManagerCompat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import helper_classes_and_methods.*;

@RunWith(MockitoJUnitRunner.class)
public class UserTest {

    private User user;

    @Mock
    Context mockContext;

    @Mock
    AssetManager mockAssetManager;

    @Mock
    NotificationManagerCompat mockNotificationManager;

    @Captor
    ArgumentCaptor<Integer> idCaptor;

    @Captor
    ArgumentCaptor<Notification> notificationCaptor;

    @Before
    public void setUp() {
        user = new User("testuser", "password", true, "userID");
    }

    @Test
    public void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    public void testGetPassword() {
        assertEquals("password", user.getPassword());
    }

    @Test
    public void testIsMaintainer() {
        assertTrue(user.isMaintainer());
    }

    @Test
    public void testGetUserID() {
        assertEquals("userID", user.getUserID());
    }

    @Test
    public void testSetUsername() {
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());
    }

    @Test
    public void testSetMaintainer() {
        user.setMaintainer(false);
        assertFalse(user.isMaintainer());
    }

    @Test
    public void testToString() {
        String expected = "User{username='testuser', isMaintainer=true}";
        assertEquals(expected, user.toString());
    }



    @Test
    public void testValidateUserCredentials_ValidUser() throws IOException {
        String csvContent = "username,password,isMaintainer,userID\n" +
                "testuser,password,true,userID";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        when(mockAssetManager.open(anyString())).thenReturn(inputStream);

        boolean isValid = user.validateUserCredentials("testuser", "password", mockAssetManager);
        assertTrue(isValid);
        assertEquals("testuser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertTrue(user.isMaintainer());
        assertEquals("userID", user.getUserID());
    }

    @Test
    public void testValidateUserCredentials_InvalidUser() throws IOException {
        String csvContent = "username,password,isMaintainer,userID\n" +
                "wronguser,password,true,userID";
        InputStream inputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));
        when(mockAssetManager.open(anyString())).thenReturn(inputStream);

        boolean isValid = user.validateUserCredentials("testuser", "password", mockAssetManager);
        assertFalse(isValid);
    }

    @Test
    public void testValidateUserCredentials_IOException() throws IOException {
        when(mockAssetManager.open(anyString())).thenThrow(new IOException());

        boolean isValid = user.validateUserCredentials("testuser", "password", mockAssetManager);
        assertFalse(isValid);
    }

}
