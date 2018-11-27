package com.womenproiot.www.link;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LinkDAO extends SQLiteOpenHelper {
    private static LinkDAO instance;
    private static SQLiteDatabase mdb;
    String sql;
    Cursor cursor;

    public static final String DB_NAME = "restaurant.db";
    private static final SQLiteDatabase.CursorFactory FACTORY = null;
    public static final int VERSION = 1;

    public static LinkDAO getInstance(Context context) {
        if(instance == null) {
            instance = new LinkDAO(context);
        }
        mdb = instance.getWritableDatabase();
        return instance;
    }

    //db를 한개만 열어서 쓰기 위해 생성자를 private로.
    //객체는 getInstance()로만 얻을 수 있음.
    private LinkDAO(Context context) {
        super(context, DB_NAME, FACTORY, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE menu" +
                        " (menu_seq TEXT PRIMARY KEY" +
                        ", menu_name TEXT" +
                        ", menu_cost INTEGER" +
                        //", menu_available TEXT, " +
                        //", menu_regdate TEXT, " +
                        //", menu_moddate" +
                        ")";
        db.execSQL(sql);

        sql = "CREATE TABLE tableseat" +
                " (tableseat_seq Text PRIMARY KEY" +
                ", tableseat_name TEXT" +
                ")";
        db.execSQL(sql);

        sql = "CREATE TABLE order_list" +
                " (ordered_seq TEXT PRIMARY KEY" +
                ", ordered_count INTEGER" +
                ", ordered_date TEXT" +
                ", tableseat_seq TEXT NOT NULL" +
                ", menu_seq TEXT NOT NULL" +
                ", paid_flag TEXT" +
                ")";
        db.execSQL(sql);

        //초기에 메뉴와 테이블 테이터 5개씩 자동 삽입
        autoInsert(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE menu");
        db.execSQL("DROP TABLE orders");
        db.execSQL("DROP TABLE tableseat");
        onCreate(db);
    }

    //테스트 하기위한 fake data
    private void autoInsert(@NonNull SQLiteDatabase db){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String seq;
        //0번 테이블은 Takeout
        db.execSQL("INSERT INTO TABLESEAT VALUES(" + format.format(new Date()) + ", 'T0')");
        for (int i = 1; i < 6; i++) {
            int cost = i*1000;
            seq = format.format(new Date());
            db.execSQL("INSERT INTO MENU VALUES(" + seq + ",'menu " + i + "',"+ cost + ")");
            db.execSQL("INSERT INTO TABLESEAT VALUES( " + seq + ", 'T" + i +"')");
        }
    }



/*    public ArrayList<Menu> selectMenuTable(){
        sql = "SELECT * FROM MENU";
        cursor = mdb.rawQuery(sql,null);

        ArrayList<Menu> arrayList = new ArrayList<>();
        Menu menu;
        String seq,name;int cost;

        while(cursor.moveToNext()){
            seq = cursor.getString(cursor.getColumnIndex("menu_seq"));
            name = cursor.getString(cursor.getColumnIndex("menu_name"));
            cost = cursor.getInt(cursor.getColumnIndex("menu_cost"));

            menu = new Menu(seq,name,cost);
            arrayList.add(menu);
        }

        return arrayList;
    }

    public ArrayList<String> selectTableseatTable(){
        sql = "SELECT * FROM TABLESEAT";
        cursor = mdb.rawQuery(sql,null);

        ArrayList<String> arrayList = new ArrayList<>();

        String seq,name;
        while(cursor.moveToNext()){
            seq = cursor.getString(cursor.getColumnIndex("tableseat_seq"));
            name = cursor.getString(cursor.getColumnIndex("tableseat_name"));
            Tableseat tableseat = new Tableseat(seq,name);
            arrayList.add(tableseat.toString());
        }

        return arrayList;
    }*/
}
