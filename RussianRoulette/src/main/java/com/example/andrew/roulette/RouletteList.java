package com.example.andrew.roulette;

import java.util.ArrayList;

public class RouletteList {
    private String listName;
    private ArrayList<String> itemList;

    public RouletteList()
    {
        listName = "Roulette";
        itemList = new ArrayList<String>();
    }
    public RouletteList(String title, ArrayList<String> items)
    {
        listName = title;
        itemList = items;
    }

    public String getListName()
    {
        return this.listName;
    }

    public ArrayList<String> getItemList()
    {
        return this.itemList;
    }
    public void setListName(String name)
    {
        listName = name;
    }
    public void setItemList(ArrayList<String> items)
    {
        itemList = items;
    }
}
