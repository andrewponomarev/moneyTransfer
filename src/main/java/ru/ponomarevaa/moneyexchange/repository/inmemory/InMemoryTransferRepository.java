package ru.ponomarevaa.moneyexchange.repository.inmemory;

import com.google.inject.Inject;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;
import ru.ponomarevaa.moneyexchange.repository.AccountRepository;
import ru.ponomarevaa.moneyexchange.repository.TransferRepository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static ru.ponomarevaa.moneyexchange.util.TransferValidationUtil.checkIdTheSame;

public class InMemoryTransferRepository extends InMemoryBaseRepository<Transfer> implements TransferRepository {

    private AccountRepository accountRepository;

    private AtomicLong counter;

    private Map<Integer, Transfer> entryMap;

    @Inject
    public InMemoryTransferRepository(AccountRepository accountRepository) {
        this.counter = new AtomicLong(0);
        this.entryMap = new ConcurrentHashMap<>();
        this.accountRepository = accountRepository;
    }

    @Override
    public Transfer save(Transfer transfer, Account source, Account destination) {
        checkIdTheSame(transfer, source, destination);

        Object lock1 = source.getId() < destination.getId() ? source : destination;
        Object lock2 = source.getId() < destination.getId() ? destination : source;

        synchronized (lock1) {
            synchronized (lock2) {
                source = accountRepository.get(transfer.getSourceId());
                destination = accountRepository.get(transfer.getDestinationId());
                BigDecimal amount = transfer.getAmount();

                source.widrawal(amount);
                destination.add(amount);

                accountRepository.save(source);
                accountRepository.save(destination);

                return super.save(transfer);
            }
        }
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean delete(int id) {
        throw new UnsupportedOperationException();
    }


}
