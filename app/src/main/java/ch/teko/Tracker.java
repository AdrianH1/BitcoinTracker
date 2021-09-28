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

    /**
     * Constructor: Creates Lists and Request Object. Sets default SearchDepth
     */
    public Tracker(){
        depthCounter = 1;
        request = new Request();
        transactionDetails = new ArrayList<TransactionDetail>();  
        markedTransactions = new ArrayList<MarkedAddress>();
        toDoAddresses = new ArrayList<ToDoAddress>();  
        nextAddresses = new ArrayList<ToDoAddress>();  
    }

    /**
     * Start Tracking Process. Adds Address from user input to List
     * @param startWallet Wallet from user input
     * @param searchDepth Search Depth from user input
     * @return List of all marked Transactions
     */
    public List<MarkedAddress> startSearch (String startWallet, int searchDepth) {
        nextAddresses.add(new ToDoAddress(startWallet));
        addToMarkedTransactions(new MarkedAddress(null, 0, startWallet));
        return startTracking(searchDepth);
    }

    /**
     * Loops through search depth, gets Transaction from wallet addresse. Starts Transaction Detail Process
     * @param searchDepth Search Depth from user input
     * @return List of all marked Transactions
     */
    public List<MarkedAddress> startTracking (int searchDepth) {
        //Loop through search depth
        for (depthCounter = 1; depthCounter <= searchDepth; depthCounter++){
            toDoAddresses = getTransactions(nextAddresses);
            getTransactionDetail(toDoAddresses);
            
            toDoAddresses.clear();
        }
        
        return markedTransactions;
    }

    /**
     * Get Transactions of wallet addresses. Add addresses to List ToDoAddresses
     * @param wallets List of wallet addresses
     * @return List of ToDoAddresses for current search depth
     */
    public List<ToDoAddress> getTransactions(List<ToDoAddress> wallets){
        //Get all Transactions for every wallet address
        for (ToDoAddress wallet : wallets) {
            transactions = request.doAddressCall(wallet.getParentAddress());
            for (Address transaction : transactions) {
                if (transaction.getSpentTxid() != null && !transaction.getSpentTxid().isEmpty()){
                    //Add transaction adresses to ToDoAdresses List
                    toDoAddresses.add(new ToDoAddress(wallet.getParentAddress(), transaction.getSpentTxid(), transaction.getValue(), null));
                }
            }
        }
        return toDoAddresses;
    }

    /**
     * Add Blocktime, Fee and Predecessor Adress to Transaction and filter if transaction is newer than our parent transaction block time 
     * Calculate which addresses get bitcoins from marked wallet
     * @param addresses List of ToDoAddresses for current search depth
     */
    public void getTransactionDetail(List<ToDoAddress> addresses){
        for (ToDoAddress address : addresses) {
            //Get Transaction details from API
            transactionDetails.add(request.doTransactionDetailCall(address.getTransactionId()));

            //Add Blocktime, Fee and Predecessor Adress to Transaction details list
            overview = request.doTransactionOverviewCall(address.getTransactionId());
            transactionDetails.get(transactionDetails.size()-1).setBlockTime(overview.getBlockTime());
            transactionDetails.get(transactionDetails.size()-1).setFee(overview.getFee());
            transactionDetails.get(transactionDetails.size()-1).setPredecessor(address.getParentAddress());

            //Filter if transaction is newer than our parent transaction block time 
            for (ToDoAddress lastAddress : nextAddresses) {
                if (lastAddress.getParentAddress().equals(address.getParentAddress())) {
                    if (lastAddress.getBlockTime() != null && lastAddress.getBlockTime().after(transactionDetails.get(transactionDetails.size()-1).getBlockTime())) {
                        transactionDetails.remove(transactionDetails.size()-1);
                    }
                }
            }
        }
        nextAddresses.clear();
        //Calculate which addresses get bitcoins from marked wallet
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
                else {
                    if (valueAfterMarked <= 0 && markedValue > 0) {
                        markedValue -= output.getValue();
                    } 
                    else if (valueAfterMarked - output.getValue() < 0) {
                        valueAfterMarked -= output.getValue();
                        if (valueAfterMarked < 0) {
                            markedValue -= valueAfterMarked;
                        }
                    }
                }
            }
        }
    }

    /**
     * Filter marked Address to avoid duplicates
     * @param markedAddress Single Address to check if already contained in marked Address list
     */
    public void addToMarkedTransactions (MarkedAddress markedAddress) {
        if (!markedTransactions.stream().anyMatch(m -> m.getMarkedAddress().equals(markedAddress.getMarkedAddress()))) {
            markedTransactions.add(markedAddress);
        }
    }

}


