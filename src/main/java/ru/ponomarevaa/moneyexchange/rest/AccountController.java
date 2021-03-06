package ru.ponomarevaa.moneyexchange.rest;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.service.AccountService;
import spark.Route;

import java.util.List;

import static ru.ponomarevaa.moneyexchange.util.GsonUtil.successResponse;
import static spark.Spark.*;

public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    private static AccountService service;

    public static Route getAll = (rq, rs) -> {
        log.info("getAll");
        List<Account> accounts = service.getAll();
        return successResponse(accounts);
    };

    public static Route get = (rq, rs) -> {
        int id = Integer.valueOf(rq.params(":id"));
        log.info("get {}", id);
        Account account = service.get(id);
        return successResponse(account);
    };

    public static Route create = (rq, rs) -> {
        Account account = new Gson().fromJson(rq.body(), Account.class);
        log.info("create {}", account);
        return successResponse(service.create(account));
    };

    public static Route delete = (rq, rs) -> {
        int id = Integer.valueOf(rq.params(":id"));
        log.info("delete {}", id);
        service.delete(id);
        return successResponse();
    };

    public static void init(AccountService service) {
        AccountController.service = service;

        path("/accounts", () -> {
            get("", AccountController.getAll);
            get("/:id", AccountController.get);
            post("", AccountController.create);
            delete("/:id", AccountController.delete);
        });
    }


}
