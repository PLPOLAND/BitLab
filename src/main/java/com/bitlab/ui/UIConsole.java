package com.bitlab.ui;

import java.util.Scanner;

/**
 * Klasa do obsługi usera
 */
public class UIConsole {
    private static final Scanner keyboard = new Scanner(System.in);

    /**
     * read command from user.
     * if command is incomplete or unknown print hints and try again
     * @return complete user's command
     */
    public static String read() {

        while (true) {
            System.out.print("\n> ");
            Command command = new Command(keyboard.nextLine());

            if (command.isComplete()){
                if (command.getParam().isPresent()){
                    if(command.getParam().get().equals("-h") || command.getParam().get().equals("help"))
                        System.out.println(UserCommandMap.getInfo(command.getCommand()));
                    else System.out.println("Unknown parameter");
                }
                else
                    return command.getCommand();
            }

            else {
                System.out.println( command.getHints() );
            }
        }
    }

    
}
