package com.team4.caratan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mDBhandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "localcaratan.db";


    private static final String TABLE_NAME = "user";

    private static final String COLUMN_USER_ID = "userid";
    private static final String COLUMN_USER_FULLNAME ="userfullname";
    private static final String COLUMN_USER_EMAIL = "useremail";
    private static final String COLUMN_USER_PHONE = "userphone";
    private static final String COLUMN_USER_PROFILEPIC = "userprofilepic";
    private static final String COLUMN_USER_ADMIN = "useradmin";

    public mDBhandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" +
                COLUMN_USER_ID + " VARCHAR(50) PRIMARY KEY, " +
                COLUMN_USER_FULLNAME + " VARCHAR(30), " +
                COLUMN_USER_EMAIL + " VARCHAR(50), " +
                COLUMN_USER_PHONE + " VARCHAR(20), " +
                COLUMN_USER_PROFILEPIC + " VARCHAR(50), " +
                COLUMN_USER_ADMIN + " VARCHAR(2))";
        sqLiteDatabase.execSQL(CREATE_TABLE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    /*---- INSERT, SELECT, UPDATE, DELETE ----*/

    private SQLiteDatabase database;

    public void open() throws SQLException {
        database = this.getWritableDatabase();
    }

    private String[] allColumns = {COLUMN_USER_ID, COLUMN_USER_FULLNAME, COLUMN_USER_EMAIL,
            COLUMN_USER_PHONE, COLUMN_USER_PROFILEPIC, COLUMN_USER_ADMIN};

    private Users cursorToUsers(Cursor cursor) {
        Users users = new Users();

        users.setUser_id(cursor.getString(0));
        users.setUser_fullname(cursor.getString(1));
        users.setUser_email(cursor.getString(2));
        users.setUser_phone(cursor.getString(3));
        users.setUser_profilepic(cursor.getString(4));
        users.setUser_admin(cursor.getString(5));

        return users;
    }

    public void createUsers(String id, String fullname, String email, String phone, String profilepic, String admin) {
        database.execSQL("delete from "+ TABLE_NAME);

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, id);
        values.put(COLUMN_USER_FULLNAME, fullname);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PHONE, phone);
        values.put(COLUMN_USER_PROFILEPIC, profilepic);
        values.put(COLUMN_USER_ADMIN, admin);

        database.insert(TABLE_NAME, null, values);
    }

    public Users getUsers() {
        Users users = new Users();

        Cursor cursor = database.query(TABLE_NAME,allColumns,null,null,null,null,null);
        cursor.moveToFirst();
        users = cursorToUsers(cursor);
        cursor.close();

        return users;
    }

    public void updateUsers(Users users) {
        String filter = "userid="+users.getUser_id();
        ContentValues args = new ContentValues();
        args.put(COLUMN_USER_ID, users.getUser_id());
        args.put(COLUMN_USER_FULLNAME, users.getUser_fullname());
        args.put(COLUMN_USER_EMAIL, users.getUser_email());
        args.put(COLUMN_USER_PHONE, users.getUser_phone());
        args.put(COLUMN_USER_PROFILEPIC, users.getUser_profilepic());
        args.put(COLUMN_USER_ADMIN, users.getUser_admin());

        database.update(TABLE_NAME, args, filter, null);
    }

    public void deleteUsers() {
        database.delete(TABLE_NAME, null, null);
    }

    /*---- INSERT, SELECT, UPDATE, DELETE ----*/


}
