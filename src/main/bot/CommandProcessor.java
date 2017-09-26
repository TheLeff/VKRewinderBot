package main.bot;

import main.Exceptions.HardResetException;

import java.util.Date;

class CommandProcessor extends Processor {

    static String command(String message) throws HardResetException {
        History.add(message);
        return (function(message.replaceAll("[-+.^:,!]", "")));
    }

    private static String function(String message) throws HardResetException {

        switch (message) {
            case "uptime":
                return calculateUptime();
            case "time":
                return new Date().toString();
            case "hardresetplox":
                throw new HardResetException("BOT TURNED OFF");
            case "history":
                return fullHistory();

        }
        return "WRONG COMMAND";
    }

    private static String fullHistory() {

        return Processor.History.toString();
    }

    private static String calculateUptime() {
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - Solution.startTime;
        return totalTime / 1000 + " seconds, from: " + ChatBot.startDate;
    }

}
