package com.codingshuttle.project.uber.uberApp.services;

import com.codingshuttle.project.uber.uberApp.entities.Ride;
import com.codingshuttle.project.uber.uberApp.entities.User;
import com.codingshuttle.project.uber.uberApp.entities.Wallet;
import com.codingshuttle.project.uber.uberApp.entities.enums.TransactionMethod;

public interface WalletService {

	Wallet addMoneyToWallet(User user, Double amount , String transactionId, Ride ride, TransactionMethod transactionMethod);

	Wallet deductMoneyFromWallet(User user, Double amount , String transactionId, Ride ride, TransactionMethod transactionMethod);

	void withdrawMoneyFromWallet();

	Wallet findWalletById(Long walletId);

	Wallet crateNewWallet(User user);

	Wallet findByUser(User user);
}
