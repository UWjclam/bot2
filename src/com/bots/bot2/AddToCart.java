package com.bots.bot2;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddToCart {

    private static ArrayList<String> products = new ArrayList<>();


    private static boolean soldOut(String productURL) {
        String regex = "class=\"button sold-out\"";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(productURL);
        matcher.find();

        if (matcher.group().isEmpty())
            return false;

        return true;
    }

    public static void AddCart() {
        String postURL = "https://www.supremenewyork.com/shop/173006/add";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(postURL)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .join();

        System.out.println(client.toString());
    }
}
