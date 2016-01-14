import java.util.Random;

public class Customer extends Thread {

	private volatile float grnValue;
	private volatile float usdValue;
	private int sleepTime;
	private MoneyKeeper moneyKeeper;
	private volatile Boolean haveEnoughMoney;

	public Customer(MoneyKeeper mk) {

		this.sleepTime = getRandomSleep();
		this.grnValue = getStartMoney();
		this.usdValue = getStartMoney();
		this.moneyKeeper = mk;
		this.haveEnoughMoney = true;

		// wait while all done
		try {
			sleep(3);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// and started operations
		this.start();
	}

	private float getStartMoney() {
		Random random = new Random();
		// get number from 500 to 1100
		return (random.nextInt(6) + 5) * 100;
	}

	@Override
	public void run() {

		while (haveEnoughMoney) {
			// do exchange operation
			moneyKeeper.doOperation(this);

			// if customer don't have enough money kick out him from bank
			if (grnValue < 100f) {
				haveEnoughMoney = false;
			}

			if (usdValue < 20f) {
				haveEnoughMoney = false;
			}

			try {
				sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	int getRandomSleep() {
		Random random = new Random();
		// get number from 1000 to 6000
		return (random.nextInt(4) + 1) * 1000;
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

	int getSleepTime() {
		return sleepTime / 1000;
	}

	void setHaveEnoughMoneyFalse() {
		this.haveEnoughMoney = false;
	}

}
