package in.iamkelv.balances.models;

import com.orm.SugarRecord;

public class DbPurchase extends SugarRecord {

    private String item;
    private long date;
    private String time;
    private Integer price;

    public DbPurchase() {

    }

    public DbPurchase(String item, long date, String time, Integer price) {
        this.item = item;
        this.date = date;
        this.time = time;
        this.price = price;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }
}
