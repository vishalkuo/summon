package com.vishalkuo.summon;

/**
 * Created with IntelliJ IDEA.
 * Date: 13/05/13
 * Time: 10:36
 */
public class CurrentTableSingleton {
    private static CurrentTableSingleton mInstance = null;

    private String mString;

    private CurrentTableSingleton(){
        mString = "0";
    }

    public static CurrentTableSingleton getInstance(){
        if(mInstance == null)
        {
            mInstance = new CurrentTableSingleton();
        }
        return mInstance;
    }

    public String getString(){
        return this.mString;
    }

    public void setString(String value){
        mString = value;
    }
}