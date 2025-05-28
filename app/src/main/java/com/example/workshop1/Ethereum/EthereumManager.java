package com.example.workshop1.Ethereum;

import android.content.Context;
import android.util.Log;

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

public class EthereumManager {
    private static final String TAG = "EthereumManager";
    private static final String PRIVATE_KEY = "0x29b22c4151c2e1b54a1fd816dc22b699eb9a3ae5d52b8ae9756f5c7b1bfcf00b"; // Replace with your private key
    private static final String CONTRACT_ADDRESS = "0x800ab6c7D7614d1C44dCD2a2741f2fbDAc8b08b0"; // Replace with your contract address
    private static final String NODE_URL = "http://localhost:8545"; // Assumes local node, adjust if needed

    private final Web3j web3j;
    private final Credentials credentials;
    private final ContractGasProvider gasProvider;
    private HKUToken contract;

    public EthereumManager() {
        // Initialize Web3j
        web3j = Web3j.build(new HttpService(NODE_URL));
        
        // Load credentials from private key
        credentials = Credentials.create(PRIVATE_KEY);
        
        // Use default gas provider
        gasProvider = new DefaultGasProvider();
        
        try {
            // Load the smart contract
            contract = HKUToken.load(
                CONTRACT_ADDRESS,
                web3j,
                credentials,
                gasProvider
            );
            
            Log.d(TAG, "Smart contract loaded successfully");
        } catch (Exception e) {
            Log.e(TAG, "Error loading smart contract: " + e.getMessage());
        }
    }

    // Method to mint tokens (admin only)
    public void mintTokens(String toAddress, BigInteger amount) {
        try {
            TransactionReceipt receipt = contract.mintTokens(toAddress, amount).send();
            Log.d(TAG, "Tokens minted successfully: " + receipt.getTransactionHash());
        } catch (Exception e) {
            Log.e(TAG, "Error minting tokens: " + e.getMessage());
        }
    }

    // Method to redeem tokens (students only)
    public void redeemTokens(String fromAddress, String toAddress, BigInteger amount) {
        try {
            TransactionReceipt receipt = contract.redeemTokens(fromAddress, toAddress, amount).send();
            Log.d(TAG, "Tokens redeemed successfully: " + receipt.getTransactionHash());
        } catch (Exception e) {
            Log.e(TAG, "Error redeeming tokens: " + e.getMessage());
        }
    }

    // Method to assign roles (admin only)
    public void assignRole(String userAddress, String role) {
        try {
            TransactionReceipt receipt = contract.assignRole(userAddress, role).send();
            Log.d(TAG, "Role assigned successfully: " + receipt.getTransactionHash());
        } catch (Exception e) {
            Log.e(TAG, "Error assigning role: " + e.getMessage());
        }
    }

    // Method to get token balance
    public BigInteger getBalance(String address) {
        try {
            return contract.balanceOf(address).send();
        } catch (Exception e) {
            Log.e(TAG, "Error getting balance: " + e.getMessage());
            return BigInteger.ZERO;
        }
    }
} 