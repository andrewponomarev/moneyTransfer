package ru.ponomarevaa.moneyexchange.rest;

import com.google.gson.JsonSyntaxException;
import ru.ponomarevaa.moneyexchange.exception.AlreadyExistsException;
import ru.ponomarevaa.moneyexchange.exception.NotEnoughMoneyException;
import ru.ponomarevaa.moneyexchange.exception.NotFoundException;

import static ru.ponomarevaa.moneyexchange.util.GsonUtil.errorResponse;
import static spark.Spark.exception;

public class ControllerExceptionHandler {

    public static void init() {
        exception(IllegalArgumentException.class, (e, rq, rs) -> {
            rs.status(400);
            rs.body(errorResponse(e.getMessage()));
        });
        exception(JsonSyntaxException.class, (e, rq, rs) -> {
            rs.status(400);
            rs.body(errorResponse(e.getMessage()));
        });
        exception(NotEnoughMoneyException.class, (e, rq, rs) -> {
            rs.status(400);
            rs.body(errorResponse(e.getMessage()));
        });
        exception(NotFoundException.class, (e, rq, rs) -> {
            rs.status(404);
            rs.body(errorResponse(e.getMessage()));
        });
        exception(AlreadyExistsException.class, (e, rq, rs) -> {
            rs.status(404);
            rs.body(errorResponse(e.getMessage()));
        });
        exception(Exception.class, (e, req, res) -> {
            res.status(500);
            res.body(errorResponse("Internal server error"));
        });
    }

}
