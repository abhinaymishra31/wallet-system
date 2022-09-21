package com.abhinay.walletsystem.repository;

import com.abhinay.walletsystem.model.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory,Long> {

    Optional<List<TransactionHistory>> findAllByPersonIdOrToPersonId(String personId,String toPersonId);
}
