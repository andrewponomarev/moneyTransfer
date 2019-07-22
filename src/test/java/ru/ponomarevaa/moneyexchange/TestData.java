package ru.ponomarevaa.moneyexchange;

import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;

import java.math.BigDecimal;

public class TestData {

    public static final Account ACCOUNT_1 = new Account(BigDecimal.valueOf(100.0));
    public static final Account ACCOUNT_2 = new Account(BigDecimal.valueOf(200.0));

    public static final Transfer TRANSFER = new Transfer(1, 2, BigDecimal.valueOf(50.0));

}
