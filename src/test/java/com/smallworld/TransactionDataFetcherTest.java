package com.smallworld;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionDataFetcherTest {

    @Mock
    private ObjectMapper objectMapper;

    private TransactionDataFetcher dataFetcher;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.initMocks(this);

        List<Transaction> mockTransactions = List.of(
                new Transaction(663458, 430.2, "Tom Shelby", "Alfie Solomons", 22, 33, 1, false, "Looks like money laundering"),
                new Transaction(1284564, 150.2, "Tom Shelby", "Arthur Shelby", 22, 60, 2, true, "Never gonna give you up"),
                new Transaction(1284565, 150.2, "Aunt Polly", "Arthur Shelby", 22, 60, 3, false, "Looks like money laundering")
        );

        when(objectMapper.readValue((JsonParser) any(), any(TypeReference.class))).thenReturn(mockTransactions);

        dataFetcher = new TransactionDataFetcher(mockTransactions);
    }

    @Test
    void testGetTotalTransactionAmount() {
        double expected = 730.60; // Calculate the expected total manually
        double actual = dataFetcher.getTotalTransactionAmount();
        assertEquals(expected, actual, 0.01); // Using delta for double comparison
    }

    @Test
    void testGetTotalTransactionAmountSentBy() {
        double expected = 580.40; // Calculate the expected total manually
        double actual = dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        assertEquals(expected, actual, 0.01); // Using delta for double comparison
    }

    @Test
    void testGetMaxTransactionAmount() {
        double expected = 430.20; // The maximum transaction amount in the sample data
        double actual = dataFetcher.getMaxTransactionAmount();
        assertEquals(expected, actual, 0.01); // Using delta for double comparison
    }

    @Test
    void testCountUniqueClients() {
        long expected = 4; // Tom Shelby, Aunt Polly, Arthur Shelby, Alfie Solomons
        long actual = dataFetcher.countUniqueClients();
        assertEquals(expected, actual);
    }

    @Test
    void testHasOpenComplianceIssues() {
        assertTrue(dataFetcher.hasOpenComplianceIssues("Tom Shelby"));
        assertTrue(dataFetcher.hasOpenComplianceIssues("Arthur Shelby"));
    }

    @Test
    void testGetTransactionsByBeneficiaryName() {
        var transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(2, transactionsByBeneficiary.size());
        assertTrue(transactionsByBeneficiary.containsKey("Alfie Solomons"));
        assertTrue(transactionsByBeneficiary.containsKey("Arthur Shelby"));
        assertEquals(1, transactionsByBeneficiary.get("Alfie Solomons").size());
        assertEquals(2, transactionsByBeneficiary.get("Arthur Shelby").size());
    }

    @Test
    void testGetUnsolvedIssueIds() {
        var unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertEquals(2, unsolvedIssueIds.size());
        assertTrue(unsolvedIssueIds.contains(1));
        assertTrue(unsolvedIssueIds.contains(3));
    }

    @Test
    void testGetAllSolvedIssueMessages() {
        var solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(1, solvedIssueMessages.size());
        assertEquals("Never gonna give you up", solvedIssueMessages.get(0));
    }

    @Test
    void testGetTop3TransactionsByAmount() {
        var top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(430.2, top3Transactions.get(0).getAmount(), 0.01);
        assertEquals(150.2, top3Transactions.get(1).getAmount(), 0.01);
        assertEquals(150.2, top3Transactions.get(2).getAmount(), 0.01);
    }

    @Test
    void testGetTopSender() {
        var topSender = dataFetcher.getTopSender();
        assertTrue(topSender.isPresent());
        assertEquals("Tom Shelby", topSender.get());
    }
}
