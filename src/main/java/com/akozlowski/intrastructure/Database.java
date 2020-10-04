package com.akozlowski.intrastructure;

public class Database {
    private static final Database db = new Database();

    private Database() {
    }

    public static Database getInstance() {
        return db;
    }
}
