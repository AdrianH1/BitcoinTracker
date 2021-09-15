package ch.teko;

public class MarkedAddress {
    private String predecessor;
    private int searchDepth;
    private String markedAddress;

    public MarkedAddress(String predecessor, int searchDepth, String markedAddress){
        this.predecessor = predecessor;
        this.searchDepth = searchDepth;
        this.markedAddress = markedAddress; 
    }

    public String getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(String predecessor) {
        this.predecessor = predecessor;
    }

    public int getSearchDepth() {
        return searchDepth;
    }

    public void setSearchDepth(int searchDepth) {
        this.searchDepth = searchDepth;
    }

    public String getMarkedAddress() {
        return markedAddress;
    }

    public void setMarkedAddress(String markedAddress) {
        this.markedAddress = markedAddress;
    }
}
