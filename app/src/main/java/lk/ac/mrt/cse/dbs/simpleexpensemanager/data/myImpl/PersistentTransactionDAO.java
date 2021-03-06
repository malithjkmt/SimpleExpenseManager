/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myImpl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO  implements TransactionDAO, Serializable {

    DbHelper dbHelper;

    public PersistentTransactionDAO( DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        try {
            //  get reference to writable DB
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // create ContentValues to add key "column"/value
            ContentValues contentValues = new ContentValues();

            contentValues.put(Constants.AC_TRANSACTION_COLUMN_ACCOUNT_NO, accountNo);
            contentValues.put(Constants.AC_TRANSACTION_COLUMN_DATE, String.valueOf(date));
            contentValues.put(Constants.AC_TRANSACTION_COLUMN_EXPENSE_TYPE, String.valueOf(expenseType) );
            contentValues.put(Constants.AC_TRANSACTION_COLUMN_AMOUNT, amount );

            // insert
            db.insert(Constants.TABLE_AC_TRANSACTION, // table
                    null, //nullColumnHack
                    contentValues); // key/value -> keys = column names/ values = column values
            //  close
            db.close();
        }
        catch (SQLiteException ex){
            System.out.println("error in logTransaction() method in PersistentTransactionDAO"+ ex.toString());
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> array_list = new ArrayList<>();

        //  build the query
        String query = "select * from " + Constants.TABLE_AC_TRANSACTION;

        try {
            //  get reference to readable DB
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor  = db.rawQuery(query, null);

            //  go over each row, get the transaction and add it to list
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                array_list.add(convertResultSetToTransaction(cursor));
                cursor.moveToNext();
            }

            // for logging
            Log.d("getAllTransactionLogs()", array_list.toString());

            // close the Cursor
            cursor.close();

            // close db
            db.close();

            // return Accounts  list
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getAllTransactionLogs() method in PersistentTransactionDAO"+ ex.toString());
        }
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> array_list = new ArrayList<>();

        try {
            // get reference to readable DB
            SQLiteDatabase db = dbHelper.getReadableDatabase();

            // query
            Cursor cursor =
                    db.query(Constants.TABLE_AC_TRANSACTION, // a. table
                            Constants.AC_TRANSACTION_COLUMNS, // b. column names
                            null, // c. selections
                            null, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            String.valueOf(limit)); // h. limit


            // go over each row, get the transaction and add it to list
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                array_list.add(convertResultSetToTransaction(cursor));
                cursor.moveToNext();
            }

            // for logging
            Log.d("getPaginatedT..", array_list.toString());

            // close the Cursor
            cursor.close();

            // close db
            db.close();

            // return Accounts  list
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getPaginatedTransactionLogs() method in PersistentTransactionDAO"+ ex.toString());
        }
        return null;

    }



    private Transaction convertResultSetToTransaction (Cursor cursor){

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH); // important!

        try {
            return new Transaction(dateFormat.parse(cursor.getString(cursor.getColumnIndex(Constants.AC_TRANSACTION_COLUMN_DATE))), cursor.getString(cursor.getColumnIndex(Constants.AC_TRANSACTION_COLUMN_ACCOUNT_NO)), ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(Constants.AC_TRANSACTION_COLUMN_EXPENSE_TYPE))), cursor.getDouble(cursor.getColumnIndex(Constants.AC_TRANSACTION_COLUMN_AMOUNT)));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
