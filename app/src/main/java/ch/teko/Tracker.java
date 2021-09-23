package ch.teko;

import java.util.ArrayList;
import java.util.List;

public class Tracker {

    private int depthCounter;
    private List<Address> transactions;
    private List<TransactionDetail> transactionDetails;
    private List<MarkedAddress> markedTransactions;
    private List<ToDoAddress> toDoAddresses;
    private List<ToDoAddress> nextAddresses;
    private long markedValue;
    private long valueAfterMarked;
    private TransactionOverview overview;
    private Request request;
    
    public Tracker(){
        depthCounter = 1;
        request = new Request();
        transactionDetails = new ArrayList<TransactionDetail>();  
        markedTransactions = new ArrayList<MarkedAddress>();
        toDoAddresses = new ArrayList<ToDoAddress>();  
        nextAddresses = new ArrayList<ToDoAddress>();  
    }

    public List<MarkedAddress> startSearch (String startWallet, int searchDepth) {
        nextAddresses.add(new ToDoAddress(startWallet));
        addToMarkedTransactions(new MarkedAddress(null, 0, startWallet));
        return startTracking(searchDepth);
    }

    public List<MarkedAddress> startTracking (int searchDepth) {
        //Loop through search depth
        for (depthCounter = 1; depthCounter <= searchDepth; depthCounter++){
            toDoAddresses = getTransactions(nextAddresses);
            getTransactionDetail(toDoAddresses);

            System.out.println("Suchtiefe " + depthCounter);
            for (ToDoAddress write : toDoAddresses) {
                System.out.println(write.getTransactionId());
            }
            
            toDoAddresses.clear();
        }
        
        return markedTransactions;
    }

    public List<ToDoAddress> getTransactions(List<ToDoAddress> wallets){
        //Get all Transactions for every wallet address
        for (ToDoAddress wallet : wallets) {
            transactions = request.doAddressCall(wallet.getAddress());
            for (Address transaction : transactions) {
                if (transaction.getSpentTxid() != null && !transaction.getSpentTxid().isEmpty()){
                    if () {
                        
                    }
                    //Add transaction adresses to ToDoAdresses List
                    toDoAddresses.add(new ToDoAddress(wallet.getAddress(), transaction.getSpentTxid(), transaction.getValue(), null));
                }
            }
        }
        nextAddresses.clear();
        return toDoAddresses;
    }

    public void getTransactionDetail(List<ToDoAddress> addresses){
        for (ToDoAddress address : addresses) {
            if (address.getTransactionId() != null && !address.getTransactionId().isEmpty()) {
                //Get Transaction details from API
                transactionDetails.add(request.doTransactionDetailCall(address.getTransactionId()));

                //Add Blocktime, Fee and Predecessor Adress to Transaction details list
                overview = request.doTransactionOverviewCall(address.getTransactionId());
                transactionDetails.get(transactionDetails.size()-1).setBlockTime(overview.getBlockTime());
                transactionDetails.get(transactionDetails.size()-1).setFee(overview.getFee());
                transactionDetails.get(transactionDetails.size()-1).setPredecessor(address.getAddress());

            }
        }

        for (TransactionDetail detail : transactionDetails) {
            valueAfterMarked = 0;
            markedValue = 0;
            for(int i = detail.getInputs().length-1; i >= 0; i--){
                if (!detail.getInputs()[i].getAddress().equals(detail.getPredecessor())){
                    valueAfterMarked += detail.getInputs()[i].getValue();
                } else {
                    markedValue += detail.getInputs()[i].getValue();
                    break;
                }
            }
            for (Address output : detail.getOutputs()) {
                if (!detail.getPredecessor().equals(output.getAddress())) {

                    if (valueAfterMarked <= 0 && markedValue > 0) {
                        //Add Addresses for GUI output
                        addToMarkedTransactions(new MarkedAddress(detail.getPredecessor(), depthCounter, output.getAddress()));
                        markedValue -= output.getValue();
                        if (markedValue >= detail.getFee()) {
                            //Add Wallet Addresses for next search depth
                            nextAddresses.add(new ToDoAddress(output.getAddress(), output.getValue(), detail.getBlockTime()));
                        }
                        else {
                            //Add Wallet Addresses for next search depth
                            nextAddresses.add(new ToDoAddress(output.getAddress(), output.getValue() - markedValue, detail.getBlockTime()));
                        }
                         
                    }
                    else if (valueAfterMarked - output.getValue() < 0) {
                        //Add Addresses for GUI output
                        addToMarkedTransactions(new MarkedAddress(detail.getPredecessor(), depthCounter, output.getAddress()));
                        valueAfterMarked -= output.getValue();
                        //Add Wallet Addresses for next search depth
                        nextAddresses.add(new ToDoAddress(output.getAddress(), Math.abs(valueAfterMarked), detail.getBlockTime()));
                        if (valueAfterMarked < 0) {
                            markedValue -= valueAfterMarked;
                        }
                    }
                }
            }
        }
        
    }

