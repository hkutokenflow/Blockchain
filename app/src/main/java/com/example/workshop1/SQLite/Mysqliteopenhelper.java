package com.example.workshop1.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Mysqliteopenhelper extends SQLiteOpenHelper {

    private static final String DBNAME="Mydb";
    private static final String create_users
    ="create table users(name varchar(32),password varchar(32))";

    public Mysqliteopenhelper(@Nullable Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_users);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //注册方法
    public long register(User user){
        SQLiteDatabase db=getWritableDatabase();
        //类似键值对
        ContentValues contentValues=new ContentValues();
        contentValues.put("name",user.getName());
        contentValues.put("password",user.getPassword());
        //增
        long users=db.insert("users",null,contentValues);
        return users;
    }

    //登陆--根据账户名匹配密码是否正确
    public boolean login(String name,String password){
        SQLiteDatabase db1=getWritableDatabase();
        boolean result=false;
        //查 select * from users where ...
        Cursor users=db1.query("users",null,"name like?",new String[]{name},null,null,null);
        if(users!=null){
            while(users.moveToNext()){//不断找下一个
                //对比账号密码是否正确
                String dbpwd=users.getString(1);
                result=password.equals(dbpwd);
                return result;
            }
        }
        return false;
    }
}
