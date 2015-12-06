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
    public static final String DATABASE_NAME = "db130597L";

    private static final String ACCOUNT_CREATE =
            "CREATE TABLE account (\n" +
                    "\n" +
                    "  accountNo TEXT NOT NULL,\n" +
                    "  bankName TEXT NOT NULL,\n" +
                    "  accountHolderName TEXT NOT NULL,\n" +
                    "  balance REAL NOT NULL CHECK (balance >= 0),\n" +
                    "  \n" +
                    "  PRIMARY KEY (accountNo)\n" +
                    ");";
    private static final String TRANSACTION_CREATE =
            "CREATE TABLE transaction (\n" +
                    "\n" +
                    "  transactionId INTEGER AUTOINCREMENT NOT NULL,\n" +
                    "  accountNo TEXT NOT NULL,\n" +
                    "  date TEXT NOT NULL,\n" +
                    "  expenseType TEXT NOT NULL CHECK (expenseType =='EXPENSE' OR expenseType =='INCOME'),\n" +
                    "  amount REAL NULL CHECK (amount > 0),\n" +
                    "  \n" +
                    "  PRIMARY KEY (transactionId),\n" +
                    "  \n" +
                    "  CONSTRAINT fk_transaction_account1 FOREIGN KEY (accountNo) REFERENCES account (accountNo) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                    ");";

    public DbHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {

            // SQL statement to create account table
            db.execSQL(ACCOUNT_CREATE);

            //for logging
            Log.d("create table ", "account");

            // SQL statement to create transaction table
            db.execSQL(TRANSACTION_CREATE);

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
