package org.example;
import com.google.gson.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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
    private static boolean basketLoadEnable = false;
    private static String basketLoadFileName = "";
    private static FileFormat basketLoadFormat = FileFormat.JSON;

    private static boolean basketSaveEnable = false;
    private static String basketSaveFileName = "";
    private static FileFormat basketSaveFormat = FileFormat.JSON;

    private static boolean logSaveEnable = false;
    private static String logFileName = "";

    public static void main(String[] args) throws IOException, ParseException,ParserConfigurationException, SAXException {
        Scanner scanner = new Scanner(System.in);
        String s;
        Basket shoppingCart = new Basket(products);
        int selectedItem;
        int itemCount;
        ClientLog clientLog = new ClientLog();

        loadSettings();
        System.out.println(" ");

        var basketFileForLoad = new File(basketLoadFileName);
        var basketFileForSave = new File(basketSaveFileName);
        var logFile = new File(logFileName);




        if (basketFileForLoad.exists() && basketLoadEnable) {
            if (basketLoadFormat == FileFormat.JSON) {
                shoppingCart = Basket.loadFromJSON(basketFileForLoad);
            }
            if (basketLoadFormat == FileFormat.TXT) {
                shoppingCart = Basket.loadFromTxtFile(basketFileForLoad);
            }
        }else {
            shoppingCart = new Basket(products);
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
                    if (basketSaveEnable) {
                        if (basketLoadFormat == FileFormat.JSON) {
                            shoppingCart.saveToJSON(basketFileForSave);
                        }
                    }
                    clientLog.log(selectedItem, itemCount);
                } catch (NumberFormatException nfe) {
                    System.out.println("Введите 2 числа");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else if (s.equals("end")) {
                break;
            }

        }
        if (logSaveEnable) {
            clientLog.exportAsCSV(logFile);
        }

        shoppingCart.printCart();


    }
    static void loadSettings() throws ParserConfigurationException, IOException, SAXException {
        // Создается построитель документа
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        // Создается дерево DOM документа из файла
        Document document = documentBuilder.parse("shop.xml");
        Node root = document.getDocumentElement();

        String sectionName;
        String parameterName;
        String parameterValue;
        // Просматриваем все подэлементы корневого - т.е. книги
        NodeList config = root.getChildNodes();
        for (int i = 0; i < config.getLength(); i++) {
            Node section = config.item(i);
            // Если нода не текст, то это подсекция - заходим внутрь
            if (section.getNodeType() != Node.TEXT_NODE) {  //load, save, log
                sectionName = section.getNodeName();
                NodeList options = section.getChildNodes();
                for (int k = 0; k < options.getLength(); k++) {
                    Node parameter = options.item(k);
                    if (parameter.getNodeType() != Node.TEXT_NODE) {
                        parameterName = parameter.getNodeName();
                        parameterValue = parameter.getFirstChild().getTextContent();
                        setOption(sectionName, parameterName, parameterValue);
                    }
                }

            }
        }
    }

    private static void setOption(String sectionName, String parameterName, String parameterValue) {
//        System.out.println(sectionName+": "+parameterName+": "+parameterValue);
        if (sectionName.equals("load")) {
            if (parameterName.equals("enabled")) {
                basketLoadEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketLoadFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                if (parameterValue.equals("json")) {
                    basketLoadFormat = FileFormat.JSON;
                } else {
                    basketLoadFormat = FileFormat.TXT;
                }
            }
        }
        if (sectionName.equals("save")) {
            if (parameterName.equals("enabled")) {
                basketSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                basketSaveFileName = parameterValue;
            }
            if (parameterName.equals("format")) {
                if (parameterValue.equals("json")) {
                    basketSaveFormat = FileFormat.JSON;
                } else {
                    basketSaveFormat = FileFormat.TXT;
                }
            }

        }
        if (sectionName.equals("log")) {
            if (parameterName.equals("enabled")) {
                logSaveEnable = parameterValue.equals("true");
            }
            if (parameterName.equals("fileName")) {
                logFileName = parameterValue;
            }
        }
    }
}

