package ru.ponomarevaa.moneyexchange.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ponomarevaa.moneyexchange.TransferModule;
import ru.ponomarevaa.moneyexchange.exception.AlreadyExistsException;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Account;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_1;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_2;

class AccountServiceImplTest {

    protected AccountService service;
    private static Integer sourceId;
    private static Integer destinationId;

    AccountServiceImplTest() {
        Injector injector = Guice.createInjector(new TransferModule());
        this.service = injector.getInstance(AccountService.class);
    }

    @BeforeEach
    void setUp() throws Exception {
        service.clear();
        Account newAccount_1 = new Account(ACCOUNT_1);
        Account newAccount_2 = new Account(ACCOUNT_2);
        sourceId = service.create(newAccount_1).getId();
        destinationId = service.create(newAccount_2).getId();
    }

    @Test
    void create() throws Exception {
        final Account newAccount = new Account(BigDecimal.valueOf(100.0));
        final Account createdAccount = service.create(new Account(newAccount));

        assertEquals(0, newAccount.getAmount().compareTo(createdAccount.getAmount()));
    }

    @Test
    void createNotNew() throws Exception { ;
        final Account newAccount = new Account(sourceId, BigDecimal.valueOf(100.0));
        assertThrows(AlreadyExistsException.class, () ->
            service.create(newAccount));
    }

    @Test
    void delete() throws Exception {
        service.delete(sourceId);
        assertEquals(1, service.getAll().size());
        assertEquals(0,  ACCOUNT_2.getAmount().compareTo(service.get(destinationId).getAmount()));
    }

    @Test
    void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
            service.delete(sourceId+2));
    }

    @Test
    void get() throws Exception {
        Account account = service.get(sourceId);
        assertEquals(0, account.getAmount().compareTo(ACCOUNT_1.getAmount()));
    }

    @Test
    void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () ->
                service.get(sourceId+2));
    }

}