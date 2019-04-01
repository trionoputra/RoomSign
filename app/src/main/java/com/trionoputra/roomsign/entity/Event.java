package com.trionoputra.roomsign.entity;

import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Bakwan on 09/05/2018.
 */

public class Event extends SugarRecord<Event> {
    private String title;
    private String timeFrom;
    private String timeTo;
    private String pic;
    private String email;
    private String phone;
    private String division;
    private boolean ongoing;
    private boolean selected;
    private int amount;
    private String start;
    private String end;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public boolean isOngoing() {
        return ongoing;
    }
    public void setOngoing(boolean ongoing) {
        this.ongoing = ongoing;
    }
    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {

        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateObj = null;
        try {
            dateObj = formatter1.parse(start);
        } catch (ParseException e) {
          //  e.printStackTrace();
            timeFrom = "00:00";
        }
        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        String time = formatter2.format(dateObj);
        timeFrom =  time;

        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateObj = null;
        try {
            dateObj = formatter1.parse(end);
        } catch (ParseException e) {
            //  e.printStackTrace();
            timeTo = "00:00";
        }

        SimpleDateFormat formatter2 = new SimpleDateFormat("HH:mm");
        String time = formatter2.format(dateObj);
        timeTo =  time;

        this.end = end;
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Event.class);
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
