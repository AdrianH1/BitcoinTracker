package ch.teko;

import java.util.Date;

public class TransactionDetail {
    private Address[] inputs; 
    private Address[] outputs;
    private Date blockTime;
    private long fee;
    private String predecessor;

    public Address[] getInputs() {
        return inputs;
    }

    public Address[] getOutputs() {
        return outputs;
    }

    public Date getBlockTime() {
        return blockTime;
    }

    public void setBlockTime(Date blockTime) {
        this.blockTime = blockTime;
    }

    public long getFee() {
        return fee;
    }

    public void setFee(long fee) {
        this.fee = fee;
    }

    public String getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }
}
