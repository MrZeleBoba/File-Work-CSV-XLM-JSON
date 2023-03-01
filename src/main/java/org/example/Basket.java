package org.example;
import com.google.gson.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Basket {
    private Product[] products;
    private int sumProduct = 0;

    public Basket(Product[] products) {
        this.products = products.clone();
    }

    public void addToCart(int productNum, int amount) {
        products[productNum].changeItemInBasket(amount);
        sumProduct += products[productNum].getPrice() * amount;
    }

    public void printGoodsList() {

        System.out.println("Список товаров для покупки:");

        int currentValue;
        sumProduct = 0;
        for (int i = 0; i < products.length; i++) {
            currentValue = products[i].getInBasket() * products[i].getPrice();
            sumProduct += currentValue;
            System.out.println((i + 1) + "." + products[i].getName() + "-" + products[i].getPrice() + " руб/шт - Кол-во:" + products[i].getInBasket() + " - В сумме:" + currentValue + " руб.");

        }
        System.out.println("ИТОГО Товаров в корзине на " + sumProduct + " рублей");

    }

    public void printCart() {
        System.out.println("Ваша корзина:");

        for (int i = 0; i < products.length; i++) {
            if (products[i].getInBasket() > 0) {
                int currentPrice = products[i].getInBasket() * products[i].getPrice();
                System.out.println("Продукт: " + products[i].getName() + ", Количество: " + products[i].getInBasket() + ", Стоимость: " + products[i].getPrice() + " руб/шт," + "В сумме: " + currentPrice + "руб");

            }

        }
        System.out.println("Итого: " + sumProduct + " руб");

    }

    public void saveTxt(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile);) {
            for (Product good : products) {
                out.println(good.getName() + " " + good.getPrice() + " " + good.getInBasket() + " ");

            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());

        }
    }

    public void saveToJSON(File textFile) throws IOException {
       try(var writer = new FileWriter(textFile);) {
           var gson = new GsonBuilder().setPrettyPrinting().create();
           writer.write(gson.toJson(this, Basket.class));
       }catch (IOException e) {
           System.out.println(Arrays.toString(e.getStackTrace()));
       }

    }

    public static Basket loadFromTxtFile(File textFile) throws IOException {
        Scanner sc = new Scanner(textFile);
        List<Product> products1 = new ArrayList<>();
        String name;
        int price;
        int inBasket;
        while (sc.hasNext()) {
            String[] d = sc.nextLine().split(" ");
            name = d[0];
            price = Integer.parseInt(d[1]);
            inBasket = Integer.parseInt(d[2]);
            products1.add(new Product(name, price, inBasket));
        }
        return new Basket(products1.toArray(Product[]::new));
    }
    public static Basket loadFromJSON(File textFile) throws FileNotFoundException {
        var gson = new Gson();
        var reader = new FileReader(textFile);
        return gson.fromJson(reader, Basket.class);
    }
}

