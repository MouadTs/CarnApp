package com.team4.caratan;

public class carSmallCard {

    private String car_id, car_make, car_model, car_type, car_year, car_mileage,
                car_transmission, car_location, car_price, car_mainPhoto, car_makeLogo;

    public carSmallCard() {
    }

    public carSmallCard(String id, String make, String model, String type, String year,
                        String mileage, String transmission, String location, String price,
                        String mainPhoto, String makeLogo) {

        this.car_id = id;
        this.car_make = make;
        this.car_model = model;
        this.car_type = type;
        this.car_year = year;
        this.car_mileage = mileage;
        this.car_transmission = transmission;
        this.car_location = location;
        this.car_price = price;
        this.car_mainPhoto = mainPhoto;
        this.car_makeLogo = makeLogo;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCar_make() {
        return car_make;
    }

    public void setCar_make(String car_make) {
        this.car_make = car_make;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getCar_year() {
        return car_year;
    }

    public void setCar_year(String car_year) {
        this.car_year = car_year;
    }

    public String getCar_mileage() {
        return car_mileage;
    }

    public void setCar_mileage(String car_mileage) {
        this.car_mileage = car_mileage;
    }

    public String getCar_transmission() {
        return car_transmission;
    }

    public void setCar_transmission(String car_transmission) {
        this.car_transmission = car_transmission;
    }

    public String getCar_location() {
        return car_location;
    }

    public void setCar_location(String car_location) {
        this.car_location = car_location;
    }

    public String getCar_price() {
        return car_price;
    }

    public void setCar_price(String car_price) {
        this.car_price = car_price;
    }

    public String getCar_mainPhoto() {
        return car_mainPhoto;
    }

    public void setCar_mainPhoto(String car_mainPhoto) {
        this.car_mainPhoto = car_mainPhoto;
    }

    public String getCar_makeLogo() {
        return car_makeLogo;
    }

    public void setCar_makeLogo(String car_makeLogo) {
        this.car_makeLogo = car_makeLogo;
    }
}
