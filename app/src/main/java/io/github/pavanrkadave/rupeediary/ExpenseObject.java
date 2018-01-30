package io.github.pavanrkadave.rupeediary;

/**
 * Created by pavan on 1/29/2018.
 */

public class ExpenseObject {

    private String money;
    private String description;
    private String dateToday;
    private String dayToday;


    public ExpenseObject(String money, String description, String dateToday,String dayToday) {
        this.money = money;
        this.description = description;
        this.dateToday = dateToday;
        this.dayToday = dayToday;
    }

    public ExpenseObject(){ }

    public String getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }

    public String getDateToday() {
        return dateToday;
    }

    public String getDayToday() {
        return dayToday;
    }
}

