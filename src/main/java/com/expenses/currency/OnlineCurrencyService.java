package com.expenses.currency;

import org.json.JSONArray;
import org.json.JSONObject;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class OnlineCurrencyService implements CurrencyService {

    public static void main(String[] args) {
        OnlineCurrencyService currencyService = new OnlineCurrencyService();
        BigDecimal value = currencyService.convertToPln(BigDecimal.TEN, Currency.US_DOLLAR);
        System.out.println(value);
    }

    @Override
    public BigDecimal convertToPln(BigDecimal amount, Currency currency) {
        String currencyCode = currency.getCurrencyCode();
        String url = "http://api.nbp.pl/api/exchangerates/rates/A/"+currencyCode+"/2021-06-24?format=json";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest httpRequest = HttpRequest.newBuilder(URI.create(url)).build();

        HttpResponse<String> response = null; // response - cały obiekt odpowiedzi przekazanych przez server
        try {
            response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception exception){
            throw new RuntimeException(exception); // zrobione na szybko z powodu braku czasu (try-catch), powinien być wyspecjalizowany wyjątek
        }

        System.out.println(response.body());

        //wyciągnięcie wartości z api NBP za pośrednictwem objektów JSON

        //1. Zamień ciało odpowiedzi w Stringu na obiekt JSONObject
        JSONObject responseObject = new JSONObject(response.body());

        //2. Z obiektu odpoiewdzi pobierz pole "rates", ktróre jest tablicą obiektów
        JSONArray ratesArray = responseObject.getJSONArray("rates");

        //3. Z tablicy obiektór weź pierwszy obiekt (indeks = 0)
        JSONObject object = ratesArray.getJSONObject(0);

        //4. Z tego obiektu pobierz pole "mid", które jest liczbą BigDecimal
        BigDecimal rate = object.getBigDecimal("mid");

        System.out.println(rate);
        return amount.multiply(rate);
    }
}
