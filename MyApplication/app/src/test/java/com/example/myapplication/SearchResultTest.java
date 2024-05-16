package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.*;

import helper_classes_and_methods.*;

public class SearchResultTest {
    private BTreeNode mockNode1;
    private BTreeNode mockNode2;
    private SearchResult searchResult1;
    private SearchResult searchResult2;
    private SearchResult searchResult3;

    @Before
    public void setUp() {
        mockNode1 = mock(BTreeNode.class);
        mockNode2 = mock(BTreeNode.class);

        searchResult1 = new SearchResult(true, mockNode1, 1);
        searchResult2 = new SearchResult(true, mockNode1, 1);  // Same attributes as searchResult1
        searchResult3 = new SearchResult(false, mockNode2, 2); // Different attributes
    }



    @Test
    public void testEquals_DifferentClass() {
        assertFalse(searchResult1.equals("String"));
    }

    @Test
    public void testEquals_EqualObjects() {
        assertTrue(searchResult1.equals(searchResult2));
    }

    @Test
    public void testEquals_DifferentFound() {
        SearchResult differentFound = new SearchResult(false, mockNode1, 1);
        assertFalse(searchResult1.equals(differentFound));
    }

    @Test
    public void testEquals_DifferentNode() {
        assertFalse(searchResult1.equals(searchResult3));
    }

    @Test
    public void testEquals_DifferentIndex() {
        SearchResult differentIndex = new SearchResult(true, mockNode1, 2);
        assertFalse(searchResult1.equals(differentIndex));
    }

    @Test
    public void testHashCode_EqualObjects() {
        assertEquals(searchResult1.hashCode(), searchResult2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects() {
        assertNotEquals(searchResult1.hashCode(), searchResult3.hashCode());
    }

    @Test
    public void testToString() {
        String expectedString = "SearchResult{found=true, node=" + mockNode1 + ", index=1}";
        assertEquals(expectedString, searchResult1.toString());
    }

    @Test
    public void testIsFound() {
        assertTrue(searchResult1.isFound());
        assertFalse(searchResult3.isFound());
    }

    @Test
    public void testGetNode() {
        assertEquals(mockNode1, searchResult1.getNode());
        assertEquals(mockNode2, searchResult3.getNode());
    }

    @Test
    public void testGetIndex() {
        assertEquals(1, searchResult1.getIndex());
        assertEquals(2, searchResult3.getIndex());
    }
}
