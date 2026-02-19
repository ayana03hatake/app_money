package com.example.money_add_app;

import java.time.Year;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

public class DetailPanel extends JPanel {

	public DetailPanel(Main frame) {
		int currentYear = Year.now().getValue();
		Integer[] years = new Integer[51];
		for (int i = 0; i <= 50; i++) {
			years[i] = currentYear - i;
		}
		JComboBox<Integer> yearCombo = new JComboBox<Integer>(years);
		yearCombo.setSelectedIndex(0); // 初期選択を今年に設定
		add(yearCombo);
		String[] months = {
				"1月", "2月", "3月", "4月", "5月", "6月",
				"7月", "8月", "9月", "10月", "11月", "12月"
		};
		JComboBox<String> monthCombo = new JComboBox<String>(months);
		monthCombo.setSelectedIndex(0); // 初期選択を1月に設定
		add(monthCombo);
		JButton btn02 = new JButton("検索");
		add(btn02);
	}

}
