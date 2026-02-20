package com.example.money_add_app;

import java.awt.Color;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class DetailPanel extends JPanel {
	public DetailPanel(Main frame) {
		// チェック!
		//		int currentYear = Year.now().getValue();
		//		Integer[] years = new Integer[51];
		//		for (int i = 0; i <= 50; i++) {
		//			years[i] = currentYear - i;
		//		}
		//		JComboBox<Integer> yearCombo = new JComboBox<Integer>(years);
		//		yearCombo.setSelectedIndex(0); // 初期選択を今年に設定
		//		add(yearCombo);
		//		String[] months = {
		//				"1月", "2月", "3月", "4月", "5月", "6月",
		//				"7月", "8月", "9月", "10月", "11月", "12月"
		//		};
		//		JComboBox<String> monthCombo = new JComboBox<String>(months);
		//		monthCombo.setSelectedIndex(0); // 初期選択を1月に設定
		//		add(monthCombo);
		//		JButton btn02 = new JButton("検索");
		//		add(btn02);

		//arraylist

		// 既存の JSON を読み込み（なければ空）
		//		List<Household> list = new ArrayList<>();
		//		if (Files.exists(DATA_PATH)) {
		//			try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
		//				List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
		//				if (loaded != null)
		//					list = loaded;
		//			}
		//		}

		String[] columnNames = { "日付", "項目", "金額", "メモ" };

		DefaultTableModel model = new DefaultTableModel(columnNames, 0);

	
		List<Household> householdList = new ArrayList<>();
		Household household1 = new Household("2026", "給料", BigDecimal.valueOf(400000), "給料");
		Household household2 = new Household("2020", "牛乳", BigDecimal.valueOf(500), "高い");
		Household household3 = new Household("2020", "牛乳", BigDecimal.valueOf(500), "高い");
		Household household4 = new Household("2020", "牛乳", BigDecimal.valueOf(500), "yas");
		householdList.add(household1);
		householdList.add(household2);
		householdList.add(household3);
		householdList.add(household4);

		for (Household in : householdList) {
			model.addRow(new Object[] {
					in.getDate(),
					in.getCategory(),
					in.getPrice(),
					in.getMemo()
			});
		}
		JTable table = new JTable(model);

		JPanel tablepanel = new JPanel();//テーブル用パネル
		tablepanel.setPreferredSize(new Dimension(700, 500));
		tablepanel.setBackground(new Color(230, 255, 230));//背景色

		JScrollPane scpane = new JScrollPane(table);
		scpane.setPreferredSize(new Dimension(650, 450));

		ArrayList<ArrayList<String>> tablelist = new ArrayList<>();

		//tablelist.add(new ArrayList<>(Arrays.asList(date, category, price, memo)));

		for (ArrayList<String> row : tablelist) {
			System.out.println(String.join("\t", row));
		}

		//-----------年月検索機能-----------------------------------------
		JButton serchbtn = new JButton("検索");//検索ボタン
		add(serchbtn);
		tablepanel.add(scpane);

		String[] combodateyear = { "2015年", "2016年", "2017年", "2018年", "2019年",
				"2020年", "2021年", "2022年", "2023年", "2024年", "2025年", "2026年" };//monthテキストボックスの中身
		JComboBox combo2 = new JComboBox(combodateyear);
		add(combo2);

		String[] combodatamonth = { "1月", "2月", "3月", "4月", "5月", "6月",
				"7月", "8月", "9月", "10月", "11月", "12月" };//yearテキストボックスの中身
		JComboBox combo = new JComboBox(combodatamonth);
		add(combo);

		add(tablepanel);
		frame.pack();
	}
}
