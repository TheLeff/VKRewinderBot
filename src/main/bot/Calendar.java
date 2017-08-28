package main.bot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Calendar {
    SimpleDateFormat sdf = new SimpleDateFormat("hh-mm");
    Date time;
    int userid;
    String note;

    public Calendar(Date time, int userid, String note) {
        this.time = time;
        this.userid = userid;
        this.note = note;
    }

    public Date getTime() {
        return time;
    }

    public int getUserID() {
        return userid;
    }

    public String getNote() {
        return note;
    }


}
