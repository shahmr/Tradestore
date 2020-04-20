package com.tradestore.test;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.Test;
import com.tradestore.main.InvalidTradeException;
import com.tradestore.main.Trade;
import com.tradestore.main.TradeStoreService;

class TradeStoreTest {

	private String pattern = "dd/MM/yyyy";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	private String tradeInput = "T1|1|CP-1|B1|20/05/2020|N";

	@Test
	void testExecute() {
		TradeStoreService tradeStore = new TradeStoreService(tradeInput);
		/* tradeStoreexecuteTrade(tradeInput); */
		
	}

	@Test
	void testValidateTrade() throws ParseException {
		TradeStoreService tradeStore = new TradeStoreService(tradeInput);
		Trade trade = new Trade();
		trade.setTradeId("T1");
		trade.setVersion(1);
		trade.setCounterPartyId("CP-1");
		trade.setBookId("B1");
		trade.setMaturityDate(dateFormat.parse("20/05/2020"));
		trade.setCreatedDate(new Date());
		trade.setExpired(false);
		boolean isValid = false;
		try {
			isValid = tradeStore.validateTrade(trade);
		} catch (InvalidTradeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(true, isValid);

	}

}
