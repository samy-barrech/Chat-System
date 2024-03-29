package com.langram.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;

public class Settings {

    private static final int DEFAULT_WIDTH = 1024;
    private static final int DEFAULT_HEIGHT = 568;
    private static final int LOGIN_WIDTH = 365;
    private static final int LOGIN_HEIGHT = 500;
    public static final String DB_DIRECTORY = "data";

    private HashMap<String, String> params = new HashMap<>();
    private Locale locale = new Locale("fr", "FR");
    private static final Settings instance = new Settings();

    // Get instance
    public static Settings getInstance() {
        return instance;
    }

    // Init locale and params
    private Settings() {
        Locale.setDefault(locale);
        System.setProperty("java.net.preferIPv4Stack" , "true");
        params.put("AppName", "Langram");
    }

    /**
     * @return String Application Name
     */
    public String getAppName() {
        return params.get("AppName");
    }

    /**
     @return Locale Application Locale
    */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return int Application Width
     */
    public int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    /**
     * @return int Application Height
     */
    public int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    /**
     * @return int Application Login Width
     */
    public int getLoginWidth() {
        return LOGIN_WIDTH;
    }

    /**
     * @return int Application Login Height
     */
    public int getLoginHeight() {
        return LOGIN_HEIGHT;
    }

    /**
     * @return String SQLite Database path
     */
    public String getDatabasePath() {
        String databaseName = Hash.get(User.getInstance().getUsername().toLowerCase()) + ".db";
        return this.getDatabaseDirectory() + File.separator + databaseName;
    }

    public String getDatabaseDirectory() {
        return System.getProperty("user.dir") + File.separator + DB_DIRECTORY;
    }

}
