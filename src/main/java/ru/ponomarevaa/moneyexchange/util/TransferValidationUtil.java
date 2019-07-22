package ru.ponomarevaa.moneyexchange.util;

import ru.ponomarevaa.moneyexchange.exception.NotEnoughMoneyException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;

public class TransferValidationUtil {

    private TransferValidationUtil() {
    }

    public static void checkNegativeAmount(Transfer transfer) {
        if (transfer.getAmount().doubleValue() < 0) {
            throw new IllegalArgumentException("Transfer amount must be not negative but = " + transfer.getAmount());
        }
    }

    public static void checkTheSameSourceAndDestinationId(Transfer transfer) {
        if (transfer.getSourceId() == transfer.getDestinationId()) {
            throw new IllegalArgumentException("Source and destination id must be different");
        }
    }

    public static void checkBalanceIsEnoughForTransfer(Account source, Transfer transfer) {
        if (source.getAmount().doubleValue() < transfer.getAmount().doubleValue()) {
            throw new NotEnoughMoneyException(source);
        }
    }

    public static void checkIdTheSame(Transfer transfer, Account source, Account destination) {
        if (transfer.getSourceId() != source.getId() ||
                transfer.getDestinationId() != destination.getId()) {
            throw new IllegalArgumentException("Id in transfer not equals ids in source and destinatrion");
        }
    }
}
