package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Malith on 12/4/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "ExpenseManager";

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {

            // SQL statement to create account table
            db.execSQL(
                    "create table account " +
                            "(accountNo text primary key, bankName text,accountHolderName text,balance real);"
            );

            //for logging
            Log.d("create table ", "account");

            // SQL statement to create transaction table
            db.execSQL(
                    "create table transaction " +
                            "(accountNo text primary key, date text, expenseType text,amount real);"
            );

            //for logging
            Log.d("create table ", "transaction");
        }
        catch (SQLiteException ex){
            System.out.println("error in onCreate() method in DbHelper"+ ex.toString());
        }

        }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transaction");

        // create fresh tables
        onCreate(db);
    }

}
