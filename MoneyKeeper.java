import java.util.Random;

// Singleton, keeps banks money and do exchange
public enum MoneyKeeper {

	INSTANCE;

	private float startGrnValue;
	private float startUsdValue;

	private float grnValue;
	private float usdValue;
	private final float SELL_COURCE = 26.2f;
	private final float BUY_COURCE = 25.8f;
	private final float FLOAT_5f = 5f;
	private final float FLOAT_2f = 2f;

	private MoneyKeeper() {
		grnValue = startGrnValue = getStartMoney() * FLOAT_5f;
		usdValue = startUsdValue = getStartMoney();
	}

	private float getStartMoney() {
		Random random = new Random();
		// get number from 21000 to 30000
		return (random.nextInt(9) + 21) * 1000;
	}

	// synchronize method for only one customer
	public synchronized void doOperation(Customer customer) {

		float csGrnValue = customer.getGrnValue();
		float csUsdValue = customer.getUsdValue();
		float summOfOperation;

		if (getTypeOperation() == 0) {
			// sell half of usd
			summOfOperation = (csUsdValue / FLOAT_2f) * BUY_COURCE;

			if (summOfOperation > grnValue) {
				customer.setHaveEnoughMoneyFalse();
			} else {
				grnValue = grnValue - summOfOperation;
				usdValue = usdValue + (csUsdValue / FLOAT_2f);
				customer.setGrnValue(csGrnValue + summOfOperation);
				customer.setUsdValue(csUsdValue - (csUsdValue / FLOAT_2f));
			}

		} else {
			// buy usd on half of grn
			summOfOperation = (csGrnValue / FLOAT_2f) / SELL_COURCE;
			if (summOfOperation > usdValue) {
				customer.setHaveEnoughMoneyFalse();
			} else {
				grnValue = grnValue + (csGrnValue / FLOAT_2f);
				usdValue = usdValue - summOfOperation;
				customer.setGrnValue(csGrnValue - (csGrnValue / FLOAT_2f));
				customer.setUsdValue(csUsdValue + summOfOperation);
			}
		}
		BankMain.viewStateOfAll();
	}

	float getGrnValue() {
		return grnValue;
	}

	float getUsdValue() {
		return usdValue;
	}

	void setGrnValue(float grnValue) {
		this.grnValue = grnValue;
	}

	void setUsdValue(float usdValue) {
		this.usdValue = usdValue;
	}

	private int getTypeOperation() {
		Random random = new Random();
		// get number 0 or 1
		return Math.round(random.nextFloat());
	}

	float getStartGrnValue() {
		return startGrnValue;
	}

	float getStartUsdValue() {
		return startUsdValue;
	}

	float getProfit() {
		return (grnValue - startGrnValue) + (usdValue - startUsdValue)
				* SELL_COURCE;
	}

}
