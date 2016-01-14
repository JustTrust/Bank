import java.util.Random;

// Keeps banks money and do exchange
public class MoneyKeeper extends Thread {

	private float startGrnValue;
	private float startUsdValue;

	private float grnValue;
	private float usdValue;
	private final float SELL_COURCE = 26.2f;
	private final float BUY_COURCE = 25.8f;

	public MoneyKeeper() {

		grnValue = startGrnValue = getStartMoney() * 5f;
		usdValue = startUsdValue = getStartMoney();

		// wait while all done
		try {
			sleep(4);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// and started operations
		this.start();
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
			summOfOperation = (csUsdValue / 2) * BUY_COURCE;

			if (summOfOperation > grnValue) {
				customer.setHaveEnoughMoneyFalse();
			} else {
				grnValue = grnValue - summOfOperation;
				usdValue = usdValue + (csUsdValue / 2);
				customer.setGrnValue(csGrnValue + summOfOperation);
				customer.setUsdValue(csUsdValue - (csUsdValue / 2));
			}

		} else {
			// buy usd on half of grn
			summOfOperation = (csGrnValue / 2) / SELL_COURCE;
			if (summOfOperation > usdValue) {
				customer.setHaveEnoughMoneyFalse();
			} else {
				grnValue = grnValue + (csGrnValue / 2);
				usdValue = usdValue - summOfOperation;
				customer.setGrnValue(csGrnValue - (csGrnValue / 2));
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
