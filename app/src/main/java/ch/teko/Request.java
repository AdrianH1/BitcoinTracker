package ch.teko;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Request {
    private String addressUrl = "https://api.bitcore.io/api/BTC/mainnet/address/";
    private String transactionUrl = "https://api.bitcore.io/api/BTC/mainnet/tx/";
    
    /**
     * Get all Transactions from API for specified address
     * @param address Single wallet address
     * @return List of Transactions or null if request not successfull
     */
    public List<Address> doAddressCall(String address) {
        try {
            URL url = new URL(addressUrl + address);
            URLConnection request = url.openConnection();
            request.connect();
    
            InputStreamReader reader = new InputStreamReader((InputStream)request.getContent());

            Gson gson = new Gson();
            List<Address> data = Arrays.asList(gson.fromJson(reader, Address[].class));
            
            return data;

        } catch (Exception e) {
            //Repeat if API returns Error 429 "Too Many Requests"
            if (e.getMessage().contains("429")){
                doTransactionDetailCall(address);
            }
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get all details from API for specified transaction ID
     * @param transactionId Single Transaction ID
     * @return Object of Transaction Details or null if request is not successfull
     */
    public TransactionDetail doTransactionDetailCall(String transactionId) {
        try {
            URL url = new URL(transactionUrl + transactionId + "/coins");
            URLConnection request = url.openConnection();
            request.connect();
    
            InputStreamReader reader = new InputStreamReader((InputStream)request.getContent());

            Gson gson = new Gson();
            TransactionDetail data = gson.fromJson(reader, TransactionDetail.class);
            return data;

        } catch (Exception e) {
            //Repeat if API returns Error 429 "Too Many Requests"
            if (e.getMessage().contains("429")){
                doTransactionDetailCall(transactionId);
            }
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Get overview from API for specified transaction ID
     * @param transactionId Single Transaction ID
     * @return Object of Transaction Overviews or null if request is not successfull
     */
    public TransactionOverview doTransactionOverviewCall(String transactionId) {
        try {
            URL url = new URL(transactionUrl + transactionId );
            URLConnection request = url.openConnection();
            request.connect();
    
            InputStreamReader reader = new InputStreamReader((InputStream)request.getContent());

            Gson gson = new Gson();
            TransactionOverview data = gson.fromJson(reader, TransactionOverview.class);
            return data;

        } catch (Exception e) {
            //Repeat if API returns Error 429 "Too Many Requests"
            if (e.getMessage().contains("429")){
                doTransactionDetailCall(transactionId);
            }
            System.out.println(e.getMessage());
        }
        return null;
    }
}
