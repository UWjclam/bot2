package com.bots.bot2;

import java.util.ArrayList;

import static com.bots.bot2.GetProducts.*;
import static com.bots.bot2.AddToCart.*;

public class Main {

    public static void main(String[] args) {
        ArrayList<String> keywords = new ArrayList<>();
        //keywords.add("Supreme®/Barbour® Lightweight");
        //keywords.add("Supreme®/Barbour® Lightweight");
        //keywords.add("Leopard");

        // String link1 = "https://www.supremenewyork.com/shop/all";
        String link2 = "https://www.supremenewyork.com/shop/all/jackets";

        //System.out.println(GetProducts(link2, keywords)); // WORKING
        AddCart();
    }
}
