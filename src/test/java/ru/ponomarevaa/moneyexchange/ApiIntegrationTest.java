package ru.ponomarevaa.moneyexchange;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.service.AccountService;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.stream.IntStream.rangeClosed;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ApiIntegrationTest {

    private static final int DEFAULT_REST_PORT = 4567;

    private HttpClient httpClient;
    private static TransferApp app;
    private static AccountService accountService;
    private static Integer sourceId;
    private static Integer destinationId;

    @BeforeAll
    static void setUpOnce() throws Exception {
        app = new TransferApp();
        app.start();

        Injector injector = Guice.createInjector(new TransferModule());
        accountService = injector.getInstance(AccountService.class);
        sourceId = accountService.create(new Account(BigDecimal.valueOf(2000))).getId();
        destinationId = accountService.create(new Account(BigDecimal.valueOf(1000))).getId();
    }

    @BeforeEach
    void setUp() {
        httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    }

    @Test
     void transferEmptyAmount() throws Exception {
        transferFixture(sourceId.toString(), destinationId.toString(), "", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferNotNumAmount() throws Exception {
        transferFixture(sourceId.toString(), destinationId.toString(), "k", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferEmptySource() throws Exception {
        transferFixture("",  destinationId.toString(), "100", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferNotNumSource() throws Exception {
        transferFixture("d",  destinationId.toString(), "100", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferEmptyDestination() throws Exception {
        transferFixture(sourceId.toString(),  "", "100", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferNotNumDestination() throws Exception {
        transferFixture(sourceId.toString(),  "d", "100", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferSameFromToAccount() throws Exception {
        transferFixture(sourceId.toString(), sourceId.toString(), "111", HttpStatus.BAD_REQUEST_400);
    }

    @Test
    void transferOK() throws Exception {
        transferFixture(sourceId.toString(), destinationId.toString(), "100", HttpStatus.OK_200);
        // Then
        assertEquals(BigDecimal.valueOf(1900L), accountService.get(sourceId).getAmount());
        assertEquals(BigDecimal.valueOf(1100L), accountService.get(destinationId).getAmount());
    }

    @Test
    void transferConcurrencyOK() throws Exception {
        Assertions.assertTimeout(Duration.ofMillis(6000), () -> {
            // Given
            final int transactionsNum = 10;
            final Integer accountId1 = accountService.create(new Account(BigDecimal.TEN)).getId();
            final Integer accountId2 = accountService.create(new Account(BigDecimal.TEN)).getId();
            final Integer accountId3 = accountService.create(new Account(BigDecimal.TEN)).getId();
            final CountDownLatch startTransaction = new CountDownLatch(1);
            final CountDownLatch transactionsCompleted = new CountDownLatch(transactionsNum * 2);
            final ExecutorService executorService = Executors.newFixedThreadPool(transactionsNum * 2);

            // When
            rangeClosed(1, transactionsNum).forEach(value -> {
                        submitTransferFixture(accountId1, accountId2, startTransaction, transactionsCompleted,
                                executorService);
                        submitTransferFixture(accountId2, accountId3, startTransaction, transactionsCompleted,
                                executorService);
                    }
            );
            startTransaction.countDown();

            // Then
            transactionsCompleted.await();
            assertEquals(0,
                    BigDecimal.valueOf(9.9).compareTo(accountService.get(accountId1).getAmount()));
            assertEquals(0,
                    BigDecimal.valueOf(10.0).compareTo(accountService.get(accountId2).getAmount()));
            assertEquals(0,
                    BigDecimal.valueOf(10.1).compareTo(accountService.get(accountId3).getAmount()));
        });
    }

    private void submitTransferFixture(Integer sourceId, Integer destinationId,
                                       CountDownLatch startTransaction, CountDownLatch transactionsCompleted,
                                       ExecutorService executorService) {
        executorService.execute(() -> {
            try {
                startTransaction.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            try {
                transferFixture(sourceId.toString(), destinationId.toString(), "0.01", HttpStatus.OK_200);
            } catch (Exception e) {
                fail(e.toString());
            }
            transactionsCompleted.countDown();
        });
    }

    private void transferFixture(String sourceId, String destinationId, String amount, int expectedStatusCode)
            throws IOException, InterruptedException {
        final String body = "{" +
                "\"sourceId\":" + sourceId +
                ",\"destinationId\":" + destinationId +
                ",\"amount\":" + amount +
                "}";
        final HttpRequest httpRequest = postHttpRequest("/transfers", body);
        final HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(expectedStatusCode, response.statusCode());
    }

    private HttpRequest postHttpRequest(String path, String body) {
        final URI uri = URI.create("http://localhost:" + DEFAULT_REST_PORT + path);
        return HttpRequest.newBuilder(uri).POST(HttpRequest.BodyPublishers.ofString(body)).build();
    }


}
