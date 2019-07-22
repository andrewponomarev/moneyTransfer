package ru.ponomarevaa.moneyexchange.repository.inmemory;

import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;

public class InMemoryAccountRepository extends InMemoryBaseRepository<Account> implements AccountRepository {

    @Override
    public void clear() {

    }
}
