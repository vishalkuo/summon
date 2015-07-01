package com.vishalkuo.summon;

import java.util.List;

import retrofit.http.GET;

/**
 * Created by vishalkuo on 15-07-01.
 */
public interface RetroGet {
    @GET("/api/v1/menuItems")
    List<MenuItemInfo> resultList();
}
