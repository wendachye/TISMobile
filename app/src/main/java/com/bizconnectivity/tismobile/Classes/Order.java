package com.bizconnectivity.tismobile.Classes;

public class Order
{
    int TimeSlotId;
    String OrderId,Customer,TankNo,Product,TruckRackNo;

    Order()
    {
        OrderId = "";
        Customer = "";
        TankNo = "";
        Product = "";
        TruckRackNo = "";
    }

    Order(String id,String cust,String tank,String prod)
    {
        OrderId = id;
        Customer = cust;
        TankNo = tank;
        Product = prod;
        TruckRackNo = "";
    }

    public Order(int slotId, String oId,String cust,String tank,String prod,String rackNo)
    {
        TimeSlotId = slotId;
        OrderId = oId;
        Customer = cust;
        TankNo = tank;
        Product = prod;
        TruckRackNo = rackNo;
    }

    public void seTimeSlotId(int id)
    {
        TimeSlotId = id;
    }

    public int getTimeSlotId()
    {
        return TimeSlotId;
    }

    public void setOrderId(String id)
    {
        OrderId = id;
    }

    public String getOrderId()
    {
        return OrderId;
    }

    public void setCustomer(String cust)
    {
        Customer = cust;
    }

    public String getCustomer()
    {
        return Customer;
    }

    public void setTankNo(String tank)
    {
        TankNo = tank;
    }

    public String getTankNo()
    {
        return TankNo;
    }

    public void setProduct(String prod)
    {
        Product = prod;
    }

    public String getProduct()
    {
        return Product;
    }

    public void setRackNo(String rackNo)
    {
        TruckRackNo = rackNo;
    }

    public String getRackNo()
    {
        return TruckRackNo;
    }
}
