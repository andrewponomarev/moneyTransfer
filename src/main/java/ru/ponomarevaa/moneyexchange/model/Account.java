package ru.ponomarevaa.moneyexchange.model;

import java.math.BigDecimal;

public class Account extends AbstractBaseEntity {

    private BigDecimal amount;

    public Account(BigDecimal amount) {
        super();
        this.amount = amount;
    }

    public Account(Integer id, BigDecimal amount) {
        super(id);
        this.amount = amount;
    }

    public Account(Account other) {
        super(other.id);
        this.amount = other.amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "amount=" + amount +
                ", id=" + id +
                '}';
    }
}
