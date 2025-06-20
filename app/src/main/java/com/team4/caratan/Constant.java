package com.team4.caratan;

public class Constant {

    // New Java 22 Backend URL (will be updated when backend is ready)
    public static final String ROOT_URL = "http://10.0.2.2:8080/api/";
    //public static final String ROOT_URL = "http://localhost:8080/api/";
    //public static final String ROOT_URL = "https://caratan.000webhostapp.com/project/functions/";

    // Authentication endpoints
    public static final String URL_REGISTER = ROOT_URL + "auth/register";
    public static final String URL_LOGIN = ROOT_URL + "auth/login";
    public static final String URL_UPDATEUSER = ROOT_URL + "users/profile";

    // Car management endpoints
    public static final String URL_GETCAR_LIST = ROOT_URL + "cars";
    public static final String URL_GETCAR_LIST_BYMAKE = ROOT_URL + "cars/make/";
    public static final String URL_GETCARPHOTOS = ROOT_URL + "cars/";
    public static final String URL_GETCARINFO = ROOT_URL + "cars/";
    public static final String URL_UPDATEVIEWS = ROOT_URL + "cars/";
    public static final String URL_GETCARLOGOSITE = ROOT_URL + "cars/logo/";

    // Car makes/models endpoints
    public static final String URL_GETMAKE_LIST = ROOT_URL + "makes";
    public static final String URL_GETMODEL_LIST_BYMAKE = ROOT_URL + "makes/";
    public static final String URL_GETTYPE_LIST_BYMODEL = ROOT_URL + "models/";
    public static final String URL_GETCOLOR_LIST = ROOT_URL + "colors";

    // Add car endpoint
    public static final String URL_ADDCAR = ROOT_URL + "cars";
}