package com.bots.bot2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetProducts {

    private static ArrayList<String> products = new ArrayList<>();


    private static String getProductsInfo(String htmlResponseBody) {
        String regex = "<div class=\"product-name\">(.*?)</div><div class=\"product-style\">(.*?)</div>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlResponseBody);

        int index = 0;
        while (matcher.find(index)) {
            products.add(matcher.group());
            index = matcher.end();
        }

        return null;
    }

    private static String getProductURL(String productInfo, ArrayList<String> keywords) {
        for (String keyword : keywords) {
            if (!productInfo.contains(keyword))
                return null;
        }

        String regex = "href=\"([^\"]+)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(productInfo);
        matcher.find();

        return "https://www.supremenewyork.com" + matcher.group(1);
    }

    private static ArrayList<String> getProductsURL(ArrayList<String> keywords) {
        ArrayList<String> urls = new ArrayList<>();

        for (String productInfo : products) {
            String productURL = getProductURL(productInfo, keywords);

            if (productURL != null)
                urls.add(productURL);
        }

        return urls;
    }

    public static ArrayList<String> GetProducts(String link, ArrayList<String> keywords) {
        products.clear();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(GetProducts::getProductsInfo)
                .join();

        return getProductsURL(keywords);
    }
}
