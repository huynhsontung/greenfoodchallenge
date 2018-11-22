package com.ecoone.mindfulmealplanner.pledge;

public class PeoplePledging {
    private String name;
    private int pledged;
    private int iconID;
    private String municipality;

    public PeoplePledging(String name, int pledged, int iconID, String municipality) {
        this.name = name;
        this.pledged = pledged;
        this.iconID = iconID;
        this.municipality = municipality;
    }

    public String getNameUser() {
        return name;
    }

    public int getPledged() {
        return pledged;
    }

    public int getIconID() {
        return iconID;
    }

    public String getMunicipality() {
        return municipality;
    }
}
