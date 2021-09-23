package ch.teko;

import java.util.Date;

public class ToDoAddress {
    private String address;
    private String transactionId;
    private long value;
    private Date blockTime;


    public ToDoAddress(String address, String transactionId, long value, Date blockTime) {
        this.address = address;
        this.value = value;
        this.blockTime = blockTime;
        this.transactionId = transactionId;
    }

    public ToDoAddress(String address, long value, Date blockTime){
        this.address = address;
        this.value = value;
        this.blockTime = blockTime;
    }

    public ToDoAddress(String address){
        this.address = address;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }
}
