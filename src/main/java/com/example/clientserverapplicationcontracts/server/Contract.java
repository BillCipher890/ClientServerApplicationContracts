package com.example.clientserverapplicationcontracts.server;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name="CONTRACT")
public class Contract implements Serializable {

    @Id
    @Column(name="NUMBER")
    public UUID NUMBER;

    @Temporal(TemporalType.DATE)
    @Column(name="CREATEDATE")
    public Date CREATEDATE;

    @Temporal(TemporalType.DATE)
    @Column(name="LASTUPDATEDATE")
    public Date LASTUPDATEDATE;


}
