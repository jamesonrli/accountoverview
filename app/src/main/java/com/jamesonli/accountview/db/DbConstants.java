package com.jamesonli.accountview.db;

public class DbConstants {

    public static final String BALANCE_TABLE = "balance";
    public static final String BALANCE_TABLE_PK = "ID";
    public static final String BALANCE_TABLE_DATE = "Date";
    public static final String BALANCE_TABLE_BAL = "Balance";

    public static final String SQL_CREATE_BALANCE =
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

    public static final String SQL_DROP_BALANCE = "DROP TABLE " + BALANCE_TABLE;
}
