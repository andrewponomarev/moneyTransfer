package ru.ponomarevaa.moneyexchange.service;

import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;

import java.util.List;

public interface AccountService {

    Account get(int id) throws NotFoundException;

    List<Account> getAll();

    Account create(Account account);

    void delete(int id) throws NotFoundException;

    void clear();

}
