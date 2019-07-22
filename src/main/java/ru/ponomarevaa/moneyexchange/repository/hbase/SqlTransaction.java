package ru.ponomarevaa.moneyexchange.repository.hbase;

import java.sql.Connection;
import java.sql.SQLException;

public interface SqlTransaction<T> {
    T execute(Connection conn) throws SQLException;
}