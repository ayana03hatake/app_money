package com.example.money_add_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class InputPanel extends JPanel {
	private static final Path DATA_PATH = Path.of("householdList.json");
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static final Type LIST_TYPE = new TypeToken<List<Household>>() {
	}.getType();

	public InputPanel(Main frame) {
		JButton btn02 = new JButton("入力画面");
		add(btn02);

		

//		GridLayout gridLayout = new GridLayout(5, 2, 10, 10);
//		setLayout(gridLayout);
		setBackground(new Color(230, 255, 230));//背景色

		JLabel datelabel = new JLabel("日付");
		JLabel categorylabel = new JLabel("カテゴリー");
		JLabel pricelabel = new JLabel("金額");
		JLabel memolabel = new JLabel("メモ");

		datelabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
		categorylabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
		pricelabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
		memolabel.setFont(new Font("MSゴシック", Font.BOLD, 18));

		datelabel.setHorizontalAlignment(JLabel.CENTER);
		categorylabel.setHorizontalAlignment(JLabel.CENTER);
		pricelabel.setHorizontalAlignment(JLabel.CENTER);
		memolabel.setHorizontalAlignment(JLabel.CENTER);

		// 入力欄（ここを _Field に統一）

		JTextField dateField = new JTextField(10);
		JTextField categoryField = new JTextField(15);
		JTextField priceField = new JTextField(15);
		JTextField memoField = new JTextField(20);

		// 見た目
		Font f = new Font("MS ゴシック", Font.BOLD, 20);
		for (JTextField ta : new JTextField[] { dateField, categoryField, priceField, memoField }) { //配列（dateField / categoryField / priceField / memoField）
			ta.setPreferredSize(new Dimension(150, 30)); //入力欄 の　箱の設定一括で設定
			ta.setFont(f);
		}

		add(datelabel);
		add(dateField);
		add(categorylabel);
		add(categoryField);
		add(pricelabel);
		add(priceField);
		add(memolabel);
		add(memoField);

		//ーー追加ーー
		JPanel buttunface = new JPanel();//ボタン用パネル
		buttunface.setBackground(new Color(230, 255, 230));
		//ーー

		JButton saveButton = new JButton("保存"); //保存ボタン
		buttunface.add(saveButton); //*名称変更		

//		frame.add(panel);
		//----追加したボタン---
		JButton income = new JButton("収入");
		buttunface.add(income);
		JButton outcome = new JButton("支出");
		buttunface.add(outcome);

		frame.setLocationRelativeTo(null);//中央揃え
		frame.setSize(1000, 800);
		frame.add(buttunface, BorderLayout.SOUTH);
		add(buttunface);
		add(new JLabel()); // 左セルを空ける
		add(buttunface);//パネルに表示
		frame.setVisible(true);
		setVisible(true);
		buttunface.setVisible(true);
		frame.pack();
		//--------------

		// ① コンソール出力（デバッグ用）
		saveButton.addActionListener(e -> {
			System.out.println("保存をしました！");
			System.out.println(dateField.getText().trim());
			System.out.println(categoryField.getText().trim());
			System.out.println(priceField.getText().trim());
			System.out.println(memoField.getText().trim());
		});

		income.addActionListener(e -> {
			System.out.println("収入に追加");
			System.out.println(dateField.getText().trim());
			System.out.println(categoryField.getText().trim());
			System.out.println(priceField.getText().trim());
			System.out.println(memoField.getText().trim());

		});
		outcome.addActionListener(e -> {
			System.out.println("支出に追加");
			System.out.println(dateField.getText().trim());
			System.out.println(categoryField.getText().trim());
			System.out.println(priceField.getText().trim());
			System.out.println(memoField.getText().trim());
		});

		// ② JSON 保存
		saveButton.addActionListener(e -> {
			try {
				String date = dateField.getText().trim();
				String category = categoryField.getText().trim();
				String priceText = priceField.getText().trim();
				String memo = memoField.getText().trim();

				BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);

				Household item = new Household(date, category, price, memo);

				// 既存の JSON を読み込み（なければ空）
				List<Household> list = new ArrayList<>();
				if (Files.exists(DATA_PATH)) {
					try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
						List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
						if (loaded != null)
							list = loaded;
					}
				}

				// 追加
				list.add(item);

				// 親フォルダがあれば作成
				Path parent = DATA_PATH.getParent();
				if (parent != null) {
					Files.createDirectories(parent);
				}

				// 上書き保存（安全）
				try (BufferedWriter writer = Files.newBufferedWriter(
						DATA_PATH, StandardCharsets.UTF_8,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
					GSON.toJson(list, LIST_TYPE, writer);
				}

				JOptionPane.showMessageDialog(frame, "JSONへ保存しました！\n" + DATA_PATH.toAbsolutePath());

			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame, "金額は数値で入力してください。",
						"入力エラー", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame,
						"保存に失敗しました: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
						"エラー", JOptionPane.ERROR_MESSAGE);
			}
		});

		income.addActionListener(e -> {
			try {
				String date = dateField.getText().trim();
				String category = categoryField.getText().trim();
				String priceText = priceField.getText().trim();
				String memo = memoField.getText().trim();

				BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);

				Household item = new Household(date, category, price, memo);

				// 既存の JSON を読み込み（なければ空）
				List<Household> list = new ArrayList<>();
				if (Files.exists(DATA_PATH)) {
					try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
						List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
						if (loaded != null)
							list = loaded;
					}
				}

				// 追加
				list.add(item);

				// 親フォルダがあれば作成
				Path parent = DATA_PATH.getParent();
				if (parent != null) {
					Files.createDirectories(parent);
				}

				// 上書き保存（安全）
				try (BufferedWriter writer = Files.newBufferedWriter(
						DATA_PATH, StandardCharsets.UTF_8,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
					GSON.toJson(list, LIST_TYPE, writer);
				}

				JOptionPane.showMessageDialog(frame, "JSONへ保存しました！\n" + DATA_PATH.toAbsolutePath());

			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame, "金額は数値で入力してください。",
						"入力エラー", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame,
						"保存に失敗しました: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
						"エラー", JOptionPane.ERROR_MESSAGE);
			}
		});

		outcome.addActionListener(e -> {
			try {
				String date = dateField.getText().trim();
				String category = categoryField.getText().trim();
				String priceText = priceField.getText().trim();
				String memo = memoField.getText().trim();

				BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);
				BigDecimal expense = price.multiply(BigDecimal.valueOf(-1));// 支出はマイナスにする（ここがポイント）

				Household item = new Household(date, category, expense, memo);

				// 既存の JSON を読み込み（なければ空）
				List<Household> list = new ArrayList<>();
				if (Files.exists(DATA_PATH)) {
					try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
						List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
						if (loaded != null)
							list = loaded;
					}
				}

				// 追加
				list.add(item);

				// 親フォルダがあれば作成
				Path parent = DATA_PATH.getParent();
				if (parent != null) {
					Files.createDirectories(parent);
				}

				// 上書き保存（安全）
				try (BufferedWriter writer = Files.newBufferedWriter(
						DATA_PATH, StandardCharsets.UTF_8,
						StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
					GSON.toJson(list, LIST_TYPE, writer);
				}

				JOptionPane.showMessageDialog(frame, "JSONへ保存しました！\n" + DATA_PATH.toAbsolutePath());

			} catch (NumberFormatException nfe) {
				JOptionPane.showMessageDialog(frame, "金額は数値で入力してください。",
						"入力エラー", JOptionPane.ERROR_MESSAGE);
			} catch (Exception ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(frame,
						"保存に失敗しました: " + ex.getClass().getSimpleName() + " - " + ex.getMessage(),
						"エラー", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

}
