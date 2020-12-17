package com.bitlab.ui;

import java.util.*;
import java.util.stream.Collectors;

/**
 * class represents a command that the user wrote. It can be further expanded to support commands with parameters or additional options.
 * @author Jacek Giedronowicz
 */
public class Command {
    private String raw;
    private Map<Index, String> map;

    /**
     *
     * @return list of hint based on the user's incomplete command
     */
    public List<String> getHints() {
        var hints = Arrays.stream(UserCommandMap.values())
                .filter( c -> c.toString().toLowerCase().startsWith( this.getCommand().toLowerCase() ))
                .collect(Collectors.toList());

        if (hints.size() == 0) {
            System.out.println("Unknown command");
            System.out.println("Found by first letter");
            hints = Arrays.stream(UserCommandMap.values())
                    .filter( c -> c.toString().startsWith( this.getCommand().substring(0,1)))
                    .collect(Collectors.toList());
        }
        return hints.stream()
                .map( UserCommandMap::toString )
                .collect(Collectors.toList());
    }

//    public void launchWithParam() {
//        var param = this.getParam().orElseThrow();
//        switch (param) {
//            case "-h" : UserCommandMap.info(this.getCommand());
//                break;
//            default:
//                System.out.println("Unknown parameter");
//                break;
//        }
//    }

    private enum Index {
        COMMAND, PARAM
    }

    public Command(String command) {
        this.init(command);
    }

    /**
     * init method usage in constructor.
     * Method split raw command by spaces.
     * First part is command, second is parameter
     * @param command - raw user's command
     */
    private void init(String command) {
        if (command == null) throw new IllegalArgumentException("Empty command");
        this.raw = command;

        String[] data = this.raw.split(" ");
        this.map = new HashMap<>();
        for ( int i = 0; i < data.length; i++) {
            map.put( Index.values()[i], data[i] );
        }
    }

    private Optional<String> get(Index index) {
        return this.map.get(index) != null ?
                Optional.of(this.map.get(index)) :
                Optional.empty();
    }

    public String getRaw() {
        return this.raw;
    }

    public String getCommand() {
        return this.map.get( Index.COMMAND ).toLowerCase();
    }

    /**
     *
     * @return Optional String becauseer may not have entered the parameter
     */
    public Optional<String> getParam() {
        return this.get( Index.PARAM );
    }

    /**
     *
     * @return true if user command exist in UserCommandMap
     */
    public boolean isComplete() {
        return Arrays.stream(UserCommandMap.values())
                .anyMatch( c -> c.toString().toLowerCase().equals( this.getCommand()));
    }
}
