package ru.ponomarevaa.moneyexchange.repository;

import ru.ponomarevaa.moneyexchange.model.Account;

import java.util.List;

public interface AccountRepository {

    // null if not found
    Account save(Account acct);

    // false if not found
    boolean delete(int id);

    // null if not found
    Account get(int id);

    List<Account> getAll();

    void clear();

}
