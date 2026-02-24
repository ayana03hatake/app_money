package com.example.money_add_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.Arrays;
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
	private BarChartPanel chartPanel;
	private JPanel centerPanel;
	private boolean firstView = true;
	
	private Map<String, Integer> incomeMap = new HashMap<>();
	private Map<String, Integer> expenseMap = new HashMap<>();
	private Map<String, Integer> balanceMap = new HashMap<>();
	
	public HomePanel(Main frame) {
		
		setLayout(new BorderLayout());
		
		JButton btn01 = new JButton("Home画面");
		add(btn01, BorderLayout.NORTH);
		
		//ラベル作成
		label = new JLabel("",SwingConstants.CENTER);
		label.setFont(new java.awt.Font("メイリオ", java.awt.Font.BOLD,20));
		
		add(label, BorderLayout.CENTER);
		
		monthBox = new JComboBox<>();
		
		add(monthBox, BorderLayout.SOUTH);
		
		chartPanel = new BarChartPanel();
		chartPanel.setPreferredSize(new Dimension(400, 300));
		
		//データ読込
		loadData();
		
		//月選択時
		monthBox.addActionListener(e -> {
			
			//初回のみレイアウト切り替え
			if(firstView) {
				switchToSplitLayout();
				firstView = false;
			}
			
			updateLabel();
			updateGraph();
		});
	}
	
	//レイアウト切り替え用
	private void switchToSplitLayout() {
		
		remove(label);
		
		//中央パネル作成
		centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(label, BorderLayout.CENTER);
		centerPanel.add(monthBox, BorderLayout.SOUTH);
		
		add(centerPanel, BorderLayout.CENTER);
		add(chartPanel, BorderLayout.EAST);
		
		revalidate();
		repaint();
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
	
	//ラベルの更新
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
	
	//棒グラフの更新
	private void updateGraph() {
		String selectedMonth = (String)monthBox.getSelectedItem();
		if(selectedMonth == null) return;
		
		//年と月を分割
		String[] parts = selectedMonth.split("-");
		int year = Integer.parseInt(parts[0]);
		int month = Integer.parseInt(parts[1]);
		
		String[] labels = new String[3];
		int[] values = new int[3];
		
		//表示年月洗い出し
		for(int i = 0; i < 3; i++) {
			int m = month - i;
			int y = year;
			if(m <= 0) {
				m += 12;
				y -= 1;
			}
			String key = String.format("%04d-%02d", y,m);
			labels[2 - i] = key;//左から古い順
			values[2 - i] = balanceMap.getOrDefault(key, 0);
		}
		
		chartPanel.setData(labels, values);
		chartPanel.repaint();
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
	
	//棒グラフ書く用
	private static class BarChartPanel extends JPanel{
		private String[] labels = new String[0];
		private int[] values = new int[0];
		
		public void setData(String[] labels, int[] values) {
			this.labels = labels;
			this.values = values;
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(labels.length == 0) return;
			
			int w = getWidth();
			int h = getHeight();
			int margin = 40;
			int barWidth = (w - margin * 2) / labels.length - 20;
			
			//最大値計算
			int max = Arrays.stream(values).max().orElse(1);
			//最小値
			int min = Arrays.stream(values).min().orElse(0);
			
			//最大振れ幅
			int absMax = Math.max(Math.abs(max), Math.abs(min));
			
			//基準0を中央へ
			int zeroY = h / 2;
			
			//0ライン
			g.setColor(Color.GRAY);
			g.drawLine(margin, zeroY, w - margin, zeroY);
			
			for(int i = 0; i < labels.length; i++) {
				
				int barHeight = (int)((double)Math.abs(values[i]) / absMax * (h / 2 - margin));
				
				int x = margin + i * (barWidth + 20);
				
				if(values[i] >= 0) {
					//プラスを上に伸ばす
					g.setColor(Color.BLUE);
					g.fillRect(x, zeroY - barHeight, barWidth, barHeight);
					g.setColor(Color.BLACK);
					g.drawRect(x, zeroY - barHeight, barWidth, barHeight);
					g.drawString(String.valueOf(values[i]), x, zeroY - barHeight - 5);
				}else {
					//マイナスを下に伸ばす
					g.setColor(Color.RED);
					g.fillRect(x, zeroY, barWidth, barHeight);
					g.setColor(Color.BLACK);
					g.drawRect(x, zeroY, barWidth, barHeight);
					g.drawString(String.valueOf(values[i]), x, zeroY + barHeight + 15); 
				}
				
				g.drawString(labels[i], x, h - margin + 15);
				
			}
		}
	}


}

