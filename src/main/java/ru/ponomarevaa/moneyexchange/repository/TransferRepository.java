package ru.ponomarevaa.moneyexchange.repository;

import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;

import java.util.List;

public interface TransferRepository {

    Transfer save(Transfer transfer);

    // null if not found
    Transfer get(int id);

    List<Transfer> getAll();

    void clear();
}
