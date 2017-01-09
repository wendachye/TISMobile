package com.bizconnectivity.tismobile.Classes;

/**
 * Created by User on 11/11/2016.
 */

public class OrderCount {
    int late,today,queueUp,weighIn;
    int pumpStart,departure,weighOut;
    int pumpStop;

    public OrderCount()
    {
        late = today = queueUp = weighIn = 0;
        pumpStart = departure = weighOut = 0;
        pumpStop = 0;
    }

    public OrderCount(int l,int t,int qUp,int wIn,int pS,int d,int wOut)
    {
        late = l;
        today = t;
        queueUp = qUp;
        weighIn = wIn;
        pumpStart = pS;
        departure = d;
        weighOut = wOut;
        pumpStop = 0;
    }

    public OrderCount(int l,int t,int qUp,int wIn,int pStart,int pStop,int d,int wOut)
    {
        late = l;
        today = t;
        queueUp = qUp;
        weighIn = wIn;
        pumpStart = pStart;
        departure = d;
        weighOut = wOut;
        pumpStop = pStop;
    }

    public void setLateCount(int l)
    {
        late = l;
    }

    public int getLateCount()
    {
        return late;
    }

    public void setTodayCount(int t)
    {
        today = t;
    }

    public int getTodayCount()
    {
        return today;
    }

    public void setQueueUpCount(int qUp)
    {
        queueUp = qUp;
    }

    public int getQueueUpCount()
    {
        return queueUp;
    }

    public void setWeighInCount(int wIn)
    {
        weighIn = wIn;
    }

    public int getWeighInCount()
    {
        return weighIn;
    }

    public void setPumpStartCount(int pS)
    {
        pumpStart = pS;
    }

    public int getPumpStartCount()
    {
        return pumpStart;
    }

    public void setPumpStopCount(int pS)
    {
        pumpStop = pS;
    }

    public int getPumpStopCount()
    {
        return pumpStop;
    }

    public void setDepartureCount(int d)
    {
        departure = d;
    }

    public int getDepartureCount()
    {
        return departure;
    }

    public void setWeighOutCount(int wOut)
    {
        weighOut = wOut;
    }

    public int getWeighOutCount()
    {
        return weighOut;
    }
}
