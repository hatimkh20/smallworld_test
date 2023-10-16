package com.smallworld;

import com.smallworld.data.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        try {
            TransactionDataFetcher dataFetcher = TransactionDataFetcher.fromJsonFile("transactions.json");

            double totalTransactionAmount = dataFetcher.getTotalTransactionAmount();
            System.out.println("Total Transaction Amount: " + totalTransactionAmount);

            String senderFullName = "Tom Shelby";
            double totalAmountSentBySender = dataFetcher.getTotalTransactionAmountSentBy(senderFullName);
            System.out.println("Total Amount Sent by " + senderFullName + ": " + totalAmountSentBySender);

            double maxTransactionAmount = dataFetcher.getMaxTransactionAmount();
            System.out.println("Max Transaction Amount: " + maxTransactionAmount);

            long uniqueClientsCount = dataFetcher.countUniqueClients();
            System.out.println("Unique Clients Count: " + uniqueClientsCount);

            String clientFullName = "Tom Shelby";
            boolean hasOpenComplianceIssues = dataFetcher.hasOpenComplianceIssues(clientFullName);
            System.out.println("Does " + clientFullName + " have open compliance issues? " + hasOpenComplianceIssues);

            Map<String, List<Transaction>> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
            System.out.println("Transactions by Beneficiary:");
            for (Map.Entry<String, List<Transaction>> entry : transactionsByBeneficiary.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

            Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
            System.out.println("Unsolved Issue IDs: " + unsolvedIssueIds);

            List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
            System.out.println("Solved Issue Messages: " + solvedIssueMessages);

            List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
            System.out.println("Top 3 Transactions by Amount:");
            for (int i = 0; i < top3Transactions.size(); i++) {
                Transaction transaction = top3Transactions.get(i);
                System.out.println("Transaction " + (i + 1) + ":");
                System.out.println("Amount: " + transaction.getAmount());
                System.out.println("Sender: " + transaction.getSenderFullName());
                System.out.println("Beneficiary: " + transaction.getBeneficiaryFullName());
                System.out.println();
            }


            Optional<String> topSender = dataFetcher.getTopSender();
            topSender.ifPresent(sender -> System.out.println("Top Sender by Total Sent Amount: " + sender));

        } catch (IOException e) {
            System.err.println("An error occurred while reading the JSON file: " + e.getMessage());
        }
    }
}

