package com.abhinay.walletsystem.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transaction_history")
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Long transactionId;
    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;
    @Column(name = "person_id")
    private String personId;
    @Column(name = "to_person_id")
    private String toPersonId;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Override
    public String toString() {
        return  ("id= " + transactionId +
                " | fromPersonId= " + personId  +
                " | transactionType= " + transactionType +
                " | toPersonId= " + toPersonId +
                " | amount= " + amount +
                " | transactionTime= " + transactionTime);
    }
}
