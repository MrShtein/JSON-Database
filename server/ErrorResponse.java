package server;

public class ErrorResponse {

    private String response;
    private String reason;

    public ErrorResponse(String response, String reason) {
        this.response = response;
        this.reason = reason;
    }
}
