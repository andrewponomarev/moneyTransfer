package ru.ponomarevaa.moneyexchange.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ponomarevaa.moneyexchange.TransferModule;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;
import ru.ponomarevaa.moneyexchange.model.Account;


import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_1;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_2;

class AccountServiceImplTest {

    protected AccountService service;

    public AccountServiceImplTest() {
        Injector injector = Guice.createInjector(new TransferModule());
        this.service = injector.getInstance(AccountService.class);
    }

    @BeforeEach
    void setUp() throws Exception {
        service.clear();
        Account newAccount_1 = new Account(ACCOUNT_1);
        Account newAccount_2 = new Account(ACCOUNT_2);
        service.create(newAccount_1);
        service.create(newAccount_2);
    }

    @Test
    void create() throws Exception {
        final Account newAccount = new Account(BigDecimal.valueOf(100.0));
        final Account createdAccount = service.create(new Account(newAccount));

        assertEquals(newAccount.getAmount(), createdAccount.getAmount());
    }

    @Test
    void createNotNew() throws Exception {
        int id = service.getAll().get(0).getId();
        final Account newAccount = new Account(id, BigDecimal.valueOf(100.0));
        assertThrows(IllegalArgumentException.class, () ->
            service.create(newAccount));
    }

    @Test
    void delete() throws Exception {
        int id = service.getAll().get(0).getId();
        service.delete(id);
        assertEquals(1, service.getAll().size());
        assertEquals( ACCOUNT_2.getAmount(), service.getAll().get(0).getAmount());
    }

    @Test
    void deletedNotFound() throws Exception {
        int id = service.getAll().get(0).getId();
        assertThrows(NotFoundException.class, () ->
            service.delete(id+2));
    }

    @Test
    void get() throws Exception {
        int id = service.getAll().get(0).getId();
        Account account = service.get(id);
        assertEquals(account.getAmount(), ACCOUNT_1.getAmount());
    }

    @Test
    void getNotFound() throws Exception {
        int id = service.getAll().get(0).getId();
        assertThrows(NotFoundException.class, () ->
                service.get(id+2));
    }

    @Test
    void update() throws Exception {
        int id = service.getAll().get(0).getId();
        Account account = new Account(id, BigDecimal.valueOf(300.0));
        service.update(account);
        assertEquals(account.getAmount(), service.get(id).getAmount());
    }

}