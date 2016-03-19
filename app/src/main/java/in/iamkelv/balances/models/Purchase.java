package in.iamkelv.balances.models;

import java.util.HashMap;
import java.util.Map;

public class Purchase {

    private String date;
    private String time;
    private String item;
    private Integer price;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return Date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date Date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return Time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time Time
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * @return Item
     */
    public String getItem() {
        return item;
    }

    /**
     * @param item Item
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * @return Price
     */
    public Integer getPrice() {
        return price;
    }

    /**
     * @param price Price
     */
    public void setPrice(Integer price) {
        this.price = price;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}