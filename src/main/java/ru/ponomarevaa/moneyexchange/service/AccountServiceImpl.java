package ru.ponomarevaa.moneyexchange.service;

import com.google.inject.Inject;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;
import spark.utils.Assert;

import java.util.List;

import static ru.ponomarevaa.moneyexchange.util.ValidationUtil.checkNew;
import static ru.ponomarevaa.moneyexchange.util.ValidationUtil.checkNotFoundWithId;

public class AccountServiceImpl implements AccountService {

    private final AccountRepository repository;

    @Inject
    public AccountServiceImpl(AccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public Account get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Account> getAll() {
        return repository.getAll();
    }

    @Override
    public Account create(Account account) {
        Assert.notNull(account, "account must not be null");
        checkNew(account);
        return repository.save(account);
    }

    @Override
    public void delete(int id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Override
    public void clear() {
        repository.clear();
    }


}
