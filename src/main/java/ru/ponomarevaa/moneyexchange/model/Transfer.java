package ru.ponomarevaa.moneyexchange.model;

import java.math.BigDecimal;

public class Transfer extends AbstractBaseEntity {

    private Integer sourceId;

    private Integer destinationId;

    private BigDecimal amount;

    public Transfer(Integer source, Integer destinationId, BigDecimal amount) {
        this.sourceId = source;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    public Transfer(Integer id, Integer sourceId, Integer destinationId, BigDecimal amount) {
        super(id);
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.amount = amount;
    }

    public Transfer(Transfer other) {
        super(other.id);
        this.sourceId = other.sourceId;
        this.destinationId = other.destinationId;
        this.amount = other.amount;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                ", amount=" + amount +
                ", id=" + id +
                '}';
    }
}
