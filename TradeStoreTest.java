package com.tradestore.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import com.tradestore.main.InvalidTradeException;
import com.tradestore.main.Trade;
import com.tradestore.main.TradeStoreService;

class TradeStoreTest {

	private String tradeInput1 = "T1,1,CP-1,B1,20/05/2020,N";
	private String nullTradeInput = null;
	private String invalidTradeInput = "T1.1-CP-1|  B1|20/05/2020N";
	private String tradeInput2 = "T1,2,CP-1,B1,20/05/2020,N";
	private String tradeInput3 = "T1,1,CP-1,B3,10/05/2020,N";
	private String tradeInput4 = "T1,1,CP-1,B1,20/05/2020,N";
	private String tradeInput5 = "T1,1,CP-1,B1,20/05/2020,N";
	private String pattern = "dd/MM/yyyy";
	private SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	private TradeStoreService tradeStore;

	/*
	 * @Before public void setup() { Trade trade = new Trade();
	 * trade.setBookId(bookId); trade.setCounterPartyId(counterPartyId);
	 * trade.setCreatedDate(createdDate); trade.setExpired(expired);
	 * trade.setTradeId(tradeId); trade.setVersion(version);
	 * trade.setMaturityDate(maturityDate); }
	 */

	@Test
	void createTradeShouldRunNormally() throws InvalidTradeException {
		tradeStore = new TradeStoreService(tradeInput1);
		Trade td = tradeStore.createTrade(tradeInput1);
		assertNotNull(td);
		assertEquals("B1", td.getBookId());

	}

	@Test
	void shouldThrowNullPointerWhenInputStrIsNull() throws InvalidTradeException {
		
		Assertions.assertThrows(NullPointerException.class, () -> {
			tradeStore.createTrade(nullTradeInput);
		});

	}

	@Test
	void shouldThrowWhenInputStrIsInvalid() throws InvalidTradeException {
	
		assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			tradeStore.createTrade(invalidTradeInput);
		});

	}

	 @Test
	void validateTradeShouldRunNormally() throws ParseException, InvalidTradeException {

		Trade trade1 = new Trade();
		trade1.setBookId("B1");
		trade1.setCounterPartyId("CP-2");
		trade1.setCreatedDate(new Date());
		trade1.setExpired(false);
		trade1.setTradeId("T1");
		trade1.setVersion(2);
		trade1.setMaturityDate(dateFormat.parse("20/05/2020"));
				
		TradeStoreService service = new TradeStoreService(tradeInput1);
		assertEquals(true, service.validateTrade(trade1));
	}
	 
	 @Test
		void shoudThrowInvalidTradeExceptionWhenversionIsLower() throws ParseException, InvalidTradeException {

			Trade trade1 = new Trade();
			trade1.setBookId("B1");
			trade1.setCounterPartyId("CP-2");
			trade1.setCreatedDate(new Date());
			trade1.setExpired(false);
			trade1.setTradeId("T1");
			trade1.setVersion(2);
			trade1.setMaturityDate(dateFormat.parse("20/05/2020"));
			
			Trade trade2 = new Trade();
			trade2.setBookId("B1");
			trade2.setCounterPartyId("CP-2");
			trade2.setCreatedDate(new Date());
			trade2.setExpired(false);
			trade2.setTradeId("T1");
			trade2.setVersion(3);
			trade2.setMaturityDate(dateFormat.parse("20/05/2020"));
			
			TradeStoreService service = new TradeStoreService(tradeInput1);
			service.storeTrade(trade2);
			
			assertThrows(InvalidTradeException.class, () -> {
				tradeStore.validateTrade(trade1);
			});		

		}

}
