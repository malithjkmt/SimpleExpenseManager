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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * This is an In-Memory implementation of TransactionDAO interface. This is not a persistent storage. All the
 * transaction logs are stored in a LinkedList in memory.
 */
public class PersistentTransactionDAO extends SQLiteOpenHelper implements TransactionDAO {


    public static final String DATABASE_NAME = "ExpenseManager.db";
    public static final String ACCOUNT_TABLE_NAME = "transaction";
    public static final String ACCOUNT_COLUMN_ACCOUNT_NO = "accountNo";
    public static final String ACCOUNT_COLUMN_DATE = "date";
    public static final String ACCOUNT_COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String ACCOUNT_COLUMN_AMOUNT = "amount";

    public PersistentTransactionDAO(Context context) {
        super(context, DATABASE_NAME , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table transaction " +
                        "(accountNo text primary key, date numeric, expenseType text,amount real)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS transaction");
        onCreate(db);
    }


    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();

            contentValues.put("accountNo", accountNo);
            contentValues.put("date", date.toString());
            contentValues.put("expenseType", expenseType.toString() );
            contentValues.put("amount", amount );

            db.insert("account", null, contentValues);
        }
        catch (SQLiteException ex){
            System.out.println("error in logTransaction() method in PersistentTransactionDAO");
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> array_list = new ArrayList<>();
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from transaction", null);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                array_list.add(convertResultSetToTransaction(res));
            }
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getAllTransactionLogs() method in PersistentTransactionDAO");
        }
        return null;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> array_list = new ArrayList<>();
        try {

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res = db.rawQuery("select * from transaction ORDER BY accountNo LIMIT" + Integer.toString(limit), null);
            res.moveToFirst();

            while (!res.isAfterLast()) {
                array_list.add(convertResultSetToTransaction(res));
            }
            return array_list;
        }
        catch (SQLiteException ex){
            System.out.println("error in getPaginatedTransactionLogs() method in PersistentTransactionDAO");
        }
        return null;

    }



    private Transaction convertResultSetToTransaction (Cursor res){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            return new Transaction(dateFormat.parse(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_DATE))), res.getString(res.getColumnIndex(ACCOUNT_COLUMN_ACCOUNT_NO)), ExpenseType.valueOf(res.getString(res.getColumnIndex(ACCOUNT_COLUMN_EXPENSE_TYPE))), res.getDouble(res.getColumnIndex(ACCOUNT_COLUMN_AMOUNT)));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
