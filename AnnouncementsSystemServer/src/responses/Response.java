package responses;

public class Response {
    protected final String response;
    protected final String message;

    public Response(String response, String message) {
        this.response = response;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Response{" +
                "response='" + response + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}