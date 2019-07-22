package ru.ponomarevaa.moneyexchange.service;

import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Transfer;

import java.util.List;

public interface TransferService {

    Transfer get(int id) throws NotFoundException;

    List<Transfer> getAll();

    Transfer create(Transfer transfer);

    void clear();

}
