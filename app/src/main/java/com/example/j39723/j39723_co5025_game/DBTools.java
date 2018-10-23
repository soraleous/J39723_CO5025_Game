package com.example.j39723.j39723_co5025_game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBTools extends SQLiteOpenHelper {

    // Code adapted from https://stackoverflow.com/a/22209047
    private final static int DB_VERSION = 10;

    public DBTools(Context context){
        super(context, "myApp.db", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "create table logins (userId Integer primary key autoincrement, " +
                " username text, password text)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            System.out.println("UPGRADE DB oldVersion=" + oldVersion + " - new Version=" + newVersion);
            onCreate(db);
            if (oldVersion<10){
                String query = "create table logins (userId Integer primary key autoincrement," +
                        " username text, password text)";
                db.execSQL(query);
            }
        }
        catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // super.onDowngrade(db, oldVersion, newVersion);
        System.out.println("DOWNGRADE DB oldVersion="+oldVersion+" - newVersion="+newVersion);
    }

    public User insertUser (User queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", queryValues.username);
        values.put("password", queryValues.password);
        queryValues.userId=database.insert("logins", null, values);
        database.close();
        return queryValues;
    }

    public int updateUserPassword (User queryValues){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", queryValues.username);
        values.put("password", queryValues.password);
        queryValues.userId=database.insert("logins", null, values);
        database.close();
        return database.update("logins", values, "userId = ?", new String[] {String.valueOf(queryValues.userId)});
    }

    public User getUser (String username){
        String query = "Select userId, password from logins where username ='"+username+"'";
        User myUser = new User(0,username,"");
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                myUser.userId=cursor.getLong(0);
                myUser.password=cursor.getString(1);
            } while (cursor.moveToNext());
        }
        return myUser;
    }

    // End of adapted code
}
