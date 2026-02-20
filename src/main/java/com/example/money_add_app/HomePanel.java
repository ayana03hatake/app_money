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
	
	//GUI
	public HomePanel(Main frame) {
		
		setLayout(new BorderLayout());
		
		JButton btn01 = new JButton("Home画面");
		add(btn01, BorderLayout.NORTH);
		
		JLabel label = new JLabel("",SwingConstants.CENTER);
		label.setFont(new java.awt.Font("メイリオ", java.awt.Font.BOLD,20));
		add(label, BorderLayout.CENTER);
		
		
		//LocalDate対応Gson
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,(JsonDeserializer<LocalDate>)(json, type, context) -> LocalDate.parse(json.getAsString())).create();
				
		Type listType = new TypeToken<List<Jsondata>>() {}.getType();
				
		try(FileReader reader = new FileReader("houseHoldList.json")){
					
			List<Jsondata> jd = gson.fromJson(reader, listType);
					
			//月別集計
			Map<String, Integer> incomeMap = new HashMap<>();
			Map<String, Integer> expenseMap = new HashMap<>();
			Map<String, Integer> balanceMap = new HashMap<>();
					
			for(Jsondata j : jd) {
						
				String month = j.getDate().getYear() + "-" + String.format("%02d", j.getDate().getMonthValue());
						
				int price = j.getPrice();
						
				//収支合計
				balanceMap.put(month, balanceMap.getOrDefault(month, 0) + price);
						
				//収入と支出を分離
				if(price > 0) {
					incomeMap.put(month, incomeMap.getOrDefault(month, 0) + price);
				}else if(price < 0) {
					expenseMap.put(month, expenseMap.getOrDefault(month, 0) + Math.abs(price));
				}
			}
			
			label.setText("<html><center>" + "<h2>==月次サマリー==</h2>" + 
							"年月を選択してください" + "</center></html>");
					
			Set<String> months = new TreeSet<>(balanceMap.keySet());
			
			JComboBox<String> monthBox = new JComboBox<>(months.toArray(new String[0]));
			add(monthBox,BorderLayout.SOUTH);
			
			monthBox.addActionListener(e -> {
				
				String month = (String)monthBox.getSelectedItem();
				
				int income = incomeMap.getOrDefault(month, 0);
				int expense = expenseMap.getOrDefault(month, 0);
				int balance = balanceMap.getOrDefault(month, 0);
				
				label.setText("<html><center>" + "<h2>月次サマリー</h2>" + 
							month + "<br></br>" + 
							"収入：<b>" + income + "</b><br>" +
							"支出：<b>" + expense + "</b><br>" +
							"収支：<b>" + balance + "</center></html>");
			});
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
	
	//メイン
	/*public static void main(String[] args) {
		
		//LocalDate対応Gson
		Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class,(JsonDeserializer<LocalDate>)(json, type, context) -> LocalDate.parse(json.getAsString())).create();
		
		Type listType = new TypeToken<List<Jsondata>>() {}.getType();
		
		try(FileReader reader = new FileReader("houseHoldList.json")){
			
			List<Jsondata> jd = gson.fromJson(reader, listType);
			
			//月別集計
			Map<String, Integer> incomeMap = new HashMap<>();
			Map<String, Integer> expenseMap = new HashMap<>();
			Map<String, Integer> balanceMap = new HashMap<>();
			
			for(Jsondata j : jd) {
				
				String month = j.getDate().getYear() + "-" + String.format("%02d", j.getDate().getMonthValue());
				
				int price = j.getPrice();
				
				//収支合計
				balanceMap.put(month, balanceMap.getOrDefault(month, 0) + price);
				
				//収入と支出を分離
				if(price > 0) {
					incomeMap.put(month, incomeMap.getOrDefault(month, 0) + price);
				}else if(price < 0) {
					expenseMap.put(month, expenseMap.getOrDefault(month, 0) + Math.abs(price));
				}
			}
			
			System.out.println("===月次サマリー===");
			
			Set<String> months = new TreeSet<>();
			months.addAll(balanceMap.keySet());
			
			for(String month : months) {
				
				int income = incomeMap.getOrDefault(month, 0);
				int expense = expenseMap.getOrDefault(month, 0);
				int balance = balanceMap.getOrDefault(month, 0);
				
				System.out.println(month + "｜収入" + income +
											"｜支出" + expense +
											"｜収支" + balance);
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}*/

}
