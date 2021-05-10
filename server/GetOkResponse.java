package server;

import com.google.gson.JsonElement;

public class GetOkResponse {

    private String response;
    private JsonElement value;

    public GetOkResponse(String response, JsonElement value) {
        this.response = response;
        this.value = value;
    }
}
