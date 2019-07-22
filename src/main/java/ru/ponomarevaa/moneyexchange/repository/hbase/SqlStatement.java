package ru.ponomarevaa.moneyexchange.repository.hbase;

import java.sql.PreparedStatement;
import java.sql.SQLException;


@FunctionalInterface
public interface SqlStatement<T> {
    T execute(PreparedStatement ps) throws SQLException;
}
