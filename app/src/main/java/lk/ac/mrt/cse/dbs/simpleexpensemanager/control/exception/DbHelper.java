package lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Malith on 12/4/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ExpenseManager.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(
                    "create table account " +
                            "(accountNo text primary key, bankName text,accountHolderName text,balance real);"
            );

            db.execSQL(
                    "create table transaction " +
                            "(accountNo text primary key, date text, expenseType text,amount real);"
            );
        }
        catch (SQLiteException ex){
            System.out.println("error in onCreate() method in DbHelper"+ ex.toString());
        }

        }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS account");
        db.execSQL("DROP TABLE IF EXISTS transaction");
        onCreate(db);
    }

}
