package com.bizconnectivity.tismobile.Classes;

import java.util.ArrayList;
import java.util.List;

public class TruckBayOrderList
{
    String title;
    List<Order> orderList;

    public TruckBayOrderList()
    {
        title = "";
        orderList = new ArrayList<Order>();
    }

    public TruckBayOrderList(String title)
    {
        this.title = title;
        orderList = new ArrayList<Order>();
    }

    public TruckBayOrderList(String title,List<Order> list)
    {
        this.title = title;
        orderList = list;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setOrderList(List<Order> list)
    {
        this.orderList = list;
    }

    public List<Order> getOrderList()
    {
        return orderList;
    }
}
