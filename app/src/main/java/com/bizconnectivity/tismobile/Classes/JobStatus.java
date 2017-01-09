package com.bizconnectivity.tismobile.Classes;

/**
 * Created by User on 10/11/2016.
 */
import java.util.Date;

public class JobStatus {
    int timeslotId,operatorID;
    String status,OperatorIdNum,driverWI;
    boolean isPPE,isOperator,isDriver,isSafetyCheck,isLoadingArm,isBC;
    boolean isPumpStart,isPumpStop,isSeal,isDeparture;
    Date pumpStartTime,pumpStopTime,departureTime;

    JobStatus()
    {
        timeslotId = operatorID = 0;
        status = OperatorIdNum = driverWI = "";
        isPPE = isOperator = isDriver = isSafetyCheck = isLoadingArm = isBC = false;
        isPumpStart = isPumpStop = isSeal = isDeparture = false;
        pumpStartTime = pumpStopTime = departureTime = null;
    }

    public JobStatus(int tsId,String stat,boolean isPPE,boolean isOperator,int operatorID,String OperatorIdNum,
                     boolean isDriver,String driverWI,boolean isSafetyCheck,boolean isLoadingArm,boolean isBC,
                     boolean isPumpStart,Date pumpStartTime,boolean isPumpStop,Date pumpStopTime,
                     boolean isSeal,boolean isDeparture,Date departureTime)
    {
        this.timeslotId = tsId;
        this.status = stat;
        this.isPPE = isPPE;
        this.isOperator = isOperator;
        this.operatorID = operatorID;
        this.OperatorIdNum = OperatorIdNum;
        this.isDriver = isDriver;
        this.driverWI = driverWI;
        this.isSafetyCheck = isSafetyCheck;
        this.isLoadingArm = isLoadingArm;
        this.isBC = isBC;
        this.isPumpStart = isPumpStart;
        this.pumpStartTime = pumpStartTime;
        this.isPumpStop = isPumpStop;
        this.pumpStopTime = pumpStopTime;
        this.isSeal = isSeal;
        this.isDeparture = isDeparture;
        this.departureTime = departureTime;
    }

    public void setTimeslotId(int tsId)
    {
        this.timeslotId = tsId;
    }

    public int getTimeslotId()
    {
        return timeslotId;
    }

    public void setStatus(String stat)
    {
        this.status = stat;
    }

    public String getStatus()
    {
        return status;
    }

    public void setIsPPE(boolean isPPE)
    {
        this.isPPE = isPPE;
    }

    public boolean getIsPPE()
    {
        return isPPE;
    }

    public void setIsOperator(boolean isOperator)
    {
        this.isOperator = isOperator;
    }

    public boolean getIsOperator()
    {
        return isOperator;
    }

    public void setOperatorId(int operatorID)
    {
        this.operatorID = operatorID;
    }

    public int getOperatorID()
    {
        return operatorID;
    }

    public void setOperatorIdNum(String OperatorIdNum)
    {
        this.OperatorIdNum = OperatorIdNum;
    }

    public String getOperatorIDNum()
    {
        return OperatorIdNum;
    }

    public void setIsDriver(boolean isDriver)
    {
        this.isDriver = isDriver;
    }

    public boolean getIsDriver()
    {
        return isDriver;
    }

    public void setDriverWI(String driverWI)
    {
        this.driverWI = driverWI;
    }

    public String getDriverWI()
    {
        return driverWI;
    }

    public void setIsSafetyCheck(boolean isSafetyCheck)
    {
        this.isSafetyCheck = isSafetyCheck;
    }

    public boolean getIsSafetyCheck()
    {
        return isSafetyCheck;
    }

    public void setIsLoadingArm(boolean isLoadingArm)
    {
        this.isLoadingArm = isLoadingArm;
    }

    public boolean getIsLoadingArm()
    {
        return isLoadingArm;
    }

    public void setIsBC(boolean isBC)
    {
        this.isBC = isBC;
    }

    public boolean getIsBC()
    {
        return isBC;
    }

    public void setIsPumpStart(boolean isPumpStart)
    {
        this.isPumpStart = isPumpStart;
    }

    public boolean getIsPumpStart()
    {
        return isPumpStart;
    }

    public void setPumpStartTime(Date pumpStartTime)
    {
        this.pumpStartTime = pumpStartTime;
    }

    public Date getPumpStartTime()
    {
        return pumpStartTime;
    }

    public void setIsPumpStop(boolean isPumpStop)
    {
        this.isPumpStop = isPumpStop;
    }

    public boolean getIsPumpStop()
    {
        return isPumpStop;
    }

    public void setPumpStopTime(Date pumpStopTime)
    {
        this.pumpStopTime = pumpStopTime;
    }

    public Date getPumpStopTime()
    {
        return pumpStopTime;
    }

    public void setIsSeal(boolean isSeal)
    {
        this.isSeal = isSeal;
    }

    public boolean getIsSeal()
    {
        return isSeal;
    }

    public void setIsDeparture(boolean isDeparture)
    {
        this.isDeparture = isDeparture;
    }

    public boolean getIsDeparture()
    {
        return isDeparture;
    }

    public void setDepartureTime(Date departureTime)
    {
        this.departureTime = departureTime;
    }

    public Date getDepartureTime()
    {
        return departureTime;
    }
}
