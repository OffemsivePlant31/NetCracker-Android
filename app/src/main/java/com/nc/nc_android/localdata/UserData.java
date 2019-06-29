package com.nc.nc_android.localdata;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.nc.nc_android.MyApplication;


public class UserData {

    private static DBHelper dbHelper;

    static {
        dbHelper = new DBHelper(MyApplication.getContext());
    }

    public static void saveUserId(long userid){
        saveLong("userId", userid);
    }

    public static long loadUserId(){
        return loadLong("userId");
    }

    public static void saveGameId(long gameid){
        saveLong("gameId", gameid);
    }

    public static long loadGameId(){
        return loadLong("gameId");
    }



    public static void saveBoolean(String name, boolean value){
        saveString(name, String.valueOf(value));
    }

    public static boolean loadBoolean(String name){
        String val = loadString(name);
        if(val == null){
            return false;
        }else{
            return val.toUpperCase().equals("true".toUpperCase());
        }

    }


    public static void saveLong(String name, long value){
        saveString(name, String.valueOf(value));
    }

    public static long loadLong(String name){
        String val = loadString(name);
        if(val == null){
            return -1;
        }else{
            return Long.valueOf(val);
        }
    }

    public static void saveString(String name, String value){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("delete from userdata where name = \""+name+"\"");
        //db.execSQL("insert into userdata(name, value) values (?, ?)", new String[]{name, value});
        db.execSQL("insert into userdata(name, value) values (\""+name+"\", \"" + value + "\")");

    }

    public static String loadString(String name){

        String val = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //Cursor cursor = db.rawQuery("select value from userdata where name = ?", new String[]{name});
        Cursor cursor = db.rawQuery("select value from userdata where name = \""+name+"\"", null);
        if(cursor.moveToFirst()){
            val =  cursor.getString(0);
        }

        cursor.close();
        return val;
    }

}
