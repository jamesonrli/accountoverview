package com.jamesonli.accountview.provider;

final class DbConstants {

    static final String BALANCE_TABLE = "balances";
    static final String BALANCE_TABLE_PK = "_ID";
    static final String BALANCE_TABLE_DATE = "Date";
    static final String BALANCE_TABLE_BAL = "Balance";

    static final String SQL_CREATE_BALANCE =
            String.format(
                    "CREATE TABLE %s " +
                            "(%s INT PRIMARY KEY," +
                            "%s DATE NOT NULL," +
                            "%s REAL NOT NULL" +
                            ")",
                    BALANCE_TABLE,
                    BALANCE_TABLE_PK,
                    BALANCE_TABLE_DATE,
                    BALANCE_TABLE_BAL
            );

    static final String SQL_DROP_BALANCE = "DROP TABLE " + BALANCE_TABLE;
    static final String SQL_DROP_BALANCE_OLD = "DROP TABLE balance";
}
