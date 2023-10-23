package com.tuempresa;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/tu_base_de_datos";
        String user = "daniel";
        String password = "1234";

        try {
            Connection connection = DriverManager.getConnection(jdbcUrl, user, password);
            String url = "https://www.ktronix.com/computadores-tablet/computadoresportatiles/c/BI_104_KTRON";
            Document document = Jsoup.connect(url).get();

            Elements productElements = document.select(".ais-InfiniteHits-item product__item js-product-item js-algolia-product-click");

            for (Element productElement : productElements) {
                String productName = productElement.select(".product__item__top__title").text();
                String productPrice = productElement.select(".price").text();
                String productCode = productElement.select(".review").text();

                // Almacena los datos en la base de datos
                String sql = "INSERT INTO productos (nombre, precio, codigo) VALUES (?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, productName);
                statement.setString(2, productPrice);
                statement.setString(3, productCode);
                statement.executeUpdate();
            }

            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
