package ch.teko;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tracker {

    private int depthCounter;
    private List<Address> transactions;
    private List<TransactionDetail> transactionDetails;
    private List<MarkedAddress> markedTransactions;
    private List<ToDoAddress> toDoAddresses;
    private long markedValue;
    private long valueAfterMarked;
    private TransactionOverview overview;
    private Date blockTime;
    private Request request;
    
    public Tracker(){
        depthCounter = 1;
        request = new Request();
        transactionDetails = new ArrayList<TransactionDetail>();  
        markedTransactions = new ArrayList<MarkedAddress>();
        toDoAddresses = new ArrayList<ToDoAddress>();  
    }

    public List<MarkedAddress> startTracking (String startWallet, int searchDepth) {
        if (depthCounter == 1){
            transactions = request.doAddressCall(startWallet);
            getTransactionDetail(transactions, startWallet);
            depthCounter ++;
        } else {
            if (depthCounter > searchDepth) {
                depthCounter ++;
            }
        }
        return markedTransactions;
    }

    public void getTransactionDetail(List<Address> transactions, String wallet){
        for (Address transaction : transactions){
            if (transaction.getSpentTxid() != null){
                transactionDetails.add(request.doTransactionDetailCall(transaction.getSpentTxid()));
                overview = request.doTransactionOverviewCall(transaction.getSpentTxid());
                transactionDetails.get(transactionDetails.size()-1).setBlockTime(overview.getBlockTime());
                transactionDetails.get(transactionDetails.size()-1).setFee(overview.getFee());
            }
        }
        for (TransactionDetail detail : transactionDetails){
            if (detail == null){
                continue;
            }
            valueAfterMarked = 0;
            markedValue = 0;
            toDoAddresses.clear();
            for(int i = detail.getInputs().length-1; i >= 0; i--){
                if (!detail.getInputs()[i].getAddress().equals(wallet)){
                    valueAfterMarked += detail.getInputs()[i].getValue();
                } else {
                    markedValue += detail.getInputs()[i].getValue();
                }
            }
            for (Address output : detail.getOutputs()) {
                if (valueAfterMarked <= 0 && markedValue > 0) {
                    addToMarkedTransactions(new MarkedAddress(wallet, depthCounter, output.getAddress()));
                    markedValue -= output.getValue();
                    if (markedValue >= detail.getFee()) {
                        toDoAddresses.add(new ToDoAddress(output.getAddress(), output.getValue(), blockTime));
                    }
                    else {
                        toDoAddresses.add(new ToDoAddress(output.getAddress(), output.getValue() - markedValue, blockTime));
                    }
                     
                }
                else if (valueAfterMarked - output.getValue() < 0) {
                    addToMarkedTransactions(new MarkedAddress(wallet, depthCounter, output.getAddress()));
                    valueAfterMarked -= output.getValue();
                    toDoAddresses.add(new ToDoAddress(output.getAddress(), Math.abs(valueAfterMarked), blockTime)); 
                    if (valueAfterMarked < 0) {
                        markedValue -= valueAfterMarked;
                    }
                }
            }
        }
    }

    public void addToMarkedTransactions (MarkedAddress markedAddress) {
        if (!markedTransactions.stream().anyMatch(m -> m.getMarkedAddress() == markedAddress.getMarkedAddress())) {
            markedTransactions.add(markedAddress);
        }
    }

}


