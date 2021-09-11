package ch.teko;

import java.util.ArrayList;
import java.util.List;

public class Tracker {

    private int depthCounter;
    private List<Address> transactions;
    private List<TransactionDetail> transactionDetails;
    private List<String> markedTransactions;
    private long markedValue;
    private long valueAfterMarked;
    private Request request;
    
    public Tracker(){
        depthCounter = 1;
        request = new Request();
        transactionDetails = new ArrayList<TransactionDetail>();    
    }

    public void startTracking (String startWallet, int searchDepth) {
        if (depthCounter == 1){
            transactions = request.doAddressCall(startWallet);
            getTransactionDetail(transactions, startWallet);
        } else {

        }
    }

    public void getTransactionDetail(List<Address> transactions, String wallet){
        for (Address transaction : transactions){
            if (transaction.getSpentTxid() != null){
                transactionDetails.add(request.doTransactionDetailCall(transaction.getSpentTxid()));
            }
        }
        for (TransactionDetail detail : transactionDetails){
            if (detail == null){
                continue;
            }
            valueAfterMarked = 0;
            markedValue = 0;
            for(int i = detail.getInputs().length-1; i >= 0; i--){
                if (!detail.getInputs()[i].getAddress().equals(wallet)){
                    valueAfterMarked += detail.getInputs()[i].getValue();
                } else {
                    markedValue += detail.getInputs()[i].getValue();
                }
            }
        }
    }

}


