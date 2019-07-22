package ru.ponomarevaa.moneyexchange.repository.hbase;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;

}
