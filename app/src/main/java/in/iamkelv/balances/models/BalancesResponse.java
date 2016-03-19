
package in.iamkelv.balances.models;

import java.util.HashMap;
import java.util.Map;

public class BalancesResponse {

    private String error;
    private Balances balances;
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
     * @return Balances
     */
    public Balances getBalances() {
        return balances;
    }

    /**
     * @param balances Balances
     */
    public void setBalances(Balances balances) {
        this.balances = balances;
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