package com.example.myapplication;

import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
//import org.robolectric.RuntimeEnvironment;
//import androidx.test.core.app.ApplicationProvider;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

//import org.robolectric.RobolectricTestRunner;


import android.content.Context;
import android.content.res.AssetManager;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import helper_classes_and_methods.*;
import helper_classes_and_methods.DataLoader;
import helper_classes_and_methods.parser.*;

/**
 * Author: Hongyu Li
 * Description: unit tests to make sure java methods are feasible
 */
//@RunWith(RobolectricTestRunner.class)
public class UnitTest {

    @Test(expected = IOException.class)
    public void testLoadData_FileNotFound() throws Exception {
        // preparation
        String fileName = "nonexistent.json";
        Context context = mock(Context.class);
        AssetManager assetManager = mock(AssetManager.class);

        // when using openFileInput throw FileNotFoundException
        when(context.openFileInput(fileName)).thenThrow(new FileNotFoundException("File not found"));
        // when calling getAssets() return the mocked AssetManager
        when(context.getAssets()).thenReturn(assetManager);
        // when opening file from asset throw IOException，no such assets
        when(assetManager.open(fileName)).thenThrow(new IOException("File not found in assets"));

        DataLoader dataLoader = new DataLoader(context);

        // execution
        dataLoader.loadData(fileName);
    }

//    BTree tests
    @Test
    public void testPut_NewKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling dwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));

        assertNull(bTree.put("123 Test St", dwelling));
        assertNotNull(bTree.get("123 Test St"));
    }

    @Test
    public void testPut_ExistingKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling oldDwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));
        bTree.put("123 Test St", oldDwelling);
        Dwelling newDwelling = new Dwelling("123 Test St", LocalDate.now().plusDays(1), BuildingMaterial.BRICK, new ArrayList<>(), null, new Dwelling.Location(0, 0));

        assertNotNull(bTree.put("123 Test St", newDwelling));
        assertEquals(BuildingMaterial.BRICK, bTree.get("123 Test St").getBuildingMaterial());
    }

    @Test
    public void testRemove_ExistingKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling dwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));
        bTree.put("123 Test St", dwelling);

        assertNotNull(bTree.remove("123 Test St"));
        assertNull(bTree.get("123 Test St"));
    }

    @Test

    public void testRemove_NonExistingKey() {
        BTree bTree = new BTree(5, String::compareTo);
        assertNull(bTree.remove("nonexistent"));
    }

