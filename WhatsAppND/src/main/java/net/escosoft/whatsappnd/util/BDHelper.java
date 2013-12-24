package net.escosoft.whatsappnd.util;

import android.database.sqlite.SQLiteDatabase;

public class BDHelper {

    private static String DATABASE_NAME = "/storage/emulated/0/msgstore.db";

    public static SQLiteDatabase openBDHelper() {
        return SQLiteDatabase.openDatabase(DATABASE_NAME, null, 0);
    }

    public static void setDatabaseName(String databaseName) {
        DATABASE_NAME = databaseName;
    }

}
