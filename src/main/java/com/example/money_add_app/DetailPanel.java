package com.example.money_add_app;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DetailPanel extends JPanel {
	private static final Path DATA_PATH = Path.of("householdList.json");
	private static final Path DATA_PATH2 = Path.of("categoryList.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Type LIST_TYPE = new TypeToken<List<Household>>() {
	}.getType();
	private static final Type LIST_TYPE2 = new TypeToken<String>() {
	}.getType();
	private JComboBox<String> categoryBox;

	public DetailPanel(Main frame) {

		//-----------検索ボタン-----------------------------------------
		JPanel buttonPanel = new JPanel();

		String[] yearList = { "2015年", "2016年", "2017年", "2018年", "2019年",
				"2020年", "2021年", "2022年", "2023年", "2024年", "2025年", "2026年", "全件取得" };
		JComboBox yearBox = new JComboBox(yearList);
		buttonPanel.add(yearBox);

		String[] monthList = { "01月", "02月", "03月", "04月", "05月", "06月",
				"07月", "08月", "09月", "10月", "11月", "12月", "全件取得" };
		JComboBox monthBox = new JComboBox(monthList);
		buttonPanel.add(monthBox);

		//		String[] categoryList = {
		//				"家賃", "水道", "ガス", "電気", "食費",
		//				"稽古", "化粧品", "外食費", "飲料", "娯楽費", "交際費", "趣味", "全件取得"
		//		};
		//JSONの読み込み

		categoryBox = new JComboBox();
		buttonPanel.add(categoryBox);

		updateCategoryList();

		JButton searchbtn = new JButton("検索");
		buttonPanel.add(searchbtn);

		JLabel explainLabel = new JLabel("検索したい範囲やカテゴリを入力してください");
		buttonPanel.add(explainLabel);
		add(buttonPanel, BorderLayout.NORTH);

		//テーブル作成
		String[] columnNames = { "日付", "項目", "金額", "メモ" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);
		JPanel contentPanel = new JPanel(new BorderLayout());
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);

		contentPanel.add(scrollPane, BorderLayout.CENTER);
		add(contentPanel, BorderLayout.CENTER);
		//-----------検索機能-----------------------------------------
		searchbtn.addActionListener(e -> {
			//JSONの読み込み
			List<Household> updatedHouseholdList = new ArrayList<>();
			if (Files.exists(DATA_PATH)) {
				try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
					List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
					if (loaded != null)
						updatedHouseholdList = loaded;

					//パネルから一度すべて削除したのちにテーブルを再設定
					contentPanel.removeAll();
					String selectedYear = String.valueOf(yearBox.getSelectedItem()).replaceAll("[^0-9]", "");
					String selectedMonth = String.valueOf(monthBox.getSelectedItem()).replaceAll("[^0-9]", "");
					DefaultTableModel selectedModel = new DefaultTableModel(columnNames, 0);
					List<Household> selectedHouseholdList = new ArrayList<>();

					for (Household household : updatedHouseholdList) {
						String[] splitedDate = household.getDate().split("-", 3);

						if (selectedYear.equals(splitedDate[0]) && selectedMonth.equals(splitedDate[1])) {
							selectedHouseholdList.add(household);
						} else if (yearBox.getSelectedItem().equals("全件取得") && selectedMonth.equals(splitedDate[1])) {
							selectedHouseholdList.add(household);
						} else if (selectedYear.equals(splitedDate[0]) && monthBox.getSelectedItem().equals("全件取得")) {
							selectedHouseholdList.add(household);
						} else if (yearBox.getSelectedItem().equals("全件取得")
								&& monthBox.getSelectedItem().equals("全件取得")) {
							selectedHouseholdList.add(household);
						}
					}

					for (Household household : selectedHouseholdList) {
						if (categoryBox.getSelectedItem().equals(household.getCategory())) {
							selectedModel.addRow(new Object[] {
									household.getDate(),
									household.getCategory(),
									household.getPrice(),
									household.getMemo()
							});
						} else if (categoryBox.getSelectedItem().equals("全件取得")) {
							selectedModel.addRow(new Object[] {
									household.getDate(),
									household.getCategory(),
									household.getPrice(),
									household.getMemo()
							});
						}
					}

					JTable selectedTable = new JTable(selectedModel);
					selectedTable.setAutoCreateRowSorter(true);
					JScrollPane scrollPane1 = new JScrollPane(selectedTable);
					if (selectedModel.getRowCount() == 0) {
						JLabel noDatalabel = new JLabel("※選択した範囲で登録された収支はありません");
						contentPanel.add(noDatalabel, BorderLayout.SOUTH);
					}
					contentPanel.add(scrollPane1);
					contentPanel.revalidate();
					contentPanel.repaint();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}

		});

	}

	public void updateCategoryList() {
		categoryBox.removeAll();

		try (FileReader reader = new FileReader("categoryList.json")) {
			Gson gson = new GsonBuilder().create();
			Type listType = new TypeToken<List<String>>(){}.getType();
			List<String> categoryList = gson.fromJson(reader, listType);

			for(String category : categoryList) {
				categoryBox.addItem(category);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
