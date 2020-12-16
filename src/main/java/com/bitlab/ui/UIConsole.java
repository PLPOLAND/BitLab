package com.bitlab.ui;

import java.util.Scanner;

/**
 * Klasa do obsługi usera
 */
public class UIConsole {
    private static final Scanner keyboard = new Scanner(System.in);


    public static String read() {

        while (true) {
            System.out.print("\n> ");
            Command command = new Command(keyboard.nextLine());

            if (command.isComplete())
                return command.getCommand();

            else {
                System.out.println( command.getHints() );
            }
        }
    }

    
}
