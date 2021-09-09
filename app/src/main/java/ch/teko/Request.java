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
    
    public void doAddressCall(String address) {
        try {
            URL url = new URL(addressUrl + address);
            URLConnection request = url.openConnection();
            request.connect();
    
            //InputStream input = url.openStream();
            InputStreamReader reader = new InputStreamReader((InputStream)request.getContent());

            Gson gson = new Gson();
            List<Address> data = Arrays.asList(gson.fromJson(reader, Address[].class));

            int value = 5;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
