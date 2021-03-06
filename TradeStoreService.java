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
				//sortTradeStrore();
			}
		} catch (InvalidTradeException ex) {
			logger.log(Level.SEVERE, "Trade execution failed", ex);
		}

	}

	/*
	 * private void sortTradeStrore() { List<Map.Entry<String, Trade> > list = new
	 * LinkedList<Map.Entry<String, Trade> >(store.entrySet()); Comparator<Trade> c
	 * = Comparator.comparing((Trade t) -> t.getTradeId()).thenComparing(t ->
	 * t.getVersion()); Collections.sort(store, c); }
	 */

	public Trade createTrade(String inputTrade) throws InvalidTradeException {
		Trade trade = null;
		if (inputTrade == null) {
			throw new NullPointerException("Input Trade String is null");
		}
		try {
			String[] inputparams = inputTrade.split(",");
			Date today = new Date();
			trade = new Trade();
			trade.setTradeId(inputparams[0]);
			trade.setVersion(Integer.parseInt(inputparams[1]));
			trade.setCounterPartyId(inputparams[2]);
			trade.setBookId(inputparams[3]);
			String maturityDt = inputparams[4];
			//System.out.println(maturityDt);
			trade.setMaturityDate(dateFormat.parse(maturityDt));
			trade.setCreatedDate(today);
			trade.setExpired(Boolean.getBoolean(inputparams[5]));
		} catch (ParseException e) {
			logger.log(Level.SEVERE, "Trade creation failed", e);
			throw new InvalidTradeException("Failed while parsing Input Trade String", e);
		} catch (NullPointerException ne) {
			logger.log(Level.SEVERE, "Trade creation failed", ne);
			throw new InvalidTradeException("Input Trade String is null", ne);
		}
		return trade;

	}

	public Trade getTrade(String key) {
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
		Trade trade;
		try {
			trade = createTrade(tradeStr);
		
		executeTrade(trade);
		} catch (InvalidTradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
