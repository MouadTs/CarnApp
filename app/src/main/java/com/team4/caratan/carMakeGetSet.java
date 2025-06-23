package com.team4.caratan;

public class carMakeGetSet {

    private String make_logo, make_name, counts;

    public carMakeGetSet() {
    }

    public carMakeGetSet(String make_logo, String make_name, String counts) {
        this.make_logo = make_logo;
        this.make_name = make_name;
        this.counts = counts;
    }

    public String getMake_logo() {
        return make_logo;
    }

    public String getMake_name() {
        return make_name;
    }

    public String getCounts() {
        return counts;
    }
}
