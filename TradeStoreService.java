package com.tradestore.main;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TradeStoreService implements Runnable {

	static Logger logger = Logger.getLogger(TradeStoreService.class.getName());
	private String tradeStr;
	private HashMap<String, Trade> store = new HashMap<String, Trade>();
	private static String pattern = "dd/MM/yyyy"; 
	private static SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	 
	Date today = new Date();

	public TradeStoreService(String tradeStr) {
		this.tradeStr = tradeStr;
	}

	public void executeTrade(Trade trade) {
		try {
			boolean isValid = validateTrade(trade);

			if (isValid) {
				storeTrade(trade);
				sortTradeStrore();
			}
		} catch (InvalidTradeException ex) {
			logger.log(Level.SEVERE, "Trade execution failed", ex);
		}

	}

	private void sortTradeStrore() {
		store.
		store.sort((o1, o2)->o1.getAge()-o2.getAge());
		
	}

	private Trade createTrade(String inputTrade) {
		Trade trade = null;
		try {
			String[] inputparams = inputTrade.split(",");
			Date today = new Date();
			trade = new Trade();
			trade.setTradeId(inputparams[0]);
			trade.setVersion(Integer.parseInt(inputparams[1]));
			trade.setCounterPartyId(inputparams[2]);
			trade.setBookId(inputparams[3]);
			String maturityDt = inputparams[4];
			System.out.println(maturityDt);
			trade.setMaturityDate(dateFormat.parse(maturityDt));
			trade.setCreatedDate(today);
			trade.setExpired(Boolean.getBoolean(inputparams[5]));
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Trade creation failed", e);
		}
		return trade;

	}

	private Trade getTrade(String key) {
		Trade existingTrade = null;
		if (key != null) {
			existingTrade = store.get(key);
		}
		return existingTrade;
	}

	public boolean validateTrade(Trade trade) throws InvalidTradeException {
		boolean isTradevalid = false;
		String keygen = trade.getTradeId() + trade.getVersion();
		
		if (trade != null) {
			Trade existingTrade = getTrade(keygen);
			if (existingTrade == null) {
				return true;
			}
			if (existingTrade.getVersion() == trade.getVersion()) {
				isTradevalid = true;
			}
			if (existingTrade.getVersion() < trade.getVersion())
				throw new InvalidTradeException("Trade Version is invalid");

			if (trade.getMaturityDate().before(today)) {
				isTradevalid = false;
			}
		}
		return isTradevalid;
	}

	public void storeTrade(Trade trade) {
		if (trade != null) {
			String key = trade.getTradeId() + trade.getVersion();
			store.put(key, trade);
		}
	}

	public String getTrade() {
		return tradeStr;
	}

	public void setTrade(String tradeStr) {
		this.tradeStr = tradeStr;
	}

	@Override
	public void run() {
		Trade trade = createTrade(tradeStr);
		executeTrade(trade);
	}

}
