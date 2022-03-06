package com.techelevator.view;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Logger {
    static BufferedReader br;
    static BufferedWriter bw;

    //log activity
    public static void logActivity( String logFilePath, String message){
        String logMessage = formatLog(message);

        try {
            bw = new BufferedWriter(new FileWriter(logFilePath, true));
            bw.append(logMessage).append("\n");
            bw.flush();
        } catch(IOException ex){
            System.out.println("Error: Logging failed. Please, try again.");
        }
    }

    //format log: add date
    private static String formatLog(String message){
        Date now = new Date();
        return String.valueOf(now) + ": " + message;
    }

    //log sales: sales report
    public static void logSale(String filePath, String itemName, double itemCost){
        String lastLine = "";
        StringBuilder data = new StringBuilder();

        try{
            br = new BufferedReader(new FileReader(filePath));
            String line =  br.readLine();
            while (line != null) {
                String[] parts = line.split("\\|");

                if (parts[0].equals(itemName)) {
                    data.append(parts[0]).append("|").append(Integer.parseInt(parts[1]) + 1).append("\n");
                } else if (parts[0].equalsIgnoreCase("TOTAL SALE")) {
                    lastLine = parts[0] + "|" + String.format("%.2f", Double.parseDouble(parts[1]) + itemCost);
                } else if (line.equals("")){
                    data.append("\n");
                } else {
                    data.append(parts[0]).append("|").append(parts[1]).append("\n");
                }
                line = br.readLine();
            }
            data.append(lastLine);

            bw = new BufferedWriter(new FileWriter(filePath));
            bw.write(data.toString());
            bw.flush();
            bw.close();
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

}
