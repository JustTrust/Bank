import java.awt.Button;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;

public class BankMain extends JFrame implements ActionListener {

	private static JTextArea textArea = new JTextArea(6, 25);
	private static Customer[] customers = new Customer[5];
	static MoneyKeeper moneyKeeper = MoneyKeeper.INSTANCE;

	public BankMain() {
		super("Эмулятор обменки");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Create form
		final JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		Button btStart = new Button("START");
		btStart.setActionCommand("btStart");
		mainPanel.add(btStart);
		btStart.addActionListener(this);

		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border marginBorder = BorderFactory.createEmptyBorder(3, 10, 0, 0);

		textArea.setBorder(new CompoundBorder(lineBorder, marginBorder));
		textArea.setEditable(false);
		mainPanel.add(textArea);

		Container mainContainer = getContentPane();
		mainContainer.add(mainPanel);
		mainContainer.setPreferredSize(new Dimension(450, 400));
		pack();
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public static void main(String[] args) {

		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame.setDefaultLookAndFeelDecorated(true);
				new BankMain();
			}
		});
		fillStartData();

		viewStateOfAll();
	}

	static void viewStateOfAll() {

		StringBuffer stBuffer = new StringBuffer();

		for (int i = 0; i < customers.length; i++) {
			Customer ks = customers[i];
			stBuffer.append(ks.getName());
			stBuffer.append(" (" + ks.getSleepTime() + ")");
			stBuffer.append(",  UAH = ");
			stBuffer.append(String.format("% 10.2f", ks.getGrnValue()));
			stBuffer.append(",  USD = ");
			stBuffer.append(String.format("% 10.2f", ks.getUsdValue()));
			if (!ks.isAlive()) {
				stBuffer.append(" - stoped");
			}
			stBuffer.append("\n");
		}
		stBuffer.append("\n");
		stBuffer.append("Money keeper \n");
		stBuffer.append("Start money ");
		stBuffer.append("  UAH = ");
		stBuffer.append(String.format("% 10.2f", moneyKeeper.getStartGrnValue()));
		stBuffer.append("  USD = ");
		stBuffer.append(String.format("% 10.2f", moneyKeeper.getStartUsdValue()));
		stBuffer.append("\n");

		stBuffer.append("Current money ");
		stBuffer.append("  UAH = ");
		stBuffer.append(String.format("% 10.2f", moneyKeeper.getGrnValue()));
		stBuffer.append("  USD = ");
		stBuffer.append(String.format("% 10.2f", moneyKeeper.getUsdValue()));
		stBuffer.append("\n");

		stBuffer.append("PROFIT : ");
		stBuffer.append("  UAH = ");
		stBuffer.append(String.format("% 10.2f", moneyKeeper.getProfit()));
		stBuffer.append("\n");

		textArea.setText(stBuffer.toString());
	}

	private static void fillStartData() {

		for (int i = 0; i < customers.length; i++) {
			Customer ks = new Customer();
			ks.setName("Customer " + i);
			customers[i] = ks;
		}

		for (int i = 0; i < customers.length; i++) {
			Customer customer = customers[i];
			try {
				customer.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		viewStateOfAll();
		String st = textArea.getText();
		textArea.setText(st + "\n"
				+ "All stoped. Money keeper always in profit )");

	}

	static String getMainText() {
		return textArea.getText();
	}

	void setMainText(String text) {
		textArea.setText(text);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String a = e.getActionCommand();
		if (a.equals("btStart")) {

			for (int i = 0; i < customers.length; i++) {
				Customer customer = customers[i];
				customer.start();
			}
		}
	}

}
