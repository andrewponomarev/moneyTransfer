package ru.ponomarevaa.moneyexchange.repository.hbase;

import com.google.inject.Inject;
import ru.ponomarevaa.moneyexchange.exception.StorageException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlAccountRepository implements AccountRepository {

    public static final String INSERT_ACCOUNT = "INSERT INTO accounts (amount) VALUES (?)";
    public static final String UPDATE_ACCOUNT = "UPDATE accounts SET amount=? WHERE id=?";
    public static final String DELETE_ACCOUNT = "DELETE FROM accounts WHERE id=?";
    public static final String GET_ACCOUNT = "SELECT * FROM accounts WHERE id=?";
    public static final String LIST_ACCOUNTS = "SELECT * FROM accounts";
    public static final String CLEAR_ACCOUNTS = "DELETE FROM accounts";

    public final SqlHelper sqlHelper;

    @Inject
    public SqlAccountRepository() {
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
    public Account save(Account acct) {
        if (acct.isNew()) {
            return sqlHelper.transactionalExecute(  (conn) -> {
                        try (PreparedStatement ps = conn.prepareStatement(INSERT_ACCOUNT, Statement.RETURN_GENERATED_KEYS)) {
                            ps.setBigDecimal(1, acct.getAmount());
                            ps.executeUpdate();
                            ResultSet psGeneratedKeys = ps.getGeneratedKeys();
                            if (psGeneratedKeys.next()) {
                                int id = psGeneratedKeys.getInt("id");
                                acct.setId(id);
                            }
                        }
                        return acct;
                    });
        } else {
            return sqlHelper.transactionalExecute((conn) -> {
                try (PreparedStatement ps = conn.prepareStatement(UPDATE_ACCOUNT)) {
                    ps.setBigDecimal(1, acct.getAmount());
                    ps.setInt(2, acct.getId());
                    int id = ps.executeUpdate();
                    if (id == 0) {
                        return null;
                    }
                    return acct;
                }
            });
        }
    }

    @Override
    public boolean delete(int id) {
        return sqlHelper.transactionalExecute( conn -> {
            try (PreparedStatement ps = conn.prepareStatement(DELETE_ACCOUNT)) {
                ps.setInt(1, id);
                int rs = ps.executeUpdate();
                if (rs == 0) {
                    return false;
                }
                return true;
            }
        });
    }

    @Override
    public Account get(int id) {
        return sqlHelper.transactionalExecute(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(GET_ACCOUNT)) {
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (!rs.next()) {
                    return null;
                }
                return new Account(id, rs.getBigDecimal("amount"));
            }
        });
    }

    @Override
    public List<Account> getAll() {
        return sqlHelper.transactionalExecute(conn -> {
            Map<Integer, Account> resumes = new LinkedHashMap<>();

            try (PreparedStatement ps = conn.prepareStatement(LIST_ACCOUNTS)) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    Integer id = rs.getInt("id");
                    resumes.put(id, new Account(id, rs.getBigDecimal("amount")));
                }
            }
            return new ArrayList<>(resumes.values());
        });
    }

    @Override
    public void clear() {
        sqlHelper.execute(CLEAR_ACCOUNTS, (ps) -> {
            ps.execute();
            return null;
        });
    }
}
