package com.example.myapplication;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import helper_classes_and_methods.FireAlarmStorageHandler;
import helper_classes_and_methods.LoginStorageHandler;
import helper_classes_and_methods.StorageFactory;
import helper_classes_and_methods.StorageHandler;

@RunWith(MockitoJUnitRunner.class)
public class StorageFactoryTest {
    @Mock
    Context mockContext;

    @Before
    public void setUp() {
        // Any setup code can go here if needed
    }

    @Test
    public void testGetStorageHandler_Login() {
        StorageHandler handler = StorageFactory.getStorageHandler(mockContext, StorageFactory.HandlerType.LOGIN);
        assertNotNull(handler);
        assertTrue(handler instanceof LoginStorageHandler);
    }

    @Test
    public void testGetStorageHandler_FireAlarm() {
        StorageHandler handler = StorageFactory.getStorageHandler(mockContext, StorageFactory.HandlerType.FIRE_ALARM);
        assertNotNull(handler);
        assertTrue(handler instanceof FireAlarmStorageHandler);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStorageHandler_NullType() {
        StorageFactory.getStorageHandler(mockContext, null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void testGetStorageHandler_UnknownType() {
        // Simulating an unknown type by creating a new instance of HandlerType that is not in the enum
        StorageFactory.getStorageHandler(mockContext, StorageFactory.HandlerType.valueOf("UNKNOWN"));
    }
}
