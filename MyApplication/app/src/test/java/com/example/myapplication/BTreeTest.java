package com.example.myapplication;

import helper_classes_and_methods.BTree;
import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.SearchResult;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Comparator;
import java.util.List;

public class BTreeTest {

    private BTree bTree;
    private Comparator<String> stringComparator = Comparator.naturalOrder();

    @Before
    public void setUp() {
        // Initialize BTree with degree 3 for testing
        bTree = new BTree(3, stringComparator);
    }

    @Test
    public void testInsertIntoEmptyTree() {
        bTree.put("key1", new Dwelling()); // Assuming Dwelling is a predefined class
        assertNotNull("The key should be found after insertion", bTree.get("key1"));
    }

    @Test
    public void testInsertCausesSplit() {
        bTree.put("key1", new Dwelling());
        bTree.put("key2", new Dwelling());
        bTree.put("key3", new Dwelling()); // This should cause a split
        assertNotNull("The key should still be found after split", bTree.get("key3"));
    }

    @Test
    public void testRemoveLeafNoUnderflow() {
        bTree.put("key1", new Dwelling());
        bTree.put("key2", new Dwelling());
        bTree.remove("key1");
        assertNull("The key should be removed", bTree.get("key1"));
        assertNotNull("Other keys should still exist", bTree.get("key2"));
    }

    @Test
    public void testRemoveCausesUnderflow() {
        bTree.put("key1", new Dwelling());
        bTree.put("key2", new Dwelling());
        bTree.put("key3", new Dwelling());
        bTree.remove("key1");
        // Further assertions can be based on the expected state of the tree
    }


    @Test
    public void testDeleteCausesMerge() {
        for (int i = 0; i < 6; i++) {
            bTree.put("key" + i, new Dwelling());
        }
        bTree.remove("key0"); // This may cause underflow depending on the tree's degree and handling
        assertNull("Element should be removed", bTree.get("key0"));
        // Add checks for structure if specific behavior is implemented in the merge logic
    }

    @Test
    public void testSearchForExistingElement() {
        bTree.put("key1", new Dwelling());
        Dwelling result = bTree.get("key1");
        assertNotNull("Element should be found", result);
    }

    @Test
    public void testSearchForNonExistingElement() {
        assertNull("Element should not be found", bTree.get("nonExistingKey"));
    }


    @Test
    public void testInsertManyElements() {
        for (int i = 0; i < 1000; i++) {
            bTree.put("key" + i, new Dwelling());
        }
        // Check for a random element to ensure tree integrity
        assertNotNull("Random element should be retrievable", bTree.get("key999"));
    }









}
