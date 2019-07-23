package ru.ponomarevaa.moneyexchange.service;

import com.google.inject.Inject;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;
import ru.ponomarevaa.moneyexchange.repository.TransferRepository;
import spark.utils.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static ru.ponomarevaa.moneyexchange.util.TransferValidationUtil.checkBalanceIsEnoughForTransfer;
import static ru.ponomarevaa.moneyexchange.util.TransferValidationUtil.checkNegativeAmount;
import static ru.ponomarevaa.moneyexchange.util.TransferValidationUtil.checkTheSameSourceAndDestinationId;
import static ru.ponomarevaa.moneyexchange.util.ValidationUtil.checkNew;
import static ru.ponomarevaa.moneyexchange.util.ValidationUtil.checkNotFoundWithId;


public class TransferServiceImpl implements TransferService {

    private final TransferRepository repository;

    private final AccountRepository accountRepository;

    @Inject
    public TransferServiceImpl(TransferRepository repository, AccountRepository accountRepository) {
        this.repository = repository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Transfer get(int id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    @Override
    public List<Transfer> getAll() {
        return repository.getAll();
    }

    @Override
    public Transfer create(Transfer transfer) {
        Assert.notNull(transfer, "transfer must not be null");
        checkNew(transfer);
        checkNegativeAmount(transfer);
        checkTheSameSourceAndDestinationId(transfer);

        Account source = accountRepository.get(transfer.getSourceId());
        Account destination = accountRepository.get(transfer.getDestinationId());
        Assert.notNull(source, "source must not be null");
        Assert.notNull(destination, "destination must not be null");

        return repository.save(transfer);
    }

    @Override
    public void clear() {
        repository.clear();
    }
}
