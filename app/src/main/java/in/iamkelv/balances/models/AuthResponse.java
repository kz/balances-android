package in.iamkelv.balances.models;

import java.util.HashMap;
import java.util.Map;

public class AuthResponse {

    private String error;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return Error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error Error
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message Message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}