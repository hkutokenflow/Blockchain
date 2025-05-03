package com.example.workshop1.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class Mysqliteopenhelper extends SQLiteOpenHelper {

    private static final String DBNAME="Mydb";

    public Mysqliteopenhelper(@Nullable Context context) {
        super(context, DBNAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsers = "CREATE TABLE Users (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username varchar(255), password varchar(255)," +
                "name varchar(255), type varchar(16), balance INTEGER)";
        db.execSQL(createUsers);

        String createTransactions = "CREATE TABLE Transactions (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "datetime TEXT, source INTEGER, destination INTEGER, amount INTEGER, " +
                "eid INTEGER REFERENCES Events(_id))";
        db.execSQL(createTransactions);

        String createRewards = "CREATE TABLE Rewards (_id INTEGER PRIMARY KEY, " +
                "name varchar(255), description varchar(1000), value INTEGER," +
                "uid INTEGER REFERENCES Users(_id))";
        db.execSQL(createRewards);

        String createEvents = "CREATE TABLE Events (_id INTEGER PRIMARY KEY, " +
                "description varchar(2000), reward INTEGER)";
        db.execSQL(createEvents);

        String createStudentRewards = "CREATE TABLE StudentRewards (_id INTEGER PRIMARY KEY, " +
                "uid INTEGER REFERENCES Users(_id)," +
                "rid INTEGER REFERENCES Rewards(_id))";
        db.execSQL(createStudentRewards);

        // Create admin account
        String addAdmin = "INSERT INTO Users VALUES(1, 'admin', 'admin123','HKU TokenFlow Admin', 'admin', 0)";
        db.execSQL(addAdmin);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing tables
        db.execSQL("DROP TABLE IF EXISTS Users;");
        db.execSQL("DROP TABLE IF EXISTS Transactions;");
        db.execSQL("DROP TABLE IF EXISTS Rewards;");
        db.execSQL("DROP TABLE IF EXISTS Events;");
        db.execSQL("DROP TABLE IF EXISTS StudentRewards;");

        // Call onCreate to recreate the tables
        onCreate(db);
    }

    // Add user (student from register/vendor from admin)
    public long addUser(User user){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("username", user.getUsername());
        contentValues.put("password", user.getPassword());
        contentValues.put("name", user.getName());
        contentValues.put("type", user.getType());
        contentValues.put("balance", user.getBalance());

        return db.insert("Users",null, contentValues);
    }

    // login
    public User login(String name, String password){
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor users = db1.query("Users",null,"username like?", new String[]{name},null,null,null);
        if (users!=null && users.moveToNext()) {
            String dbpwd = users.getString(2);
            if (password.equals(dbpwd)) {
                String username = users.getString(1);
                String uname = users.getString(3);
                String type = users.getString(4);
                int balance = users.getInt(5);

                return new User(username, dbpwd, uname, type, balance);
            }
        }
        return null;  // unsuccessful login
    }


    // ------------------ USER ------------------
    // get user id from username and password
    public int getUserId(String username, String pwd) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor id =  db1.query("Users", new String[]{"_id"}, "username = ? AND password = ?", new String[] {username, pwd}, null, null, null);
        if (id != null) {
            id.moveToFirst();
            return id.getInt(0);
        } else {
            return -999;
        }
    }

    // get all user transactions
    public Cursor getUserTrans(int uid) {
        SQLiteDatabase db1 = getWritableDatabase();
        return db1.query("Transactions", null, "source = ? OR destination = ?", new String[] {String.valueOf(uid), String.valueOf(uid)}, null, null, "datetime DESC");
    }


    // ------------------ VENDOR ------------------
    // get vendor list
    public Cursor getVendors() {
        SQLiteDatabase db1 = getWritableDatabase();
        return db1.query("Users", null, "type like?", new String[]{"vendor"}, null, null, null);
    }

    // edit vendor name
    public void editVendorName(String name, String username) {
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        db1.update("Users", cv, "username = ?", new String[] {username});
    }

    // edit vendor password
    public void editVendorPwd(String pwd, String username) {
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("password", pwd);
        db1.update("Users", cv, "username = ?", new String[] {username});
    }

    // delete vendor
    public void deleteVendor(String username) {
        SQLiteDatabase db1 = getWritableDatabase();
        db1.delete("Users", "username = ? AND type LIKE 'vendor'", new String[] {username});
    }

    // ------------------ EVENTS ------------------
    // Add event
    public long addEvent(Event event){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("description", event.getDescription());
        contentValues.put("reward", event.getReward());
        return db.insert("Events",null, contentValues);
    }

    // get events list
    public Cursor getEvents() {
        SQLiteDatabase db1 = getWritableDatabase();
        return db1.query("Events", null, null, null, null, null, null);
    }

    // edit event name
    public void editEvent(String newName, int newToken, String orgName, int orgToken) {
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("description", newName);
        cv.put("reward", newToken);
        db1.update("Events", cv, "description = ? AND reward = ?", new String[] {orgName, String.valueOf(orgToken)});
    }

    // get event id from description and reward
    public int getEventId(String orgName, int orgToken) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor id =  db1.query("Events", new String[]{"_id"}, "description = ? AND reward = ?", new String[] {orgName, String.valueOf(orgToken)}, null, null, null);
        if (id != null) {
            id.moveToFirst();
            return id.getInt(0);
        } else {
            return -999;
        }
    }

    // get event reward from eid
    public int getEventReward(int eid) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor reward =  db1.query("Events", new String[]{"reward"}, "_id = ?", new String[] {String.valueOf(eid)}, null, null, null);
        if (reward != null) {
            reward.moveToFirst();
            return reward.getInt(0);
        } else {
            return -999;
        }
    }

    // get event name from eid
    public String getEventName(int eid) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor desc =  db1.query("Events", new String[]{"description"}, "_id = ?", new String[] {String.valueOf(eid)}, null, null, null);
        if (desc != null) {
            desc.moveToFirst();
            return desc.getString(0);
        } else {
            return "";
        }
    }

    // delete event
    public void deleteEvent(String orgName, int orgToken) {
        SQLiteDatabase db1 = getWritableDatabase();
        db1.delete("Events", "description = ? AND reward = ?", new String[] {orgName, String.valueOf(orgToken)});
    }


    // ------------------ REWARDS ------------------
    // get full rewards list
    public Cursor getRewardsAll() {
        SQLiteDatabase db1 = getWritableDatabase();
        return db1.query("Rewards", null, null, null, null, null, null);
    }


    // get rewards list by vendor
    public Cursor getRewardsVendor(int uid) {
        SQLiteDatabase db1 = getWritableDatabase();
        return db1.query("Rewards", null, "uid = ?", new String[]{String.valueOf(uid)}, null, null, null);
    }

    // Add event
    public long addReward(Reward reward){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", reward.getName());
        contentValues.put("description", reward.getDescription());
        contentValues.put("value", reward.getValue());
        contentValues.put("uid", reward.getUid());
        return db.insert("Rewards",null, contentValues);
    }

    // edit reward name
    public void editReward(String newName, String newDesc, int newToken, String orgName, String orgDesc) {
        SQLiteDatabase db1 = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", newName);
        cv.put("description", newDesc);
        cv.put("value", newToken);
        db1.update("Rewards", cv, "name = ? AND description = ?", new String[] {orgName, orgDesc});
    }


    // delete reward voucher
    public void deleteReward(String orgName, String orgDesc) {
        SQLiteDatabase db1 = getWritableDatabase();
        db1.delete("Rewards", "name = ? AND description = ?", new String[] {orgName, orgDesc});
    }


    // get reward id from description and reward
    public int getRewardId(String name, String desc, int value, int uid) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor id =  db1.query("Rewards", new String[]{"_id"}, "name = ? AND description = ? AND value = ? AND uid = ?",
                new String[] {name, desc, String.valueOf(value), String.valueOf(uid)}, null, null, null);
        if (id != null) {
            id.moveToFirst();
            return id.getInt(0);
        } else {
            return -999;
        }
    }



    // ------------------ TRANSACTIONS ------------------
    // check if check-in id is valid (id exists in Events table)
    public boolean checkValidEvent(int checkInId) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor eids = db1.query("Events", new String[]{"_id"}, null, null, null, null, null);
        if (eids != null) {
            while (eids.moveToNext()) {
                int eid = eids.getInt(0);
                if (checkInId == eid) {
                    return true;
                }
            }
        }
        return false;
    }

    // check if check-in is repeated (if source,dest already exists in Transactions)
    public boolean checkRepeatedCheckIn(int checkInId, int srcId) {
        SQLiteDatabase db1 = getWritableDatabase();
        Cursor res = db1.query("Transactions", null, "eid = ? AND destination = ?", new String[] {String.valueOf(checkInId), String.valueOf(srcId)}, null, null, null);
        Log.d("SQL",  "result length: " + res.getCount());
        return res.getCount() == 0;
    }

    // Add transaction
    public long addTransaction(Transaction trans){
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db1 = this.getReadableDatabase();

        // update Users balances
        String sourceUid = String.valueOf(trans.getSource());
        Cursor cursor = db1.rawQuery("SELECT balance FROM Users WHERE _id = ?", new String[]{sourceUid});
        cursor.moveToFirst();
        int sourceBalance = cursor.getInt(0);
        int newSourceBalance = sourceBalance - trans.getAmount();
        String updateSource = "UPDATE Users SET balance = " + newSourceBalance + " WHERE _id = " + sourceUid;
        db.execSQL(updateSource);
        Log.d("SQL", "source balance updated");

        String destUid = String.valueOf(trans.getDestination());
        Cursor cursor1 = db1.rawQuery("SELECT balance FROM Users WHERE _id = ?", new String[]{destUid});
        cursor1.moveToFirst();
        int destBalance = cursor1.getInt(0);
        int newDestBalance = destBalance + trans.getAmount();
        String updateDest = "UPDATE Users SET balance = " + newDestBalance + " WHERE _id = " + destUid;
        db.execSQL(updateDest);
        Log.d("SQL", "dest balance updated");

        // Add row to Transactions
        ContentValues contentValues = new ContentValues();
        contentValues.put("datetime", trans.getDatetime());
        contentValues.put("source", trans.getSource());
        contentValues.put("destination", trans.getDestination());
        contentValues.put("amount", trans.getAmount());
        contentValues.put("eid", trans.getEid());
        return db.insert("Transactions",null, contentValues);
    }


    // ------------------ STUDENTREWARDS ------------------
    // Add student-reward record (redeem reward)
    public void addStudentReward(StudentReward sr){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("uid", sr.getUid());
        contentValues.put("rid", sr.getRid());

        db.insert("StudentRewards",null, contentValues);
    }


    // ------------------ Delete all transactions + reset balance for testing ----------------
    public void reset() {
        SQLiteDatabase db = this.getWritableDatabase();
        String deleteAllTransactions = "DELETE FROM Transactions";
        db.execSQL(deleteAllTransactions);
        String resetBalances = "UPDATE Users SET balance =" + 0;
        db.execSQL(resetBalances);
    }

}
