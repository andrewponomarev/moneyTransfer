package ru.ponomarevaa.moneyexchange.rest;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.ponomarevaa.moneyexchange.model.Account;
import ru.ponomarevaa.moneyexchange.model.Transfer;
import ru.ponomarevaa.moneyexchange.service.TransferService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.List;

import static ru.ponomarevaa.moneyexchange.util.GsonUtil.successResponse;
import static ru.ponomarevaa.moneyexchange.util.ValidationUtil.checkNew;
import static spark.Spark.get;
import static spark.Spark.path;
import static spark.Spark.post;

public class TransferController {

    private static final Logger log = LoggerFactory.getLogger(TransferController.class);

    private static TransferService service;

    public static void init(TransferService service) {
        TransferController.service = service;

        path("/transfers", () -> {
            get("", TransferController.getAll);
            get("/:id", TransferController.get);
            post("", TransferController.create);
        });
    }

    public static Route getAll = (Request request, Response response) -> {
        log.info("getAll");
        List<Transfer> transfers = service.getAll();
        return successResponse(transfers);
    };

    public static Route get = (Request request, Response response) -> {
        int id = Integer.valueOf(request.params(":id"));
        log.info("get {}", id);
        Transfer transfer = service.get(id);
        return successResponse(transfer);
    };

    public static Route create = (Request request, Response response) -> {
        Transfer transfer = new Gson().fromJson(request.body(), Transfer.class);
        log.info("create {}", transfer);
        return successResponse(service.create(transfer));
    };

}
