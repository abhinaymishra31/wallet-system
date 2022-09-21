package com.abhinay.walletsystem.dto;

import com.abhinay.walletsystem.model.TransactionStatus;
import com.abhinay.walletsystem.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReceivedTransactionDTO {

    private Long transactionId;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String from;
    private Double amount;
    private TransactionStatus transactionStatus;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transactionTime;
}