    public void addToMarkedTransactions (MarkedAddress markedAddress) {
        if (!markedTransactions.stream().anyMatch(m -> m.getMarkedAddress().equals(markedAddress.getMarkedAddress()))) {
            markedTransactions.add(markedAddress);
        }
    }

    // public List<MarkedAddress> startTracking (String startWallet, int searchDepth) {
    //     if (depthCounter == 1){
    //         transactions = request.doAddressCall(startWallet);
    //         getTransactionDetail(transactions, startWallet);
    //         depthCounter ++;
    //     } else {
    //         if (depthCounter > searchDepth) {
    //             transactions = request.doAddressCall(startWallet);
    //             // if (condition) {
    //             //     getTransactionDetail(transactions, startWallet);
    //             // }
    //             depthCounter ++;
    //         }
    //     }
    //     return markedTransactions;
    // }

    // public void getTransactionDetail(List<Address> transactions, String wallet){
    //     for (Address transaction : transactions){
    //         if (transaction.getSpentTxid() != null && !transaction.getSpentTxid().isEmpty()){
    //             transactionDetails.add(request.doTransactionDetailCall(transaction.getSpentTxid()));
    //             overview = request.doTransactionOverviewCall(transaction.getSpentTxid());
    //             transactionDetails.get(transactionDetails.size()-1).setBlockTime(overview.getBlockTime());
    //             transactionDetails.get(transactionDetails.size()-1).setFee(overview.getFee());
    //         }
    //     }
    //     for (TransactionDetail detail : transactionDetails){
    //         if (detail == null){
    //             continue;
    //         }
    //         valueAfterMarked = 0;
    //         markedValue = 0;
    //         toDoAddresses.clear();
    //         for(int i = detail.getInputs().length-1; i >= 0; i--){
    //             if (!detail.getInputs()[i].getAddress().equals(wallet)){
    //                 valueAfterMarked += detail.getInputs()[i].getValue();
    //             } else {
    //                 markedValue += detail.getInputs()[i].getValue();
    //             }
    //         }
            // for (Address output : detail.getOutputs()) {
            //     if (!wallet.equals(output.getAddress())) {
            //         if (valueAfterMarked <= 0 && markedValue > 0) {
            //             addToMarkedTransactions(new MarkedAddress(wallet, depthCounter, output.getAddress()));
            //             markedValue -= output.getValue();
            //             if (markedValue >= detail.getFee()) {
            //                 toDoAddresses.add(new ToDoAddress(output.getAddress(), output.getValue(), blockTime));
            //             }
            //             else {
            //                 toDoAddresses.add(new ToDoAddress(output.getAddress(), output.getValue() - markedValue, blockTime));
            //             }
                         
            //         }
            //         else if (valueAfterMarked - output.getValue() < 0) {
            //             addToMarkedTransactions(new MarkedAddress(wallet, depthCounter, output.getAddress()));
            //             valueAfterMarked -= output.getValue();
            //             toDoAddresses.add(new ToDoAddress(output.getAddress(), Math.abs(valueAfterMarked), blockTime)); 
            //             if (valueAfterMarked < 0) {
            //                 markedValue -= valueAfterMarked;
            //             }
            //         }
            //     }
    //         }
    //     }
    // }



}


