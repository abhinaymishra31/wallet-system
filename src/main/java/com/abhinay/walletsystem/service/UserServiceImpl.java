package com.abhinay.walletsystem.service;

import com.abhinay.walletsystem.dto.*;
import com.abhinay.walletsystem.exceptions.*;
import com.abhinay.walletsystem.model.TransactionHistory;
import com.abhinay.walletsystem.model.TransactionStatus;
import com.abhinay.walletsystem.model.TransactionType;
import com.abhinay.walletsystem.model.UserInfo;
import com.fabhotel.walletsystem.dto.*;
import com.fabhotel.walletsystem.exceptions.*;
import com.abhinay.walletsystem.repository.TransactionHistoryRepository;
import com.abhinay.walletsystem.repository.UserInfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;


    @Override
    @Transactional
    public UserInfo userSignUp(UserInfo userInfo) {
        Optional<UserInfo> userFinder = userInfoRepository.findById(userInfo.getEmailId());
        if (userFinder.isPresent()) {
            logger.error("User is already registered using same id");
            throw new UserAlreadyExistsException("User already registered...");
        } else {
            logger.debug("UserRegister process finished");
            return userInfoRepository.save(userInfo);
        }
    }

    @Override
    @Transactional
    public UserInfo userUpdate(String emailId, UpdateDetailDTO updateDetailDTO) {
        Optional<UserInfo> userFinder = userInfoRepository.findById(emailId);
        if (!userFinder.isPresent()) {
            logger.error("Can not update user detail as user is not registered");
            throw new UserNotRegisteredException("User is not registered...");
        } else {
            UserInfo user=userFinder.get();
            user.setFirstName(updateDetailDTO.getFirstName());
            user.setLastName(updateDetailDTO.getLastName());
            user.setContactNo(updateDetailDTO.getContactNo());
            user.setUserType(updateDetailDTO.getUserType());
            user.setPassword(updateDetailDTO.getPassword());
            logger.debug("User update process finished");
            return userInfoRepository.save(user);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UserInfo getUserDetail(String emailId) {
        Optional<UserInfo> userInfo = userInfoRepository.findById(emailId);
        if (userInfo.isPresent()) {
            logger.debug("user details fetching process finished");
            return userInfo.get();
        } else {
            logger.error("unable to fetch user details as user is not registered");
            throw new UserNotRegisteredException("User details not found...");
        }
    }

    @Override
    @Transactional(noRollbackFor = {InsufficientAccountBalanceException.class})
    public UserInfo addBalanceToWallet(String emailId, AddWalletBalanceDTO addWalletBalanceDTO) {
        Optional<UserInfo> userFinder = userInfoRepository.findById(emailId);
//        if(userFinder.isPresent())
//        {
        UserInfo user = userFinder.get();
        String toPersonId = emailId;
        double amount = addWalletBalanceDTO.getAmount();
        double accBal = user.getAccountBalance();
        TransactionHistory th = new TransactionHistory();
        th.setPersonId(toPersonId);
        th.setToPersonId(toPersonId);
        th.setAmount(amount);
        th.setTransactionType(TransactionType.SELF_ADD);
        if (accBal >= amount) {
            user.setWalletBalance(userFinder.get().getWalletBalance() + amount);
            user.setAccountBalance(accBal - amount);

            th.setTransactionStatus(TransactionStatus.SUCCESSFUL);
            th.setTransactionTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            transactionHistoryRepository.save(th);
            logger.debug("Adding balance to wallet from bank account process finished");
            return userInfoRepository.save(user);
        } else {
            th.setTransactionStatus(TransactionStatus.FAILED);
            th.setTransactionTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
            transactionHistoryRepository.save(th);
            logger.error("Account balance is not sufficient");
            throw new InsufficientAccountBalanceException("Insufficient amount in account...");
        }
//        }
//        else
//        {
//            throw new UserNotRegisteredException("User is not registered...");
//        }
    }

    @Override
    @Transactional(noRollbackFor = {InsufficientAccountBalanceException.class})
    public UserInfo sendBalanceFromWallet(String emailId, SendWalletBalanceDTO sendWalletBalanceDTO) {
        Optional<UserInfo> userFinder = userInfoRepository.findById(emailId);
//        if(userFinder.isPresent())
//        {
        UserInfo user = userFinder.get();
        String toPersonId = sendWalletBalanceDTO.getToPersonId();
        double amount = sendWalletBalanceDTO.getAmount();
        double walletBalance = user.getWalletBalance();
        TransactionHistory th = new TransactionHistory();
        th.setPersonId(emailId);
        th.setToPersonId(toPersonId);
        th.setAmount(amount);
        th.setTransactionType(TransactionType.SENT);
        Optional<UserInfo> receiver = userInfoRepository.findById(toPersonId);
        if (receiver.isPresent() && !receiver.get().getEmailId().equals(emailId)) {
            if (walletBalance >= amount) {
                user.setWalletBalance(walletBalance - amount);
                receiver.get().setWalletBalance(receiver.get().getWalletBalance() + amount);
                userInfoRepository.save(receiver.get());
                logger.info("user wallet balance changed");
                th.setTransactionStatus(TransactionStatus.SUCCESSFUL);
                th.setTransactionTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                transactionHistoryRepository.save(th);
                logger.info("history saved");
                logger.debug("Sending balance from one wallet to other wallet process finished");
                return userInfoRepository.save(user);
            } else {
                th.setTransactionStatus(TransactionStatus.FAILED);
                th.setTransactionTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                transactionHistoryRepository.save(th);
                logger.info("history saved");
                logger.error("Insufficient balance in wallet. Transaction failed");
                throw new InsufficientAccountBalanceException("Insufficient balance in wallet...");
            }
        }
        else
        {
            if(!receiver.get().getEmailId().equals(emailId))
            {
                th.setTransactionStatus(TransactionStatus.FAILED);
                th.setTransactionTime(LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                transactionHistoryRepository.save(th);
                logger.info("history saved");
                logger.error("recipient is not registered. Transaction failed");
                throw new ReceiverDoesNotExistException("Receiver is not registered...");
            }
            logger.error("sender and receiver cannot be same. Transaction couldn't be processed");
            throw new SelfWalletSendException("same sender and receiver not allowed");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public PassBookDTO viewPassBook(String emailId) {
        Optional<UserInfo> userFinder = userInfoRepository.findById(emailId);
        List<Object> historyList=new ArrayList<>();
        if (userFinder.isPresent()) {
            UserInfo user = userFinder.get();
            Optional<List<TransactionHistory>> transactionHistories = getHistory(emailId);

            PassBookDTO passBookDTO = new PassBookDTO();
            passBookDTO.setAccountBalance(user.getAccountBalance());
            passBookDTO.setWalletBalance(user.getWalletBalance());

            if (transactionHistories.isEmpty()) {
                System.out.println("No transaction history to show...");
                logger.info("no transactions no history");
            }
            else
            {
                for(TransactionHistory th: transactionHistories.get())
                {
                    if(th.getToPersonId().equals(emailId) && !th.getTransactionType().equals(TransactionType.SELF_ADD)
                            && th.getTransactionStatus().equals(TransactionStatus.SUCCESSFUL))
                    {
//                        th.setTransactionType(TransactionType.RECEIVED);
                        ReceivedTransactionDTO receivedTransactionDTO=new ReceivedTransactionDTO();
                        receivedTransactionDTO.setTransactionId(th.getTransactionId());
                        receivedTransactionDTO.setFrom(th.getPersonId());
                        receivedTransactionDTO.setTransactionType(TransactionType.RECEIVED);
                        receivedTransactionDTO.setAmount(th.getAmount());
                        receivedTransactionDTO.setTransactionStatus(th.getTransactionStatus());
                        receivedTransactionDTO.setTransactionTime(LocalDateTime.parse(th.getTransactionTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                        historyList.add(receivedTransactionDTO);
                        logger.info("received type transaction added. from [{}], to[{}]",th.getPersonId(),emailId);
                    } else if (th.getTransactionType().equals(TransactionType.SELF_ADD)) {
                        SelfTransactionDTO selfTransactionDTO=new SelfTransactionDTO();
                        selfTransactionDTO.setTransactionId(th.getTransactionId());
                        selfTransactionDTO.setTransactionType(th.getTransactionType());
                        selfTransactionDTO.setAmount(th.getAmount());
                        selfTransactionDTO.setTransactionStatus(th.getTransactionStatus());
                        selfTransactionDTO.setTransactionTime(LocalDateTime.parse(th.getTransactionTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                        historyList.add(selfTransactionDTO);
                        logger.info("balance added to wallet from bank account");
                    }
                    else if(!th.getToPersonId().equals(emailId) && th.getTransactionType().equals(TransactionType.SENT))
                    {
                        SendTransactionDTO sendTransactionDTO=new SendTransactionDTO();
                        sendTransactionDTO.setTransactionId(th.getTransactionId());
                        sendTransactionDTO.setTo(th.getToPersonId());
                        sendTransactionDTO.setTransactionType(TransactionType.SENT);
                        sendTransactionDTO.setAmount(th.getAmount());
                        sendTransactionDTO.setTransactionStatus(th.getTransactionStatus());
                        sendTransactionDTO.setTransactionTime(LocalDateTime.parse(th.getTransactionTime()
                                .format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")));
                        historyList.add(sendTransactionDTO);
                        logger.info("sent type transaction added. from [{}], to [{}]",emailId, th.getToPersonId());

                    }
                }
            }
//            transactionHistories.get()
            passBookDTO.setHistoryList(historyList);
            logger.debug("Passbook fetching process finished");
            return passBookDTO;
        } else {
            logger.error("No passbook available as user is not registered");
            throw new UserNotRegisteredException("No passbook available as User is not registered...");
        }
    }

    @Transactional(readOnly = true)
    private Optional<List<TransactionHistory>> getHistory(String emailId) {
        return transactionHistoryRepository.findAllByPersonIdOrToPersonId(emailId,emailId);
    }

}
