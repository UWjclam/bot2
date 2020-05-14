package com.bots.bot2;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static java.lang.Thread.sleep;

public class Setup {

    public static WebDriver setupChrome() throws Exception {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\Jason\\Desktop\\Java\\Selenium\\chromedriver.exe");

        WebDriver chrome = new ChromeDriver();
        chrome.get("https://www.supremenewyork.com/shop/all");

        return chrome;
    }
}
