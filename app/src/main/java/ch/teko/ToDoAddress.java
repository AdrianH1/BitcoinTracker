package ch.teko;

import java.util.Date;

public class ToDoAddress {
    private String address;
    private long value;
    private Date blockTime;


    public ToDoAddress(String address, long value, Date blockTime) {
        this.address = address;
        this.value = value;
        this.blockTime = blockTime;
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
