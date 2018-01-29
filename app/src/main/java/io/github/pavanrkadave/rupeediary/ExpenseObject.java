package io.github.pavanrkadave.rupeediary;

/**
 * Created by pavan on 1/29/2018.
 */

public class ExpenseObject {

    private String money;
    private String description;
    private String time;


    public ExpenseObject(String money, String description, String time) {
        this.money = money;
        this.description = description;
        this.time = time;
    }

    public ExpenseObject(){ }

    public String getMoney() {
        return money;
    }

    public String getDescription() {
        return description;
    }

    public String getTime() {
        return time;
    }
}

