package ch.teko;

public class Address {
    private String spentTxid;
    private String address;
    private long value;

    public String getSpentTxid(){
        return spentTxid;
    }

    public long getValue(){
        return value;
    }

    public String getAddress(){
        return address;
    }
}
