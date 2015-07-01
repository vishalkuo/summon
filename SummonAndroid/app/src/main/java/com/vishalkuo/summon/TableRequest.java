package com.vishalkuo.summon;

/**
 * Created by vishalkuo on 15-07-01.
 */
public class TableRequest {
    private String tableno;
    private String request;
    private String requestCode;


    public TableRequest(String t, String r, String requestCode){
        this.tableno =t;
        this.request = r;
        this.requestCode = requestCode;
    }

}
