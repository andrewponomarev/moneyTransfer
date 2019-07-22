package ru.ponomarevaa.moneyexchange.exception;

import ru.ponomarevaa.moneyexchange.model.Account;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(Account account) {
        super("Not enough money in the account " + account);
    }
}
