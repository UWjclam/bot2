package com.bots.bot2;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.json.Json;

import javax.sound.midi.SysexMessage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    private static HttpURLConnection connection;

    // Method 1 will block UI thread
    private static void Method1() {
        BufferedReader reader;
        String line;    // to read every line
        StringBuffer responseContent = new StringBuffer();    // append each line and build response

        try {
            //WebDriver chrome = Setup.setupChrome();
            URL url = new URL("https://www.supremenewyork.com/shop/all");
            connection = (HttpURLConnection) url.openConnection();

            // Request setup.
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int status = connection.getResponseCode();
            System.out.println(status); // 200 = connection successful

            // Get response from endpoint.
            // Response is input stream.

            // Connection has a problem.
            if (status > 299) {
                // reader reads error message
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                while ((line = reader.readLine()) != null) // there exist more stuff to read
                    responseContent.append(line);

                reader.close();
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) // there exist more stuff to read
                    responseContent.append(line);

                reader.close();
            }
            System.out.println(responseContent.toString()); // convert string buffer to string.

            parse(responseContent.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // Need to close connection.
            connection.disconnect();
        }
    }

    private static void Method4(String link) {
        // Method 2 handles asynchronous threading
        // Uses java.net.http.HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).build();

        // Send request asynchronously.
        // 2nd param means want server to send post body as string
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // once sendAsync is done (receive response), apply method to previous result.
                // :: means lambda expression (want to use body method from HttpResponse class and use in prev result).
                .thenApply(HttpResponse::body)

                //.thenAccept(System.out::println)
                .thenApply(Main::parse4)

                // prints completable future to current state
                // else if missing, does not print anything
                .join();
    }

    private static void Method5(String link) {
        // Method 2 handles asynchronous threading
        // Uses java.net.http.HttpClient
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).build();

        // Send request asynchronously.
        // 2nd param means want server to send post body as string
        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                // once sendAsync is done (receive response), apply method to previous result.
                // :: means lambda expression (want to use body method from HttpResponse class and use in prev result).
                .thenApply(HttpResponse::body)

                //.thenAccept(System.out::println)
                .thenApply(Main::parse5)

                // prints completable future to current state
                // else if missing, does not print anything
                .join();
    }

    private static String parse(String responseBody) {
        // Method to parse JSON data
        JSONArray items = new JSONArray(responseBody);
        for (int i = 0; i < items.length(); ++i) {
            JSONObject item = items.getJSONObject(i);
            int id = item.getInt("id");
            System.out.println("ID: " + id);
        }
        return null;
    }

    private static String parse2() throws Exception {
        /*
        if html has:  <td class="topic starter"><a href="http://www.test.com">Title</a></td>
        use:
        // select the a element and then extract its href
        Element link = doc.select("td.topic.starter > a");
        String url = link.attr("href");
         */
        System.out.println("OK");
        File input = new File("C:\\Users\\Jason\\Desktop\\Java\\mytxt.txt");
        Document doc = Jsoup.parse(input, "UTF-8", "https://www.supremenewyork.com/shop/all");

        /*
        Elements links = doc.select("a[href]"); // a with href
        Elements pngs = doc.select("img[src$=.png]"); // img with src ending .png
        Element masthead = doc.select("div.masthead").first();
        String linkhref = links.attr("href");
        System.out.println(linkhref);
        return linkhref;
         */

        Elements links = doc.select("div.inner-article > a");
        System.out.println(links);
        String url = links.attr("href");
        System.out.println(url);
        return url;
    }

    private static String parse3(String responseBody) {
        //String html = "https://www.supremenewyork.com/shop/all";
        String html = responseBody;
        String regex = "<a href\\s?=\\s?\"([^\"]+)\">";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String wholething = matcher.group(); // includes "<a href" and ">"
            String link = matcher.group(1); // just the link
            // do something with wholething or link.
            System.out.println(link);
            index = matcher.end();
        }
        return null;
    }

    private static String parse4(String responseBody) {
        //String html = "https://www.supremenewyork.com/shop/all";
        String html = responseBody;
        // String regex = "<a href\\s?=\\s?\"([^\"]+)\">";
        String regex = "<a\\s[^>]*?href\\s?=\\s?\"([^\"]+)\">"; // room for extra attributes
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String wholething = matcher.group(); // includes "<a href" and ">"
            String link = matcher.group(1); // just the link
            // do something with wholething or link.
            //System.out.println(link);
            //https://www.supremenewyork.com/shop/jackets/g84fwstrv/a9och5sqd
            //System.out.println("https://www.supremenewyork.com" + link);
            System.out.println(wholething);
            index = matcher.end();
        }
        return null;
    }

    private static ArrayList<String> results = new ArrayList<>();

    private static String parse5(String responseBody) {
        results.clear();

        String html = responseBody;
        // String regex = "<div class=\"[^\"]*?paddingT05[^\"]*?\">(.*?)<\\/div>";
        //String regex = "<div[^>]*class=\"[^\"]*product-name[^\"]*\".*>(.*)</div>\n";
        //String regex = "\\s[^>]*?\"([^\"]+)\">";
        //String regex = "<div\\s[^>]*?class\\s?=\"product-name\">\\s[^>]*?([^\"]+)<";
        String regex = "<div\\s[^>]*?class\\s?=\"product-name\">" +
                "<a\\s[^>]*?class\\s?=\"name-link\"\\s[^>]*?>" +
                "([^\"]+)" +
                "</a></div>";
        //String regexA = regexDIV + "<a\\s[^>]*?class\\s?=\"name-link\">";
        //String regex = regexA + "([^\"]+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String wholething = matcher.group();
            String link = matcher.group(1);
            //System.out.println(wholething);
            //System.out.println(link);
            results.add(wholething);
            index = matcher.end();
        }
        return null;
    }


    // FINAL
    private static ArrayList<String> productNames = new ArrayList<>();
    private static ArrayList<String> productStyles = new ArrayList<>();

    private static void Method(String link) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(link)).build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parseProductName)
                .join();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(Main::parseProductStyle)
                .join();
    }

    private static String parseProductName(String responseBody) {
        productNames.clear();

        String html = responseBody;
        String regex = "<div\\s[^>]*?class\\s?=\"product-name\">" +
                "<a\\s[^>]*?class\\s?=\"name-link\"\\s[^>]*?>" +
                "([^\"]+)" +
                "</a></div>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String wholething = matcher.group();
            productNames.add(wholething);
            index = matcher.end();
        }
        return null;
    }

    private static String parseProductStyle(String responseBody) {
        productStyles.clear();

        String html = responseBody;
        String regex = "<div\\s[^>]*?class\\s?=\"product-style\">" +
                "<a\\s[^>]*?class\\s?=\"name-link\"\\s[^>]*?>" +
                "([^\"]+)" +
                "</a></div>";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(html);
        int index = 0;
        while (matcher.find(index)) {
            String wholething = matcher.group();
            productStyles.add(wholething);
            index = matcher.end();
        }
        return null;
    }

    private static String returnLink(String name1, String name2, String style) {
        String result = null;

        for (String s1 : productNames) {
            if (s1.contains(name1) && s1.contains(name2)) {
                for (String s2 : productStyles) {
                    if (s2.contains(style)) {
                        String regex = "<a\\s[^>]*?href\\s?=\\s?\"([^\"]+)\">";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(s2);
                        int index = 0;
                        while (matcher.find(index)) {
                            String wholething = matcher.group();
                            String link = matcher.group(1);
                            System.out.println(link);
                            result = "https://www.supremenewyork.com" + link;
                            System.out.println(result);
                            index = matcher.end();
                        }
                    }
                }
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        String link1 = "https://www.supremenewyork.com/shop/all";
        String link2 = "https://www.supremenewyork.com/shop/all/jackets";

        String matchName1 = "Supreme®/Barbour® Lightweight";
        String matchName2 = "Supreme®/Barbour® Lightweight";
        String matchStyle = "Leopard";

        Method(link2);

        System.out.println("NAMES");
        System.out.println();
        System.out.println(productNames);
        System.out.println();
        System.out.println();
        System.out.println("STYLES");
        System.out.println();
        System.out.println(productStyles);
        System.out.println();

        System.out.println();System.out.println();

        System.out.println(returnLink(matchName1, matchName2, matchStyle));

    }
}
