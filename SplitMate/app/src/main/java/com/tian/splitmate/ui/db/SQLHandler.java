package com.tian.splitmate.ui.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tian.splitmate.ui.entities.Bill;
import com.tian.splitmate.ui.entities.User;

import java.util.ArrayList;
import java.util.List;

public class SQLHandler extends SQLiteOpenHelper {
    private static final int DB_VERSION = 451697920;
    private static final String DB_NAME = "TIAN";
    private static final String User_Table = "User";
    private static final String COL_ID = "ID";
    private static final String COL_U_Name = "User_NAME";
    private static final String COL_NOB = "PHONE_NOB";

    private static final String Bill_Table = "Bill";
    private static final String COL_B_ID = "Bill_ID";
    private static final String COL_B_Name = "Bill_NAME";
    private static final String COL_B_type = "BILL_TYPE";
    private static final String COL_B_Date = "BILL_DATE";
    private static final String COL_B_money = "BILL_MONEY";
    private static final String COL_B_content = "BILL_CONTENT";

    public SQLHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    //分别创建User和Bill表
    public void onCreate(SQLiteDatabase db) {
        String user_query = "CREATE TABLE " + User_Table + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_U_Name + " TEXT," +
                COL_NOB + " TEXT);";
        String bill_query = "CREATE TABLE " + Bill_Table + "(" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_B_ID + " INTEGER," +
                COL_B_Name + " TEXT," +
                COL_B_type + " TEXT," +
                COL_B_Date + " TEXT," +
                COL_B_content + " TEXT, " +
                COL_U_Name + " TEXT," +
                COL_B_money + " DOUBLE);";
        db.execSQL(user_query);
        db.execSQL(bill_query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + User_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Bill_Table);
        onCreate(db);
    }

    public void addBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_B_ID, bill.getBill_ID());
        values.put(COL_B_Name, bill.getBill_Name());
        values.put(COL_B_type, bill.getBill_Type());
        values.put(COL_B_Date, bill.getBill_Date());
        values.put(COL_B_content, bill.getBill_content());
        values.put(COL_U_Name, bill.getUser_Name());
        values.put(COL_B_money, bill.getBill_Money());
        db.insert(Bill_Table, null, values);
        db.close();
    }

    //判断传进来的值在数据库中是否存在，用来唯一识别Bill_ID
    public boolean getNewBillId(int temp) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + Bill_Table + " WHERE " + COL_B_ID + " = " + temp;
        Cursor cursor = db.rawQuery(query, null);
        return cursor.moveToFirst();
    }


    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_U_Name, user.getUserName());
        values.put(COL_NOB, user.getPhoneNumber());
        db.insert(User_Table, null, values);
        db.close();
    }

    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + User_Table;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setUser_ID(cursor.getInt(0));
                user.setUserName(cursor.getString(1));
                user.setPhoneNumber(cursor.getString(2));
                userList.add(user);
            } while (cursor.moveToNext());
        }
        return userList;
    }

    public List<Bill> getAllBill() {
        List<Bill> BillList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + Bill_Table;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Bill bill = new Bill();
                bill.setBill_ID(cursor.getInt(1));
                bill.setBill_Name(cursor.getString(2));
                bill.setBill_Type(cursor.getString(3));
                bill.setBill_Date(cursor.getString(4));
                bill.setBill_content(cursor.getString(5));
                bill.setUser_Name(cursor.getString(6));
                bill.setBill_Money(cursor.getDouble(7));
                System.out.println(bill);
                BillList.add(bill);
            } while (cursor.moveToNext());
        }
        return BillList;
    }

    //根据传进来的名字在Bill表中查询所有的消费情况，并按类型归档总计，此处用于饼图的制作
    public List<Bill> getBills(String username) {
        try {
            List<Bill> Bills = new ArrayList<>();
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT BILL_TYPE,SUM(BILL_MONEY) FROM Bill WHERE User_NAME=? GROUP BY BILL_TYPE", new String[]{username});
            if (cursor.moveToFirst()) {
                do {
                    Bill bill = new Bill();
                    bill.setBill_Type(cursor.getString(0));
                    bill.setBill_Money(cursor.getDouble(1));
                    System.out.println(bill);
                    Bills.add(bill);
                } while (cursor.moveToNext());
            }
            return Bills;
        } catch (Exception e) {
            System.out.println(e);
            return new ArrayList<>();
        }
    }
}
