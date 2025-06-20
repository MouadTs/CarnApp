package com.team4.caratan;

public class Constant {
    // Use 10.0.2.2 for localhost when running the backend on the same machine as the Android emulator
    private static final String ROOT_URL = "http://10.0.2.2:8080/";
    public static final String API_URL = ROOT_URL + "api/";
    public static final String IMAGE_URL = ROOT_URL;

    // --- Auth ---
    public static final String URL_LOGIN = API_URL + "auth/login";
    public static final String URL_REGISTER = API_URL + "auth/register";
    public static final String URL_UPDATEUSER = API_URL + "auth/update";

    // --- Cars ---
    public static final String URL_CARS = API_URL + "cars";
    public static final String URL_CAR_MAKES = API_URL + "cars/makes";
    public static final String URL_CARS_BY_MAKE = API_URL + "cars/make/"; // Append make name
    public static final String URL_GETCAR_LIST = API_URL + "cars";

    // --- Data Retrieval Endpoints ---
    public static final String URL_GETCARINFO = API_URL + "cars/"; // Append car id
    public static final String URL_UPDATEVIEWS = API_URL + "cars/"; // Append car id + /views

    // Car management endpoints
    public static final String URL_GETCARPHOTOS = API_URL + "cars/";
    public static final String URL_GETCARLOGOSITE = API_URL + "cars/logo/";
    public static final String URL_ADDCAR = API_URL + "cars";

    // Car makes/models endpoints
    public static final String URL_GETMAKE_LIST = API_URL + "makes";
    public static final String URL_GET_MAKE = API_URL + "cars/makes";
    public static final String URL_GETMODEL_LIST_BYMAKE = API_URL + "makes/";
    public static final String URL_GETTYPE_LIST_BYMODEL = API_URL + "models/";
    public static final String URL_GETCOLOR_LIST = API_URL + "colors";

    // New constants
    public static final String URL = "http://192.168.100.17/caratan/";
    public static final String HOME = URL + "api";
}