package com.example.money_add_app;

import java.awt.BorderLayout;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.reflect.TypeToken;

public class HomePanel extends JPanel{
	
	private JLabel label;
	private JComboBox<String> monthBox;
	
	private Map<String, Integer> incomeMap = new HashMap<>();
	private Map<String, Integer> expenseMap = new HashMap<>();
	private Map<String, Integer> balanceMap = new HashMap<>();
	
	public HomePanel(Main frame) {
		
		setLayout(new BorderLayout());
		
		JButton btn01 = new JButton("Home画面");
		add(btn01, BorderLayout.NORTH);
		
		label = new JLabel("",SwingConstants.CENTER);
		label.setFont(new java.awt.Font("メイリオ", java.awt.Font.BOLD,20));
		add(label, BorderLayout.CENTER);
		
		monthBox = new JComboBox<>();
		add(monthBox, BorderLayout.SOUTH);
		
		monthBox.addActionListener(e -> updateLabel());
		
		loadData();
	}
	
	//再読み込み
	public void loadData() {
		
		incomeMap.clear();
		expenseMap.clear();
		balanceMap.clear();
		monthBox.removeAllItems();
		
		try(FileReader reader = new FileReader("householdList.json")){
			
			Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,(JsonDeserializer<LocalDate>)(json, type, context) -> LocalDate.parse(json.getAsString())).create();
			
			Type listType = new TypeToken<List<Jsondata>>() {}.getType();
			List<Jsondata> list = gson.fromJson(reader, listType);
			
			if(list == null)return;
			
			for(Jsondata j : list) {
				
				String month = j.getDate().getYear() + "-" + String.format("%02d", j.getDate().getMonthValue());
				
				int price = j.getPrice();
				
				balanceMap.put(month, balanceMap.getOrDefault(month, 0) + price);
				
				if(price > 0) {
					incomeMap.put(month, incomeMap.getOrDefault(month, 0) + price);
				}else{
					expenseMap.put(month, expenseMap.getOrDefault(month, 0) + Math.abs(price));
				}
			}
			
			Set<String> months = new TreeSet<>(balanceMap.keySet());
			
			for(String m : months) {
				monthBox.addItem(m);
			}
			
			label.setText("<html><center><h2>月次サマリー</h2>年月を選択してください</center></html>");
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateLabel() {
		
		if(label == null)return;
		
		String month = (String)monthBox.getSelectedItem();
		if(month == null)return;
		
		int income = incomeMap.getOrDefault(month, 0);
		int expense = expenseMap.getOrDefault(month, 0);
		int balance = balanceMap.getOrDefault(month, 0);
		
		label.setText("<html><center><h2>月次サマリー</h2>"
				+ month + "<br></br>"
				+ "収入：<b>" + String.format("%,d", income) + "</b><br>"
				+ "支出：<b>" + String.format("%,d", expense) + "</b><br>"
				+ "収支：<b>" + String.format("%,d", balance) + "</b>"
				+ "</center></html>");
	}
	
	//データクラス
	public static class Jsondata{
		
		private LocalDate date;
		private String category;
		private int price;
		
		public LocalDate getDate() {
			return date;
		}
		public String getCategory() {
			return category;
		}
		public int getPrice() {
			return price;
		}
	}


}

