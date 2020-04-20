package com.tradestore.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeStoreApp {

	static Logger logger = Logger.getLogger(TradeStoreApp.class.getName());
	private static final int THREADS = 2;
	static ExecutorService executor = Executors.newFixedThreadPool(THREADS);

	public static void main(String[] args) {

		File file = new File("D:\\trades.txt");
		try {
			List<String> myTrades = new ArrayList<>();
			myTrades = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

			for (String trade : myTrades) {
				Runnable worker = new TradeStoreService(trade);
				executor.execute(worker);
				
			}
			executor.shutdown();
			awaitTerminationAfterShutdown(executor);
			System.out.println("\nFinished all threads");

		} catch (FileNotFoundException e) {
			logger.log(Level.SEVERE, "Input Trade File not Found", e);
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "Invalid Data in Input Trade File", ex);
		}

	}

	private static void awaitTerminationAfterShutdown(ExecutorService executor) {
		executor.shutdown();
		    try {
		        if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
		        	executor.shutdownNow();
		        }
		    } catch (InterruptedException ex) {
		    	executor.shutdownNow();
		        Thread.currentThread().interrupt();
		    }
		}
		
	}

