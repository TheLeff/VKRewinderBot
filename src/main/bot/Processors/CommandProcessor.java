package main.bot.Processors;

import main.Exceptions.HardResetException;
import main.bot.ChatBot;
import main.bot.Misc;
import main.bot.Solution;

import java.util.Date;

public class CommandProcessor extends Processor {


    public String command(int userId, String message) throws HardResetException {

        if (!History.isEmpty())
            History.add(System.lineSeparator() + Misc.getUserName(userId) + " [" + message + ']');
        else
            History.add((Misc.getUserName(userId) + " [" + message).substring(1) + ']');
        return (function(message.replaceAll("[-+.^:,!]", "")));
    }


    private String function(String message) throws HardResetException {

        switch (message) {
            case "uptime":
                return calculateUptime();
            case "time":
                return new Date().toString();
            case "hardresetplox":
                throw new HardResetException("BOT TURNED OFF");
            case "history":
                return getHistory();
        }
        return "WRONG COMMAND";
    }

    @Override
    String getHistory() {

        return Processor.History.toString().substring(0, Processor.History.toString().length() - 1);
    }

    private static String calculateUptime() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - Solution.startTime;
        return totalTime / 1000 + " seconds, from: " + ChatBot.startDate;
    }

    @Override
    boolean checkAvail() {
        return true;
    }

}
