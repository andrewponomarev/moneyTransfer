package ru.ponomarevaa.moneyexchange.util;

import com.google.gson.Gson;

public class GsonUtil {

    public static String errorResponse(String message) {
        return new Gson().toJson(new JsonResponse(StatusResponse.ERROR, message));
    }

    public static String successResponse(Object obj) {
        return new Gson().toJson(
                new JsonResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(obj)));
    }

    public static String successResponse() {
        return new Gson().toJson(
                new JsonResponse(StatusResponse.SUCCESS));
    }
}
