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

package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myImpl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.myImpl.PersistentTransactionDAO;

/**
 *
 */
public class PersistentExpenseManager extends ExpenseManager {

    DbHelper dbHelper;
    public PersistentExpenseManager(Context context) {
        this.dbHelper = new DbHelper(context);
        setup();

    }

    @Override
    public void setup() {
        /*** Begin generating dummy data for In-Memory implementation ***/



        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(dbHelper);
        setTransactionsDAO(persistentTransactionDAO);


        AccountDAO persistentAccountDAO = new PersistentAccountDAO(dbHelper);
        setAccountsDAO(persistentAccountDAO);

        // dummy data for debugging
            Account dummyAcct1 = new Account("123425A", "Yoda Bank", "Anakin Skywalker", 10000.0);
            Account dummyAcct2 = new Account("789425Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();


            getAccountsDAO().addAccount(dummyAcct1);
            getAccountsDAO().addAccount(dummyAcct2);

            getTransactionsDAO().logTransaction(date, "123425A", ExpenseType.INCOME, 24.2);
            getTransactionsDAO().logTransaction(date, "789425Z", ExpenseType.INCOME, 254.2);

            // place a breakpoint here...
            try {
                 Account tempAC =  getAccountsDAO().getAccount("789425Z");
                 Transaction tempTR = getTransactionsDAO().getPaginatedTransactionLogs(1).get(0);

            } catch (InvalidAccountException e) {
                e.printStackTrace();
            }

        /*** End ***/
    }
}
