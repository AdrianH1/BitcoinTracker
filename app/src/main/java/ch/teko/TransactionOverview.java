package ch.teko;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionOverview {
    private String txid;
    private String blockTime;
    private int inputCount;
    private int outputCount;
    private long value;
    private long fee;
    private int confirmations;

    public Date getBlockTime() {
        Date date = new Date();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
            
            date = formatter.parse(blockTime); 
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return date;
    }

    public long getFee() {
        return fee;
    }
}
