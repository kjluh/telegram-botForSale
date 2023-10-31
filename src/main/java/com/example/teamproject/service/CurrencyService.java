package com.example.teamproject.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.util.Optional;


/**
 * Сервис для предоставления курса валют
 */
@Service
public class CurrencyService {

    @Autowired
    private OkHttpClient client;

    @Value("${cbr.currency.rates.xml.url}")
    private String cbrCurrencyRatesXmlUrl;

    private Optional<String> getCurrencyRatesXML() throws Exception {
        Request request = new Request.Builder().url(cbrCurrencyRatesXmlUrl).build();

        try (Response response = client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? Optional.empty() : Optional.of(body.string());
        } catch (RuntimeException e) {
            throw new Exception("Ошибка получения курсов валют от ЦБ РФ", e);
        }
    }


    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";



    public String getUSDExchangeRate() throws Exception {
        Optional<String> xmlOptional = getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new Exception("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, USD_XPATH);
    }

    public String getEURExchangeRate() throws  Exception {
        Optional<String> xmlOptional = getCurrencyRatesXML();
        String xml = xmlOptional.orElseThrow(
                () -> new Exception("Не удалось получить XML")
        );
        return extractCurrencyValueFromXML(xml, EUR_XPATH);
    }

    private static String extractCurrencyValueFromXML(String xml, String xpathExpression)
            throws Exception {
        InputSource source = new InputSource(new StringReader(xml));
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            Document document = (Document) xpath.evaluate("/", source, XPathConstants.NODE);

            return xpath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new Exception( "Не удалось распарсить XML", e);
        }
    }
}
