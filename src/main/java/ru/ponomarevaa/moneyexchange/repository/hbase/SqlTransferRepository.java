package ru.ponomarevaa.moneyexchange.repository.hbase;

import com.google.inject.Inject;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;
import ru.ponomarevaa.moneyexchange.repository.TransferRepository;

import java.sql.*;
import java.util.*;

import static ru.ponomarevaa.moneyexchange.util.TransferValidationUtil.checkIdTheSame;

public class SqlTransferRepository implements TransferRepository {

    public final String INSERT_TRANSFER = "INSERT INTO transfers (sourceId, destinationId, amount) VALUES (?,?,?)";
    public final String GET_TRANSFER = "SELECT * FROM transfers WHERE id=?";
    public final String LIST_TRANSFERS = "SELECT * FROM transfers";
    public final String CLEAR_TRANSFERS = "DELETE FROM transfers";

    public final SqlHelper sqlHelper;

    @Inject
    public SqlTransferRepository() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
        this.sqlHelper = new SqlHelper(() -> DriverManager.getConnection(
                "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1;INIT=RUNSCRIPT FROM 'classpath:init.sql'",
                "sa", ""));
    }

    @Override
    public Transfer save(Transfer transfer, Account source, Account destination) {
        Objects.requireNonNull(transfer, "Entry must not be null");
        if (!transfer.isNew()) {
            throw new IllegalArgumentException("");
        }
        checkIdTheSame(transfer, source, destination);
        return sqlHelper.transactionalExecute( (conn) -> {
            try (PreparedStatement ps = conn.prepareStatement(INSERT_TRANSFER, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, transfer.getSourceId());
                ps.setInt(2, transfer.getDestinationId());
                ps.setBigDecimal(3, transfer.getAmount());
                ps.executeUpdate();
                ResultSet psGeneratedKeys = ps.getGeneratedKeys();
                if (psGeneratedKeys.next()) {
                    int id = psGeneratedKeys.getInt("id");
                    transfer.setId(id);
                }
                source.widrawal(transfer.getAmount());
                destination.add(transfer.getAmount());
                updateAccount(conn, source);
                updateAccount(conn, destination);
            }
            return transfer;
        });
    }

    private void updateAccount(Connection conn, Account source) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(SqlAccountRepository.UPDATE_ACCOUNT)) {
            ps.setBigDecimal(1, source.getAmount());
            ps.setInt(2, source.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public Transfer get(int id) {
        return sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(GET_TRANSFER)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                return extractResult(rs);
            }
        });
    }

    @Override
    public List<Transfer> getAll() {
        return sqlHelper.transactionalExecute(conn -> {
            List<Transfer> transfers = new ArrayList<>();

            try (PreparedStatement ps = conn.prepareStatement(LIST_TRANSFERS)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                   transfers.add(extractResult(rs));
                }
            }
            return transfers;
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute(CLEAR_TRANSFERS, (ps) -> {
            ps.execute();
            return null;
        });
    }

    private Transfer extractResult(ResultSet rs) throws SQLException {
        return new Transfer(
                rs.getInt("id"),
                rs.getInt("sourceId"),
                rs.getInt("destinationId"),
                rs.getBigDecimal("amount"));
    }


}
