package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Main {
    private static final Product[] products = {
            new Product("Хлеб", (int) 40),
            new Product("Гречка", 100),
            new Product("Молоко", 70),
            new Product("Яблоко", 20),
            new Product("Тушенка", 150),
            new Product("Сгущенка", 120),
            new Product("Сахар", 60)
    };

    public static void main(String[] args) throws IOException, ParseException {
        Scanner scanner = new Scanner(System.in);
        String s;
        Basket shoppingCart = new Basket(products);
        int selectedItem;
        int itemCount;
        File basketFile = new File("basket.txt");

        if (basketFile.exists()) {
            System.out.println("Загрузить корзину?<ENTER>");

            if (scanner.nextLine().equals("")) {
                shoppingCart = Basket.loadFromTxtFile(basketFile);
            } else {
                shoppingCart = new Basket(products);

            }
        }


        while (true) {
            shoppingCart.printGoodsList(); // Выводим на экран Инфопанель
            s = scanner.nextLine();
            String[] inputValues = s.split(" ");
            if (inputValues.length == 2) {
                try {
                    selectedItem = Integer.parseInt(inputValues[0]);
                    itemCount = Integer.parseInt(inputValues[1]);

                    if (selectedItem <= 0 || selectedItem > products.length) {
                        System.out.println("Введите корректный номер товара из списка");
                        continue;
                    }
                    if (itemCount <= 0) {
                        continue;
                    }
                    shoppingCart.addToCart(selectedItem - 1, itemCount);
                    shoppingCart.saveTxt(basketFile);
                } catch (NumberFormatException nfe) {
                    System.out.println("Введите 2 числа");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (s.equals("end")) {
                break;
            }

        }

        shoppingCart.printCart();


    }
}
