package ru.ponomarevaa.moneyexchange.exception;

public class StorageException extends RuntimeException {

    public StorageException(Exception e) {
        super(e);
    }

}
