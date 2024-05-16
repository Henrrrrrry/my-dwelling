package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.*;

import java.time.LocalDate;
import java.util.ArrayList;

import helper_classes_and_methods.*;

public class DwellingTest {
    private Dwelling dwelling;
    private BuildingMaterial mockBuildingMaterial;
    private User mockMaintainer;
    private Observer mockObserver;
    private Dwelling.Location location;

    @Before
    public void setUp() {
        mockBuildingMaterial = mock(BuildingMaterial.class);
        mockMaintainer = mock(User.class);
        mockObserver = mock(Observer.class);
        location = new Dwelling.Location(35.6895, 139.6917);

        when(mockBuildingMaterial.getInitialStrength()).thenReturn((int) 10.0);
        when(mockBuildingMaterial.getCorrosionFactor()).thenReturn(0.1);
        when(mockBuildingMaterial.getRepairThreshold()).thenReturn((int) 5.0);

        ArrayList<Observer> observers = new ArrayList<>();
        observers.add(mockObserver);

        dwelling = new Dwelling("123 Main St", LocalDate.of(2020, 1, 1), mockBuildingMaterial, observers, mockMaintainer, location);
    }

    @Test
    public void testNeedsRepair_NoRepairNeeded() {
        dwelling.setLastRepairDate(LocalDate.now().minusDays(1));
        assertFalse(dwelling.needsRepair());
    }

    @Test
    public void testNeedsRepair_RepairNeeded() {
        dwelling.setLastRepairDate(LocalDate.now().minusDays(100));
        assertTrue(dwelling.needsRepair());
    }

    @Test
    public void testGetSeismicRating() {
        dwelling.setLastRepairDate(LocalDate.now().minusDays(100));
        int rating = dwelling.getSeismicRating();
        assertTrue(rating >= 3 && rating <= 8);
    }

    @Test
    public void testRepair() {
        dwelling.repair();
        assertEquals(LocalDate.now(), dwelling.getLastRepairDate());
        assertEquals(3.0, dwelling.getSeismicRating(), 0.1);
        assertEquals("NormalState", dwelling.getDwellingState().getClass().getSimpleName());
    }

    @Test
    public void testAttach() {
        Observer newObserver = mock(Observer.class);
        dwelling.attach(newObserver);
        assertTrue(dwelling.getObservers().contains(newObserver));
    }

    @Test
    public void testDetach() {
        dwelling.detach(mockObserver);
        assertFalse(dwelling.getObservers().contains(mockObserver));
    }

    @Test
    public void testSetLastRepairDate() {
        LocalDate newDate = LocalDate.of(2023, 1, 1);
        dwelling.setLastRepairDate(newDate);
        assertEquals(newDate, dwelling.getLastRepairDate());
    }

    @Test
    public void testSetFireAlarm() {
        dwelling.setFireAlarm(true);
        assertTrue(dwelling.isFireAlarm());
    }

    @Test
    public void testSetLocation() {
        Dwelling.Location newLocation = new Dwelling.Location(40.7128, -74.0060);
        dwelling.setLocation(newLocation);
        assertEquals(newLocation, dwelling.getLocation());
    }

    @Test
    public void testGetAddress() {
        assertEquals("123 Main St", dwelling.getAddress());
    }

    @Test
    public void testGetLastRepairDate() {
        assertEquals(LocalDate.of(2020, 1, 1), dwelling.getLastRepairDate());
    }

    @Test
    public void testGetConstructionDate() {
        assertEquals(LocalDate.of(2020, 1, 1), dwelling.getConstructionDate());
    }

    @Test
    public void testIsFireAlarm() {
        assertFalse(dwelling.isFireAlarm());
    }

    @Test
    public void testGetObservers() {
        assertTrue(dwelling.getObservers().contains(mockObserver));
    }

    @Test
    public void testGetLocation() {
        assertEquals(location, dwelling.getLocation());
    }

    @Test
    public void testGetDwellingState() {
        assertEquals("NormalState", dwelling.getDwellingState().getClass().getSimpleName());
    }

    @Test
    public void testGetMaintainer() {
        assertEquals(mockMaintainer, dwelling.getMaintainer());
    }
    @Test
    public void testEmptyConstructor(){
        Dwelling dwelling1= new Dwelling();
        assertNull(dwelling1.getDwellingState());
        assertNull(dwelling1.getAddress());
        assertNull(dwelling1.getLastRepairDate());
        assertNull(dwelling1.getBuildingMaterial());
        assertNull(dwelling1.getObservers());
        assertNull(dwelling1.getConstructionDate());
        assertNull(dwelling1.getLocation());
        assertThrows(NullPointerException.class, dwelling1::getSeismicRating);
        assertFalse(dwelling1.isFireAlarm());
        assertThrows(NullPointerException.class, dwelling1::needsRepair);



    }
}
