package com.example.teamproject.service;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Optional;

@Service
public class WeatherService {

    @Autowired
    private OkHttpClient client;

    private String sStart = "http://api.openweathermap.org/data/2.5/weather?id=";
    private String sClose = "&mode=xml&lang=ru&units=metric&APPID=2142222fa06eccafd0aae92641aaa834";

  //  https://api.openweathermap.org/data/2.5/weather?id=511565&mode=xml&APPID=2142222fa06eccafd0aae92641aaa834
    //http://api.openweathermap.org/data/2.5/weather?id=511565&mode=xml&lang=ru&units=metric&APPID=2142222fa06eccafd0aae92641aaa834


    private String address(String city) {
        return sStart + city + sClose;
    }

    private Optional<String> getWeatherFromCity(String s) throws Exception {
        Request request = new Request.Builder().url(s).build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (RuntimeException e) {
            throw new Exception("Ошибка получения погоды", e);
        }
    }
    public String getWeather(String s) throws Exception {
        Optional<String> xmlOptional = getWeatherFromCity(address(city(s)));
        String xml = xmlOptional.orElseThrow(
                () -> new Exception("Не удалось получить XML")
        );
        return "температура " + extractCurrencyValueFromXML(xml, "/current/temperature/@value")
                + "C \nскорость ветра " + extractCurrencyValueFromXML(xml, "/current/wind/speed/@value") + " м/с";
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws Exception {
        InputSource source = new InputSource(new StringReader(xml));
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new Exception("Не удалось распарсить XML", e);
        }
    }
    private String city(String s) {
        String city = "";
        switch (s) {
            case "Пенза" -> city = "511565";
            case "Москва" -> city = "524901";
            case "Воронеж" -> city = "472045";
            case "Саратов" -> city = "498677";
            case "Самара" -> city = "499099";
            case "Рязань" -> city = "500096";
        }
        return city;
    }
}
