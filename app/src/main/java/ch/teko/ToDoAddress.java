package ch.teko;

import java.util.Date;

public class ToDoAddress {
    private String parentAddress;
    private String transactionId;
    private long value;
    private Date blockTime;


    public ToDoAddress(String parentAddress, String transactionId, long value, Date blockTime) {
        this.parentAddress = parentAddress;
        this.value = value;
        this.blockTime = blockTime;
        this.transactionId = transactionId;
    }

    public ToDoAddress(String parentAddress, long value, Date blockTime){
        this.parentAddress = parentAddress;
        this.value = value;
        this.blockTime = blockTime;
    }

    public ToDoAddress(String parentAddress){
        this.parentAddress = parentAddress;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getParentAddress() {
        return parentAddress;
    }

    public void setParentAddress(String parentAddress) {
        this.parentAddress = parentAddress;
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
