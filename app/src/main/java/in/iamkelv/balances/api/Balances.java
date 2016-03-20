package in.iamkelv.balances.api;

import java.util.HashMap;
import java.util.Map;

public class Balances {

    private Integer lunch;
    private Integer tuck;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return Lunch balance
     */
    public Integer getLunch() {
        return lunch;
    }

    /**
     * @param lunch Lunch balance
     */
    public void setLunch(Integer lunch) {
        this.lunch = lunch;
    }

    /**
     * @return Tuck balance
     */
    public Integer getTuck() {
        return tuck;
    }

    /**
     * @param tuck Tuck balance
     */
    public void setTuck(Integer tuck) {
        this.tuck = tuck;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}