//    parser&tokenizer tests
    @Test
    public void testValidConditionExpression() {
    String expr = "a : b";
    ExpressionParser parser = new ExpressionParser(expr);
    Expression result = parser.getExpression();
    assertNotNull(result);
    assertTrue(result instanceof Condition);
    assertEquals("a", ((Condition)result).getKey());
    assertEquals("b", ((Condition)result).getValue());
}

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpressionIllegal() {
        String expr = "a";
        ExpressionParser parser = new ExpressionParser(expr);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidExpressionIllegalFormat() {
        String expr = "a, b";
        ExpressionParser parser = new ExpressionParser(expr);
    }

    @Test
    public void testValidNotExpression() {
        String expr = "not (a:b)";
        ExpressionParser parser = new ExpressionParser(expr);
        Expression result = parser.getExpression();
        assertNotNull(result);
        assertTrue(result instanceof NotExp);
        assertTrue(((NotExp)result).getExpression() instanceof Condition);
    }
    @Test
    public void testValidAndExpression() {
        Expression left = new Expression();
        Expression right= new Expression();
        AndExp andExp= new AndExp(left,right);

        assertEquals(left, andExp.getLeft());


        Expression newLeft = new Expression();
        andExp.setLeft(newLeft);
        assertEquals(newLeft, andExp.getLeft());


        assertEquals(right, andExp.getRight());

        Expression newRight = new Expression();
        andExp.setRight(newRight);
        assertEquals(newRight, andExp.getRight());

        AndExp defaultAndExp = new AndExp();
        assertNull(defaultAndExp.getLeft());
        assertNull(defaultAndExp.getRight());
    }
    @Test
    public void testValidOrExpression() {
        Expression left = new Expression();
        Expression right= new Expression();
        OrExp orExp= new OrExp(left,right);

        assertEquals(left, orExp.getLeft());


        Expression newLeft = new Expression();
        orExp.setLeft(newLeft);
        assertEquals(newLeft, orExp.getLeft());


        assertEquals(right, orExp.getRight());

        Expression newRight = new Expression();
        orExp.setRight(newRight);
        assertEquals(newRight, orExp.getRight());

        OrExp defaultOrExp = new OrExp();
        assertNull(defaultOrExp.getLeft());
        assertNull(defaultOrExp.getRight());
    }

    @Test
    public void testParseExpressionWithComma() {

        String expr = "and (condition1:condition2, condition3:condition4)";
        ExpressionParser parser = new ExpressionParser(expr);
        Expression parsedExpr = parser.getExpression();
        assertNotNull(parsedExpr);
        assertTrue(parsedExpr instanceof AndExp);

        AndExp andExp = (AndExp) parsedExpr;
        Expression leftExpr = andExp.getLeft();
        Expression rightExpr = andExp.getRight();
        assertTrue(leftExpr instanceof Condition);
        assertTrue(rightExpr instanceof Condition);

        Condition leftCond = (Condition) leftExpr;
        Condition rightCond = (Condition) rightExpr;
        assertEquals("condition1", leftCond.getKey());
        assertEquals("condition2", leftCond.getValue());
        assertEquals("condition3", rightCond.getKey());
        assertEquals("condition4", rightCond.getValue());

        expr = "or (condition1:condition2, condition3:condition4)";
        parser = new ExpressionParser(expr);
        parsedExpr = parser.getExpression();
        assertNotNull(parsedExpr);
        assertTrue(parsedExpr instanceof OrExp);

        OrExp orExp = (OrExp) parsedExpr;
        leftExpr = orExp.getLeft();
        rightExpr = orExp.getRight();
        assertTrue(leftExpr instanceof Condition);
        assertTrue(rightExpr instanceof Condition);

        leftCond = (Condition) leftExpr;
        rightCond = (Condition) rightExpr;
        assertEquals("condition1", leftCond.getKey());
        assertEquals("condition2", leftCond.getValue());
        assertEquals("condition3", rightCond.getKey());
        assertEquals("condition4", rightCond.getValue());
    }


    @Test
    public void testConvertString() {
        assertEquals(TokenType.AND, TokenType.convertString("and"));
        assertEquals(TokenType.OR, TokenType.convertString("or"));
        assertEquals(TokenType.NOT, TokenType.convertString("not"));
        assertEquals(TokenType.COMMA, TokenType.convertString(","));
        assertEquals(TokenType.COLON, TokenType.convertString(":"));
        assertEquals(TokenType.LEFT_BRA, TokenType.convertString("("));
        assertEquals(TokenType.RIGHT_BRA, TokenType.convertString(")"));
        assertEquals(TokenType.CONDITION, TokenType.convertString("condition"));
        assertEquals(TokenType.CONDITION, TokenType.convertString("unknown"));
    }


    @Test
    public void testInvalidTokenlist(){
        ExpressionParser expressionParser = new ExpressionParser("");
        List<Token> tokenList= new ArrayList<>();
        Expression result= expressionParser.getExpression();
        assertNull(result);

        List<Token> tokenList2=null;
        Expression result2= expressionParser.getExpression();
        assertNull(result2);
    }


    @Test
    public void testGetContext(){
        Token token= new Token("test content",TokenType.AND);
        String content =token.getContent();
        assertEquals("test content",content);
    }

//   building material test
    @Test
    public void testBuildingMaterial(){
        BuildingMaterial buildingMaterial = BuildingMaterial.BRICK;
        assertEquals(120,buildingMaterial.getInitialStrength());
        assertEquals(0.006,buildingMaterial.getCorrosionFactor(),0.0);

        assertEquals(50,buildingMaterial.getRepairThreshold());

    }

    @Test
    public void testGetCurrentTimestamp() {
        String timestamp = TimeUtil.getCurrentTimestamp();

        // Define the expected date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        // Parse the timestamp to check if it's in the correct format
        try {
            sdf.parse(timestamp);
        } catch (ParseException e) {
            fail("Timestamp format is incorrect: " + timestamp);
        }

        // Check the length of the timestamp string
        assertEquals("Timestamp length is incorrect", 19, timestamp.length());
    }

    @Test
    public void testLocation(){
        Dwelling.Location location= new Dwelling.Location();
        location.setLat(123.45);
        assertEquals(123.45,location.getLat(),0.0);
        location.setLng(345.56);
        assertEquals(345.56,location.getLng(),0.0);
    }

//    btree node test


    @Test
    public void testSetParent() {
        Comparator<String> comparator = String::compareTo;
        BTreeNode node = new BTreeNode(comparator);
        BTreeNode parent = new BTreeNode(comparator);
        node.setParent(parent);
        assertEquals(parent, node.getParent());
    }

    @Test
    public void testGetParent() {
        Comparator<String> comparator = String::compareTo;
        BTreeNode node = new BTreeNode(comparator);
        BTreeNode parent = new BTreeNode(comparator);
        node.setParent(parent);
        BTreeNode actualParent = node.getParent();
        assertEquals(parent, actualParent);
    }

    @Test
    public void testToString() {
        Comparator<String> comparator = String::compareTo;
        BTreeNode node = new BTreeNode(comparator);
        BTreeNode parent = new BTreeNode(comparator);
        node.children.add(new BTreeNode(comparator));
        String expectedString = "BTreeNode{elements=[], children=[BTreeNode{elements=[], children=[]}]}";
        assertEquals(expectedString, node.toString());
    }




}