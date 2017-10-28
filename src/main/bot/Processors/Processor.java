package main.bot.Processors;

import java.util.ArrayList;

abstract class Processor {

    static ArrayList<String> History = new ArrayList<>();

    abstract String getHistory();

    boolean checkAvail() {

        return false;
    }

}
