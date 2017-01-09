package com.bizconnectivity.tismobile.Classes;

/**
 * Created by User on 10/11/2016.
 */

public class TimeslotDetail {
    int TimeSlotId;
    String DriverId,ArmNo;
    float Quantity;
    String url;

    TimeslotDetail()
    {
        DriverId = "";
        ArmNo = "";
        Quantity = 0;
    }

    public TimeslotDetail(int slotId,String oId,String arm,float q,String url)
    {
        TimeSlotId = slotId;
        DriverId = oId;
        ArmNo = arm;
        Quantity = q;
        this.url = url;
    }

    public void setTimeSlotId(int id)
    {
        TimeSlotId = id;
    }

    public int getTimeSlotId()
    {
        return TimeSlotId;
    }

    public void setDriverId(String id)
    {
        DriverId = id;
    }

    public String getDriverId()
    {
        return DriverId;
    }

    public void setArmNo(String arm)
    {
        ArmNo = arm;
    }

    public String getArmNo()
    {
        return ArmNo;
    }

    public void setQuantity(float q)
    {
        Quantity = q;
    }

    public float getQuantity()
    {
        return Quantity;
    }

    public void setPDFURL(String url)
    {
        this.url = url;
    }

    public String getPDFURL()
    {
        return url;
    }
}
