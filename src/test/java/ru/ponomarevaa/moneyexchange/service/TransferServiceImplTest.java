package ru.ponomarevaa.moneyexchange.service;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ponomarevaa.moneyexchange.TransferModule;
import ru.ponomarevaa.moneyexchange.exception.NotEnoughMoneyException;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_1;
import static ru.ponomarevaa.moneyexchange.TestData.ACCOUNT_2;
import static ru.ponomarevaa.moneyexchange.TestData.TRANSFER;


class TransferServiceImplTest {

    protected TransferService service;

    protected AccountService accountService;

    TransferServiceImplTest() {
        Injector injector = Guice.createInjector(new TransferModule());
        this.service = injector.getInstance(TransferService.class);
        this.accountService = injector.getInstance(AccountService.class);
    }

    @BeforeEach
    void setUp() throws Exception {
        accountService.clear();
        Account newAccount_1 = new Account(ACCOUNT_1);
        Account newAccount_2 = new Account(ACCOUNT_2);
        accountService.create(newAccount_1);
        accountService.create(newAccount_2);

        service.clear();
        final Transfer newTransfer = new Transfer(
                accountService.getAll().get(0).getId(),
                accountService.getAll().get(1).getId(),
                TRANSFER.getAmount());
        Transfer transfer = new Transfer(newTransfer);
        service.create(transfer);
    }

    @Test
    void get() {
        int id = service.getAll().get(0).getId();
        Transfer transfer = service.get(id);
        assertEquals(transfer.getAmount(), TRANSFER.getAmount());
        assertEquals(transfer.getSourceId(), service.getAll().get(0).getSourceId());
        assertEquals(transfer.getDestinationId(), service.getAll().get(0).getDestinationId());
    }

    @Test
    void create() {
        final Transfer newTransfer = new Transfer(
                accountService.getAll().get(0).getId(),
                accountService.getAll().get(1).getId(),
                TRANSFER.getAmount());

        double initSourceBalance = accountService.get(newTransfer.getSourceId()).getAmount().doubleValue();
        double initDestinationValue = accountService.get(newTransfer.getDestinationId()).getAmount().doubleValue();

        final Transfer createdTransfer = service.create(newTransfer);

        assertEquals(newTransfer.getAmount(), createdTransfer.getAmount());
        assertEquals(newTransfer.getSourceId(), createdTransfer.getSourceId());
        assertEquals(newTransfer.getDestinationId(), createdTransfer.getDestinationId());

        assertEquals(initSourceBalance - newTransfer.getAmount().doubleValue(),
                accountService.get(newTransfer.getSourceId()).getAmount().doubleValue());

        assertEquals(initDestinationValue + newTransfer.getAmount().doubleValue(),
                accountService.get(newTransfer.getDestinationId()).getAmount().doubleValue());
    }

    @Test
    void createNotNew() throws Exception {


        final Transfer newTransfer = new Transfer(1, 1,2, BigDecimal.valueOf(100.0));
        assertThrows(IllegalArgumentException.class, () ->
                service.create(newTransfer));
    }

    @Test
    void createWithNegativeAmount() throws Exception {
        final Transfer newTransfer = new Transfer(1,2,BigDecimal.valueOf(-100.0));
        assertThrows(IllegalArgumentException.class, () ->
                service.create(newTransfer));
    }

    @Test
    void createTheSameSourceAndDestinationId() throws Exception {
        final Transfer newTransfer = new Transfer( 1,1,BigDecimal.valueOf(100.0));
        assertThrows(IllegalArgumentException.class, () ->
                service.create(newTransfer));
    }

    @Test
    void createNotEnoughMoney() throws Exception {
        final Transfer newTransfer = new Transfer(
                accountService.getAll().get(0).getId(),
                accountService.getAll().get(1).getId(),
                new BigDecimal(1000.0));;
        assertThrows(NotEnoughMoneyException.class, () ->
                service.create(newTransfer));
    }
}