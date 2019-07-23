package ru.ponomarevaa.moneyexchange;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;
import ru.ponomarevaa.moneyexchange.repository.hbase.SqlAccountRepository;
import ru.ponomarevaa.moneyexchange.repository.hbase.SqlTransferRepository;
import ru.ponomarevaa.moneyexchange.repository.TransferRepository;
import ru.ponomarevaa.moneyexchange.service.AccountService;
import ru.ponomarevaa.moneyexchange.service.AccountServiceImpl;
import ru.ponomarevaa.moneyexchange.service.TransferService;
import ru.ponomarevaa.moneyexchange.service.TransferServiceImpl;

public class TransferModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AccountRepository.class).to(SqlAccountRepository.class).in(Singleton.class);
        bind(AccountService.class).to(AccountServiceImpl.class);

        bind(TransferRepository.class).to(SqlTransferRepository.class).in(Singleton.class);
        bind(TransferService.class).to(TransferServiceImpl.class);
    }
}
