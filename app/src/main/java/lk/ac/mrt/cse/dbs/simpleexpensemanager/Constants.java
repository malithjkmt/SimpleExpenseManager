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

package lk.ac.mrt.cse.dbs.simpleexpensemanager;

/**
 *
 */
public class Constants {
    public static final String EXPENSE_MANAGER = "expense-manager";

    /*** for DbHelper Class ***/
    // Database Version
    public static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "130597L";

    public static final String ACCOUNT_CREATE =
            "CREATE TABLE account (\n" +
                    "\n" +
                    "  accountNo TEXT NOT NULL,\n" +
                    "  bankName TEXT NOT NULL,\n" +
                    "  accountHolderName TEXT NOT NULL,\n" +
                    "  balance REAL NOT NULL CHECK (balance >= 0),\n" +
                    "  \n" +
                    "  PRIMARY KEY (accountNo)\n" +
                    ");";
    public static final String TRANSACTION_CREATE =
            "CREATE TABLE ac_transaction (\n" +
                    "\n" +
                    "  transactionId INTEGER,\n" +
                    "  accountNo TEXT NOT NULL,\n" +
                    "  date TEXT NOT NULL,\n" +
                    "  expenseType TEXT NOT NULL CHECK (expenseType =='EXPENSE' OR expenseType =='INCOME'),\n" +
                    "  amount REAL NULL CHECK (amount > 0),\n" +
                    "  \n" +
                    "  PRIMARY KEY (transactionId),\n" +
                    "  \n" +
                    "  CONSTRAINT fk_ac_transaction_account1 FOREIGN KEY (accountNo) REFERENCES account (accountNo) ON DELETE NO ACTION ON UPDATE NO ACTION\n" +
                    ");";

    /*** for PersistentTransactionDAO Class ***/
    // transaction table name
    public static final String TABLE_AC_TRANSACTION = "ac_transaction";
    // transaction Table Columns names
    public static final String AC_TRANSACTION_COLUMN_ACCOUNT_NO = "accountNo";
    public static final String AC_TRANSACTION_COLUMN_DATE = "date";
    public static final String AC_TRANSACTION_COLUMN_EXPENSE_TYPE = "expenseType";
    public static final String AC_TRANSACTION_COLUMN_AMOUNT = "amount";
    public static final String[] AC_TRANSACTION_COLUMNS = {AC_TRANSACTION_COLUMN_ACCOUNT_NO,AC_TRANSACTION_COLUMN_DATE,AC_TRANSACTION_COLUMN_EXPENSE_TYPE,AC_TRANSACTION_COLUMN_AMOUNT};

    /*** for PersistentAccountDAO Class ***/
    // account table name
    public static final String TABLE_ACCOUNT = "account";
    // account Table Columns names
    public static final String ACCOUNT_COLUMN_ACCOUNT_NO = "accountNo";
    public static final String ACCOUNT_COLUMN_BANK_NAME = "bankName";
    public static final String ACCOUNT_COLUMN_ACCOUNT_HOLDER_NAME = "accountHolderName";
    public static final String ACCOUNT_COLUMN_BALANCE = "balance";
    public static final String[] ACCOUNT_COLUMNS = {ACCOUNT_COLUMN_ACCOUNT_NO,ACCOUNT_COLUMN_BANK_NAME,ACCOUNT_COLUMN_ACCOUNT_HOLDER_NAME,ACCOUNT_COLUMN_BALANCE};

}
