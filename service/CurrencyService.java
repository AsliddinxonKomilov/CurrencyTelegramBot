package currencyBot.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import currencyBot.model.CurrencyResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class CurrencyService {

    public HttpResponse<String> getFromAPI(String currency) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(URI
                        .create("https://cbu.uz/uz/arkhiv-kursov-valyut/json/."))
                .build();
        return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    public CurrencyResponse getParsedCurrency(String currency) throws IOException, InterruptedException {
        String jsonData = getFromAPI(currency).body();

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        CurrencyResponse[] responses = gson.fromJson(jsonData,CurrencyResponse[].class);
        for (CurrencyResponse response : responses) {
            if (response.code.equals(currency)){
                return response;
            }
        }
        return null;
    }
    public String calculateAmount(String rate, String amount){
        try {
            double currentRate = Double.parseDouble(rate);
            double userAmount = Double.parseDouble(amount);

            double result = currentRate * userAmount;

            return String.format("%, .2f so'm", result);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return "Xato,Iltimos, faqat son kiriting!";
    }
}
