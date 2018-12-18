package com.ecoone.mindfulmealplanner;

import com.ecoone.mindfulmealplanner.pledge.PeoplePledging;

import org.junit.Test;

import static org.junit.Assert.*;

public class PeoplePledgingTest {

    String name = "jacky";
    int pledge = 123;
    int iconID = 2;
    String location = "Vancouver";

    PeoplePledging test = new PeoplePledging(name,pledge,iconID,location);

    @Test
    public void getNameUser() {
        assertEquals(test.getNameUser(), "jacky");
    }

    @Test
    public void getPledged() {
        assertEquals(test.getPledged(), pledge, 0.001);
    }

    @Test
    public void getIconID() {
        assertEquals(test.getIconID(), 2);
    }

    @Test
    public void getMunicipality() {
        assertEquals(test.getMunicipality(), "Vancouver");
    }
}