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
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.InputDevice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * This is an Persistent implementation of the AccountDAO interface. Created by Malith @MSquad
 */
public class PersistentAccountDAO  implements AccountDAO{

    // account table name
    private static final String TABLE_NAME = "account";

    // account Table Columns names
    private static final String COLUMN_ACCOUNT_NO = "accountNo";
    private static final String COLUMN_BANK_NAME = "bankName";
    private static final String COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    private static final String COLUMN_BALANCE = "balance";
    private static final String[] COLUMNS = {COLUMN_ACCOUNT_NO,COLUMN_BANK_NAME,COLUMN_ACCOUNT_HOLDER_NAME,COLUMN_BALANCE};

    DbHelper dbHelper;

    public PersistentAccountDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> array_list = new ArrayList<>();

        // 1. build the query
        String query = "select * from " + TABLE_NAME;

        try {
            // 2. get reference to readable DB
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor= db.rawQuery(query, null);

            // 3. go over each row, get the account number and add it to list
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                array_list.add(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO)));
                cursor.moveToNext();
            }
            //for logging
            Log.d("getAccountNumbersList()", array_list.toString());

            // close the Cursor
            cursor.close();
            // close db
            db.close();
            // return Account numbers list
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getAccountNumbersList() method in PersistentAccountDAO" + ex.toString());
        }
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> array_list = new ArrayList<>();
        // 1. build the query
        String query = "select * from " + TABLE_NAME;

    try {
        // 2. get reference to readable DB
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor= db.rawQuery(query, null);

        // 3. go over each row, get the account number and add it to list
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            array_list.add(convertResultSetToAccount(cursor));
        }
        //for logging
        Log.d("getAccountsList()", array_list.toString());

        // close the Cursor
        cursor.close();

        // close db
        db.close();

        // return Accounts  list
        return array_list;
    }
    catch (SQLiteException ex){
        System.out.println("error in getAccountList() method in PersistentAccountDAO "+ ex.toString());
    }
        return null;
    }



    @Override
    public Account getAccount(String accountNo){

        try {
            // 1. get reference to readable DB
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            // 2. query
            Cursor cursor =
                    db.query(TABLE_NAME, // a. table
                            COLUMNS, // b. column names
                            COLUMN_ACCOUNT_NO +" = ?", // c. selections
                            new String[]{String.valueOf(accountNo)}, // d. selections args
                            null, // e. group by
                            null, // f. having
                            null, // g. order by
                            null); // h. limit

            if (cursor != null)
                cursor.moveToFirst();

            // build account object
            Account account = convertResultSetToAccount(cursor);

            // log
            Log.d("getAccount(" + accountNo + ")", account.toString());

            // close the Cursor
            cursor.close();

            // close db
            db.close();

            // return account
           return account;


        }
        catch (SQLiteException e) {
            System.out.println("error in getAccount() method in PersistentAccountDAO "+ e.toString());
        }

        return null;
    }

    @Override
    public void addAccount(Account account) {
        //for logging
        Log.d("addAccount ", account.toString());

       try {
           // 1. get reference to writable DB
           SQLiteDatabase db = dbHelper.getWritableDatabase();

           // 2. create ContentValues to add key "column"/value
           ContentValues contentValues = new ContentValues();

           contentValues.put(COLUMN_ACCOUNT_NO, account.getAccountNo());
           contentValues.put(COLUMN_BANK_NAME, account.getBankName());
           contentValues.put(COLUMN_ACCOUNT_HOLDER_NAME, account.getAccountHolderName());
           contentValues.put(COLUMN_BALANCE, account.getBalance());

           // 3. insert
           db.insert(TABLE_NAME, // table
                   null, //nullColumnHack
                   contentValues); // key/value -> keys = column names/ values = column values

           // 4. close
           db.close();
       }
       catch (SQLiteException ex){
           System.out.println("error in addAccount() method in PersistentAccountDAO" + ex.toString());
       }
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

      try {

          // 1. get reference to writable DB
          SQLiteDatabase db = dbHelper.getWritableDatabase();

          // 2. delete
          db.delete(TABLE_NAME, //table name
                  COLUMN_ACCOUNT_NO +" = ?",  // selections
                  new String[] { String.valueOf(accountNo)}); //selections args
          // 3. close
          db.close();

          //log
          Log.d("removeAccount", accountNo);
        }
        catch (SQLiteException e){
            System.out.println("error in removeAccount() method in PersistentAccountDAO"+ e.toString());
        }


    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        try {
            // get existing account from DB by the accountNO
            Account account = getAccount(accountNo);

            // update balance based on the transaction type
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }

            // get reference to writable DB
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // create ContentValues to add key "column"/value
            ContentValues contentValues = new ContentValues();

            contentValues.put("accountNo", account.getAccountNo());
            contentValues.put("bankName", account.getBankName());
            contentValues.put("accountHolderName", account.getAccountHolderName());
            contentValues.put("balance", account.getBalance());

            // updating row
            db.update(TABLE_NAME, //table
                    contentValues, // column/value
                    COLUMN_ACCOUNT_NO + " = ?", // selections
                    new String[]{String.valueOf(accountNo)}); //selection args
            // close
            db.close();

            //log
            Log.d("updateBalance", account.toString());

        }
        catch (SQLiteException ex){
            System.out.println("error in updateBalance() method in PersistentAccountDAO"+ ex.toString());
        }
    }

    private Account convertResultSetToAccount (Cursor cursor){
        return new Account(cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_NO)), cursor.getString(cursor.getColumnIndex(COLUMN_BANK_NAME)), cursor.getString(cursor.getColumnIndex(COLUMN_ACCOUNT_HOLDER_NAME)), cursor.getColumnIndex(COLUMN_BALANCE));

    }


}
