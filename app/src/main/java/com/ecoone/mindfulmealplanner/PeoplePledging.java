package com.ecoone.mindfulmealplanner;

public class PeoplePledging {
    private String name;
    private float pledged;
    private int iconID;
    private String municipality;

    public PeoplePledging(String name, float pledged, int iconID, String municipality) {
        this.name = name;
        this.pledged = pledged;
        this.iconID = iconID;
        this.municipality = municipality;
    }

    public String getNameUser() {
        return name;
    }

    public float getPledged() {
        return pledged;
    }

    public int getIconID() {
        return iconID;
    }

    public String getMunicipality() {
        return municipality;
    }
}
