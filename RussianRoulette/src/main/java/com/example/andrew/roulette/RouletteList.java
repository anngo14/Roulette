package com.example.andrew.roulette;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RouletteList implements Parcelable {
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
    public RouletteList(Parcel in)
    {
        listName = in.readString();
        itemList = in.readArrayList(String.class.getClassLoader());
    }
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR =
            new Parcelable.Creator() {
                public RouletteList createFromParcel(Parcel in) {
                    return new RouletteList(in);
                }

                public RouletteList[] newArray(int size) {
                    return new RouletteList[size];
                }
            };
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(listName);
        dest.writeList(itemList);
    }

    public String toString()
    {
        String output = "";
        for(String s: this.itemList)
        {
            output += s + "@!!!@";
        }
        return output;
    }
}
