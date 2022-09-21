package com.abhinay.walletsystem.dto;

import com.abhinay.walletsystem.model.TransactionStatus;
import com.abhinay.walletsystem.model.TransactionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SendTransactionDTO {

    private Long transactionId;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String to;
    private Double amount;
    private TransactionStatus transactionStatus;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime transactionTime;
}
