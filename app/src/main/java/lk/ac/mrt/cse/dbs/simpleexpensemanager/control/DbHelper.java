package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.Serializable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;

/**
 * Created by Malith @MSquad on 12/4/2015.
 */
public class DbHelper extends SQLiteOpenHelper implements  Serializable {

    public DbHelper(Context context) {

        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            // SQL statement to create account table
            db.execSQL(Constants.ACCOUNT_CREATE);

            //for logging
            Log.d("create table ", "account");

            // SQL statement to create transaction table
            db.execSQL(Constants.TRANSACTION_CREATE);

            //for logging
            Log.d("create table ", "ac_transaction");
        }
        catch (SQLiteException ex){
            System.out.println("error in onCreate() method in DbHelper"+ ex.toString());
        }
        }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS ac_transaction");

        // create fresh tables
        onCreate(db);
    }

}
