package ru.ponomarevaa.moneyexchange;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ponomarevaa.moneyexchange.rest.AccountController;
import ru.ponomarevaa.moneyexchange.rest.ControllerExceptionHandler;
import ru.ponomarevaa.moneyexchange.rest.TransferController;
import ru.ponomarevaa.moneyexchange.service.AccountService;
import ru.ponomarevaa.moneyexchange.service.TransferService;

import static spark.Spark.after;
import static spark.Spark.before;


public class TransferApp {

    private static Logger log = LoggerFactory.getLogger(TransferApp.class);

    public static void main(String[] args) {
        new TransferApp().start();
    }

    public void start() {
        Injector injector = Guice.createInjector(new TransferModule());

        before("/*", (q, a) -> log.info("Received api call"));

        ControllerExceptionHandler.init();
        AccountController.init(injector.getInstance(AccountService.class));
        TransferController.init(injector.getInstance(TransferService.class));

        after("*", ((request, response) -> {
            response.header("Content-Type", "application/json");
            response.header("Content-Encoding", "gzip");
        }));
    }
}
