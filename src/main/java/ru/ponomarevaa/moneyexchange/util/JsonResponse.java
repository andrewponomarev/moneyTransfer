package ru.ponomarevaa.moneyexchange.util;

import com.google.gson.JsonElement;

public class JsonResponse {

    private StatusResponse status;
    private String message;
    private JsonElement data;

    public JsonResponse(StatusResponse status) {
        this.status = status;
    }

    public JsonResponse(StatusResponse status, String message) {
        this.status = status;
        this.message = message;
    }

    public JsonResponse(StatusResponse status, JsonElement data) {
        this.status = status;
        this.data = data;
    }

    public StatusResponse getStatus() {
        return status;
    }

    public void setStatus(StatusResponse status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public JsonElement getData() {
        return data;
    }

    public void setData(JsonElement data) {
        this.data = data;
    }

}
