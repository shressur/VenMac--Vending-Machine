package com.techelevator.view;

public class Effects {

    //type writer effect
    public static void typeWriterEffect(String input, String cursorChar, int delay_ms){
        try{
            for (int i = 0; i < input.length(); i++) {
                Thread.sleep(delay_ms);
                System.out.print(input.charAt(i));
            }
        } catch (InterruptedException ex){
            System.out.println("Application interrupted");
        }
    }

    //spinner
    //String input = "|/-\\";
    public static void spinnerEffect(String input, int repeat, int delay_ms) throws InterruptedException {
        int idx = 0;
        for (int i=0; i < input.length() * repeat; i++) {
            Thread.sleep(delay_ms);
            System.out.print("\b" + input.charAt(idx));
            idx = ++idx % input.length();
        }
        System.out.print("\b");
    }
}

