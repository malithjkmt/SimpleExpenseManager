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
 * This is an In-Memory implementation of the AccountDAO interface. This is not a persistent storage. A HashMap is
 * used to store the account details temporarily in the memory.
 */
public class PersistentAccountDAO  implements AccountDAO{

    public static final String ACCOUNT_COLUMN_ACCOUNT_NO = "accountNo";
    public static final String ACCOUNT_COLUMN_BANK_NAME = "bankName";
    public static final String ACCOUNT_COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";

    DbHelper dbHelper;

    public PersistentAccountDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {

        try {
            ArrayList<String> array_list = new ArrayList<String>();

            //hp = new HashMap();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor res = db.rawQuery("select * from account", null);
            res.moveToFirst();

            while (res.isAfterLast() == false) {
                array_list.add(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_ACCOUNT_NO)));
                res.moveToNext();
            }
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getAccountNumbersList() method in PersistentAccountDAO");
        }
        return null;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> array_list = new ArrayList<Account>();
    try {

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor res = db.rawQuery("select * from account", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(convertResultSetToAccount(res));
        }
        return array_list;
    }
    catch (SQLiteException ex){
        System.out.println("error in getAccountList() method in PersistentAccountDAO");
    }
        return null;
    }



    @Override
    public Account getAccount(String accountNo){

        try {

            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor res = db.rawQuery("select * from account where id=" + accountNo + "", null);
            res.moveToFirst();

            Account tempAccount = convertResultSetToAccount(res);

            return tempAccount;
        }
        catch (SQLiteException e) {
            System.out.println("error in getAccount() method in PersistentAccountDAO");
        }
        return null;
    }

    @Override
    public void addAccount(Account account) {
       try {
           SQLiteDatabase db = dbHelper.getWritableDatabase();
           ContentValues contentValues = new ContentValues();

           contentValues.put("accountNo", account.getAccountNo());
           contentValues.put("bankName", account.getBankName());
           contentValues.put("accountHolderName", account.getAccountHolderName());
           contentValues.put("balance", account.getBalance());

           db.insert("account", null, contentValues);
       }
       catch (SQLiteException ex){
           System.out.println("error in addAccount() method in PersistentAccountDAO");
       }

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {

      try {

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("account",
                    "accountNo = ? ",
                    new String[]{accountNo});
        }
        catch (SQLiteException e){
            System.out.println("error in removeAccount() method in PersistentAccountDAO");
        }


    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {

        try {
            Account account = getAccount(accountNo);

            // specific implementation based on the transaction type
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("accountNo", account.getAccountNo());
            contentValues.put("bankName", account.getBankName());
            contentValues.put("accountHolderName", account.getAccountHolderName());
            contentValues.put("balance", account.getBalance());

            db.update("account", contentValues, "accountNo = ? ", new String[]{accountNo});
        }
        catch (SQLiteException ex){
            System.out.println("error in updateBalance() method in PersistentAccountDAO");
        }
    }

    private Account convertResultSetToAccount (Cursor res){
        Account tempAccount = new Account(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_ACCOUNT_NO)), res.getString(res.getColumnIndex(ACCOUNT_COLUMN_BANK_NAME)), res.getString(res.getColumnIndex(ACCOUNT_COLUMN_ACCOUNT_HOLDER_NAME)), res.getColumnIndex(ACCOUNT_COLUMN_BALANCE));
        return tempAccount;
    }


}
