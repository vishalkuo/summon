package com.vishalkuo.summon;

/**
 * Created by vishalkuo on 15-07-01.
 */
public class CurrentTableSingleton {
    private int currentTable = 0;

    private static CurrentTableSingleton ourInstance = new CurrentTableSingleton();

    public static CurrentTableSingleton getInstance() {
        return ourInstance;
    }

    private CurrentTableSingleton() {
    }

    public int getCurrentTable(){
        return ourInstance.getCurrentTable();
    }

    public void setCurrentTable(int table){
        ourInstance.currentTable = table;
    }


}
