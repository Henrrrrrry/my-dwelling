package com.example.myapplication;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationManagerCompat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;

import helper_classes_and_methods.DataLoader;
import helper_classes_and_methods.User;


public class ExampleUnitTest {
//    @Test
//    public void testLoadData_Success() throws Exception {
//        String fileName = "test.json";
//        String jsonData = "[{\"address\":\"123 Test St\"}]";
//        Context context = mock(Context.class);
//        ByteArrayInputStream inputStream = new ByteArrayInputStream(jsonData.getBytes("UTF-8"));
//        when(context.openFileInput(fileName)).thenReturn(inputStream);
//
//        DataLoader dataLoader = new DataLoader(context);
//
//        // 执行
//        JSONArray result = dataLoader.loadData(fileName);
//
//        // 验证
//        assertNotNull(result);
//        assertEquals(1, result.length());
//        assertEquals("123 Test St", result.getJSONObject(0).getString("address"));
//    }

//    @Test(expected = IOException.class)
//    public void testLoadData_FileNotFound() throws Exception {
//        String fileName = "nonexistent.json";
//        Context context = mock(Context.class);
//        when(context.openFileInput(fileName)).thenThrow(new FileNotFoundException());
//
////        DataLoader dataLoader = new DataLoader(context);
////        dataLoader.loadData(fileName);
//    }



}