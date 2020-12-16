package com.bitlab.ui;

import com.bitlab.constant.CommandMap;

import java.util.*;
import java.util.stream.Collectors;

public class Command {
    private String raw;
    private Map<Index, String> map;

    public List<String> getHints() {
        var hints = Arrays.stream(CommandMap.values())
                .filter( c -> c.toString().toLowerCase().startsWith( this.getCommand().toLowerCase() ))
                .collect(Collectors.toList());

        if (hints.size() == 0) {
            System.out.println("Unknown command");
            System.out.println("Found by first letter");
            hints = Arrays.stream(CommandMap.values())
                    .filter( c -> c.toString().toLowerCase().startsWith( this.getCommand().substring(0,1)))
                    .collect(Collectors.toList());
        }
        return hints.stream()
                .map( CommandMap::toString )
                .collect(Collectors.toList());
    }

//    public void launchWithParam() {
//        var param = this.getParam().orElseThrow();
//        switch (param) {
//            case "-h" : CommandMap.info(this.getCommand());
//                break;
//            default:
//                System.out.println("Unknown parameter");
//                break;
//        }
//    }

    private enum Index {
        COMMAND, PARAM;
    }

    public Command(String command) {
        this.init(command);
    }
    private void init(String command) {
        if (command == null || command.equals("")) throw new IllegalArgumentException("Empty command");
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

    public Optional<String> getParam() {
        return this.get( Index.PARAM );
    }

    public boolean isComplete() {
        return Arrays.stream(CommandMap.values())
                .anyMatch( c -> c.toString().toLowerCase().equals( this.getCommand()));
    }
}
