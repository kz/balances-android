package in.iamkelv.balances.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountResponse {

    private String error;
    private Balances balances;
    private List<Purchase> purchases = new ArrayList<Purchase>();
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
     * @return The balances
     */
    public Balances getBalances() {
        return balances;
    }

    /**
     * @param balances The balances
     */
    public void setBalances(Balances balances) {
        this.balances = balances;
    }

    /**
     * @return The purchases
     */
    public List<Purchase> getPurchases() {
        return purchases;
    }

    /**
     * @param purchases The purchases
     */
    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
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