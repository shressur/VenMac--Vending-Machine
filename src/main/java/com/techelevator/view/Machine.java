package com.techelevator.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.*;

//has balance
//allows feed money
//can display items
//allows purchase
public class Machine {
    Scanner sc = new Scanner(System.in);
    double balance;

    //getter
    public double getBalance() { return balance; }

    //methods
    //add to balance
    private void addToBalance(double moneyToAdd){
        //add to existing balance
        balance += moneyToAdd;
        balance = roundDouble(balance);
    }

    //display inventory
    public void displayItems(Map<String, List<String>> map){

        System.out.println("+" + "-".repeat(56) + "+");
        System.out.printf("| %s | %-20s | %s | %-7s | %s |\n", "ID", "ITEM NAME", "PRICE ($)", "CATEGORY", "QTY");
        System.out.println("+" + "-".repeat(56) + "+");

        for(Map.Entry<String, List<String>> el: map.entrySet()){
            //System.out.println(el);
            List<String> itemDetails = el.getValue();
            System.out.printf("| %s | %-20s | %9s | %-8s | %3s |\n",
                    el.getKey(), itemDetails.get(0), itemDetails.get(1), itemDetails.get(2), itemDetails.get(3));
        }
        System.out.println("+" + "-".repeat(56) + "+");

    }

    //feed money
    public void feedMoney(){
        System.out.print("Enter bill ($1, $2, $5, or $10): ");
        String input = sc.nextLine();
        try {
            int isValidMoney = Integer.parseInt(input);
            if(isValidMoney == 1 || isValidMoney == 2 || isValidMoney == 5 || isValidMoney == 10){
                addToBalance(isValidMoney);

                Logger.logActivity("Log.txt", "FEED MONEY, $" + input);

                System.out.println("Added to balance: $" + input);
            } else {
                System.out.println(input + " is not a valid bill. Tray again.");
            }
        } catch (Exception e){
            System.out.println(input + " is not accepted. Try again.");
        }
    }

    //populate inventory
    public Map<String, List<String>> listInventories(String filePath){
        Map<String, List<String>> temp = new TreeMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line = br.readLine();
            while (line != null){
                String[] lineParts = line.split("\\|");

                temp.put(lineParts[0], Arrays.asList(lineParts[1], lineParts[2], lineParts[3], "5"));    //id, name, price, category

                line = br.readLine();
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        return temp;
    }

    //purchase selected item
    public void purchaseSelected(Map<String, List<String>> inventories){
        System.out.print("Enter SlotId to purchase an item (Example: C2): ");
        String slotId = sc.nextLine().toUpperCase(Locale.ROOT).trim();

        if(inventories.containsKey(slotId)){
            List<String> temp = inventories.get(slotId);
//            int qty = Integer.parseInt(inventories.get(slotId).get(3));
//            double price = Double.parseDouble(inventories.get(slotId).get(1));
//            String item = (inventories.get(slotId)).get(0);
//            String category = (inventories.get(slotId)).get(2);
            int qty = Integer.parseInt(temp.get(3));
            double price = Double.parseDouble(temp.get(1));
            String item = temp.get(0);
            String category = temp.get(2);

            if(qty > 0){
                if (balance >= price){
                    qty -= 1;
                    temp.set(3, String.valueOf(qty));
                    inventories.put(slotId, temp);

                    balance -= price;
                    balance = roundDouble(balance);

                    System.out.println();
                    System.out.println("Purchased item: " + item);

                    Logger.logActivity("Log.txt", "PURCHASE ITEM, " + slotId + ", " + item + ", $" + price);
                    Logger.logSale("Sales.log", item, price);

                    makeSound(category);
                    System.out.printf("Current balance: %.2f", this.getBalance());
                    System.out.println();

                } else {
                    System.out.println("Available balance: $" + this.balance);
                    System.out.println("Insufficient fund. Purchase failed.");
                }
            } else {
                System.out.println("Sold out.");
            }
        }
        if(!inventories.containsKey(slotId)){
            System.out.println("Item not found. Try again.");
        }
    }

    //finish transaction
    public void finishTransaction(){
        Logger.logActivity("Log.txt", "GIVE CHANGE, $" + this.balance);

        returnChange();
        //balance = 0; -----------------------> this introuduces a bug: scanner does not end and does not dispense the change
        //System.out.println("reached");

        System.out.println("--------- MAIN MENU ---------");

        //System.out.println("--------- Thank You for using VenMac2.0 ---------");
        //System.exit(0);
    }

    //return change
    public void returnChange(){
        final double QUARTER_VALUE = 0.25;
        final double DIME_VALUE = 0.10;
        final double NICKLE_VALUE = 0.05;
        //final double CENT_VALUE = 0.01;

        int quarters = 0;
        int dimes = 0;
        int nickles = 0;
        int cents = 0;
        double rawBalance;
        while(balance > 0){
            if(balance >= QUARTER_VALUE){
                quarters++;
                rawBalance = balance -  QUARTER_VALUE;   //sometimes this might result in 0.2789987698
                balance = roundDouble(rawBalance);
            } else if (balance >= DIME_VALUE){
                dimes++;
                rawBalance = balance -  DIME_VALUE;
                balance = roundDouble(rawBalance);
            } else if(balance >= NICKLE_VALUE){
                nickles++;
                rawBalance = balance - NICKLE_VALUE;
                balance = roundDouble(rawBalance);
            }
            else {
                String centsString = String.valueOf(balance);
                cents = Integer.parseInt(centsString.split("\\.")[1]);  //0.04 ->4
                balance = 0;    //without this loop will go into infinite loop
                                //not substracting from "balance" because there is no need
            }

        }
        balance = 0;  //-----------------------------------> this fixes the above bug
        System.out.printf("Return: %d Quarter(s), %d Dime(s), %d Nickel(s), %s Cent(s).\n\n", quarters, dimes, nickles, cents);

    }

    //round to two decimal place
    private double roundDouble(double money){
        DecimalFormat df = new DecimalFormat("##.##");
        return Double.parseDouble(df.format(money));
    }

    //make sound
    private void makeSound(String itemcategory){
        switch (itemcategory){
            case "Chip":
                System.out.println("Crunch Crunch, Yum!");
                break;
            case "Drink":
                System.out.println("Glug Glug, Yum!");
                break;
            case "Candy":
                System.out.println("Munch Munch, Yum!");
                break;
            case "Gum":
                System.out.println("Chew Chew, Yum!");
                break;
        }
    }

    //good buy message
    public static void greetUser(){
        String greetMessage = "";
        String userName = System.getProperty("user.name").toUpperCase();

        LocalDateTime thisInstance = LocalDateTime.now();
        if(thisInstance.getHour() > 0 && thisInstance.getHour() < 12){
            greetMessage = "Good morning " + userName;
        } else if(thisInstance.getHour()>= 12 && thisInstance.getHour() < 18){
            greetMessage = "Good afternoon " + userName;
        } else {
            greetMessage = "Good evening " + userName;
        }
        System.out.println(greetMessage);
    }

}
