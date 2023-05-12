package com.example.clientserverapplicationcontracts.client;

import com.example.clientserverapplicationcontracts.server.Contract;
import lombok.Data;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Data
public class ContractJavaFX {
    public UUID NUMBER;
    public Date CREATEDATE;
    public Date LASTUPDATEDATE;
    public boolean LESSTHAN60DAYS;

    public ContractJavaFX(Contract contract){
        this.NUMBER = contract.getNUMBER();
        this.CREATEDATE = contract.getCREATEDATE();
        this.LASTUPDATEDATE = contract.getLASTUPDATEDATE();
        this.LESSTHAN60DAYS = System.currentTimeMillis() - contract.LASTUPDATEDATE.getTime() < TimeUnit.DAYS.toMillis(60);
    }
}
