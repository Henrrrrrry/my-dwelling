package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.*;

import java.util.Comparator;
import java.util.List;

import helper_classes_and_methods.*;

public class BTreeTest {
    private BTree bTree;
    private Comparator<String> comparator;

    @Before
    public void setUp() {
        comparator = String::compareTo;
        bTree = new BTree(3, comparator); // BTree with minimum degree 3
    }

    @Test
    public void testPutAndGet() {
        Dwelling dwelling1 = mock(Dwelling.class);
        Dwelling dwelling2 = mock(Dwelling.class);
        bTree.put("key1", dwelling1);
        bTree.put("key2", dwelling2);

        assertEquals(dwelling1, bTree.get("key1"));
        assertEquals(dwelling2, bTree.get("key2"));
    }

    @Test
    public void testPut_UpdateExistingKey() {
        Dwelling dwelling1 = mock(Dwelling.class);
        Dwelling dwelling2 = mock(Dwelling.class);
        bTree.put("key1", dwelling1);

        assertEquals(dwelling1, bTree.put("key1", dwelling2));
        assertEquals(dwelling2, bTree.get("key1"));
    }

    @Test
    public void testRemove_KeyExists() {
        Dwelling dwelling1 = mock(Dwelling.class);
        Dwelling dwelling2 = mock(Dwelling.class);
        bTree.put("key1", dwelling1);
        bTree.put("key2", dwelling2);

        assertEquals(dwelling1, bTree.remove("key1"));
        assertNull(bTree.get("key1"));
        assertEquals(dwelling2, bTree.get("key2"));
    }

    @Test
    public void testRemove_KeyDoesNotExist() {
        Dwelling dwelling1 = mock(Dwelling.class);
        bTree.put("key1", dwelling1);

        assertNull(bTree.remove("key2"));
        assertEquals(dwelling1, bTree.get("key1"));
    }

    @Test
    public void testGet_KeyDoesNotExist() {
        assertNull(bTree.get("key1"));
    }

    @Test
    public void testGetDwellings() {
        Dwelling dwelling1 = mock(Dwelling.class);
        Dwelling dwelling2 = mock(Dwelling.class);
        Dwelling dwelling3 = mock(Dwelling.class);
        bTree.put("key1", dwelling1);
        bTree.put("key3", dwelling3);
        bTree.put("key2", dwelling2);

        List<Dwelling> dwellings = bTree.getDwellings();
        assertEquals(3, dwellings.size());
        assertTrue(dwellings.contains(dwelling1));
        assertTrue(dwellings.contains(dwelling2));
        assertTrue(dwellings.contains(dwelling3));
    }

    @Test
    public void testInsertElement_NodeExceedsMaxKeySize() {
        // This test ensure that nodes are split correctly when they exceed the max key size
        Dwelling dwelling1 = mock(Dwelling.class);
        Dwelling dwelling2 = mock(Dwelling.class);
        Dwelling dwelling3 = mock(Dwelling.class);
        Dwelling dwelling4 = mock(Dwelling.class);
        Dwelling dwelling5 = mock(Dwelling.class);

        bTree.put("key1", dwelling1);
        bTree.put("key2", dwelling2);
        bTree.put("key3", dwelling3);
        bTree.put("key4", dwelling4);
        bTree.put("key5", dwelling5);

        assertEquals(dwelling1, bTree.get("key1"));
        assertEquals(dwelling2, bTree.get("key2"));
        assertEquals(dwelling3, bTree.get("key3"));
        assertEquals(dwelling4, bTree.get("key4"));
        assertEquals(dwelling5, bTree.get("key5"));
    }


}
