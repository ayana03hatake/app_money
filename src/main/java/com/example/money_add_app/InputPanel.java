package com.example.money_add_app;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.MaskFormatter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
 
public class InputPanel extends JPanel {
    private static final Path DATA_PATH = Path.of("householdList.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Type LIST_TYPE = new TypeToken<List<Household>>() {}.getType();
 
    // 日付
    private static final DateTimeFormatter STRICT_YYYY_MM_DD =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);
 
    private static boolean isValidYyyyMmDd(String s) {
        try {
            LocalDate.parse(s, STRICT_YYYY_MM_DD);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    // ==============================
 
    public InputPanel(Main frame) {
 
        GridLayout gridLayout = new GridLayout(5, 2, 10, 10);
        setLayout(gridLayout);
        setBackground(new Color(175, 223, 228)); // 背景色
 
        JLabel datelabel = new JLabel("日付");
 
        String[] categorycombo = {
                "家賃", "水道", "ガス", "電気", "食費",
                "稽古", "化粧品", "外食費", "飲料", "娯楽費", "交際費", "趣味"
        };
        JComboBox<String> categorybox = new JComboBox<>(categorycombo);
 
        JLabel pricelabel = new JLabel("金額");
        JLabel memolabel = new JLabel("メモ");
 
        datelabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
        categorybox.setFont(new Font("MSゴシック", Font.BOLD, 18));
        pricelabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
        memolabel.setFont(new Font("MSゴシック", Font.BOLD, 18));
 
        datelabel.setHorizontalAlignment(JLabel.CENTER);
        pricelabel.setHorizontalAlignment(JLabel.CENTER);
        memolabel.setHorizontalAlignment(JLabel.CENTER);
 
        //  date は JFormattedTextField（数字＋ハイフン固定）
        JFormattedTextField dateField;
        try {
            MaskFormatter mf = new MaskFormatter("####-##-##"); // yyyy-MM-dd 形式
            mf.setPlaceholderCharacter('_');                    // 入力フォーマット
            dateField = new JFormattedTextField(mf);
            dateField.setColumns(10);
        } catch (java.text.ParseException pe) {
            throw new RuntimeException(pe);
        }
        // =====================================================
 
        // 任意入力の独自カテゴリ。空の場合はコンボ選択を使う
        JTextField categoryField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        JTextField memoField = new JTextField(20);
 
        // 見た目
        Font f = new Font("MS ゴシック", Font.BOLD, 20);
        for (JTextField ta : new JTextField[] { dateField, categoryField, priceField, memoField }) {
            ta.setPreferredSize(new Dimension(150, 30));
            ta.setFont(f);
        }
 
        add(datelabel);
        add(dateField);
        add(categorybox);
        add(categoryField);
        add(pricelabel);
        add(priceField);
        add(memolabel);
        add(memoField);
 
        // ボタン用パネル
        JPanel buttunface = new JPanel();
        buttunface.setBackground(new Color(175, 223, 228));
 
        JButton categoryButton = new JButton("カテゴリーを追加"); // （動作は未実装）
        JButton income = new JButton("収入として登録");
        JButton outcome = new JButton("支出として登録");
 
        buttunface.add(categoryButton);
        buttunface.add(income);
        buttunface.add(outcome);
 
        frame.setLocationRelativeTo(null); // 中央揃え
        frame.setSize(1000, 800);
        frame.add(buttunface, BorderLayout.SOUTH);
        add(buttunface);
        add(new JLabel()); // 左セルを空ける
        add(buttunface);   // パネルに表示
        frame.setVisible(true);
        setVisible(true);
        buttunface.setVisible(true);
        frame.pack();
 
        // 収入：最初に日付チェック → 保
        income.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
 
                // ★最初に：形式＋実在チェック（NGなら中断）
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !isValidYyyyMmDd(date)) {
                    JOptionPane.showMessageDialog(frame,
                            "日付は yyyy-MM-dd 形式かつ実在する日付で入力してください。",
                            "入力エラー", JOptionPane.ERROR_MESSAGE);
                    dateField.requestFocus();
                    return;
                }
 
                // カテゴリはテキスト優先。未入力ならコンボの選択を使う
                String categoryTyped = categoryField.getText().trim();
                String categorySelected = (String) categorybox.getSelectedItem();
                String category = categoryTyped.isEmpty() ? categorySelected : categoryTyped;
 
                String priceText = priceField.getText().trim();
                String memo = memoField.getText().trim();
                
		        
		        if (priceText.isEmpty()) { // 空白チェック
		            JOptionPane.showMessageDialog(frame, "金額を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        for (int i = 0; i < priceText.length(); i++) { // if 「0以上の整数」チェック
		            char c = priceText.charAt(i);
		            if (c < '0' || c > '9') {
		                JOptionPane.showMessageDialog(frame, "金額は0以上の整数で入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		        }
                // 金額
                BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);
 
                Household item = new Household(date, category, price, memo);
 
                // 既存読み込み
                List<Household> list = new ArrayList<>();
                if (Files.exists(DATA_PATH)) {
                    try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
                        List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
                        if (loaded != null) list = loaded;
                    }
                }
 
                // 追加
                list.add(item);
 
                // 親フォルダ
                Path parent = DATA_PATH.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
 
                // 保存
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
 
        //  支出：最初に日付チェック → 金額をマイナスにして保存
        outcome.addActionListener(e -> {
            try {
                String date = dateField.getText().trim();
 
                // ★最初に：形式＋実在チェック（NGなら中断）
                if (!date.matches("\\d{4}-\\d{2}-\\d{2}") || !isValidYyyyMmDd(date)) {
                    JOptionPane.showMessageDialog(frame,
                            "日付は yyyy-MM-dd 形式かつ実在する日付で入力してください。",
                            "入力エラー", JOptionPane.ERROR_MESSAGE);
                    dateField.requestFocus();
                    return;
                }
 
                String categoryTyped = categoryField.getText().trim();
                String categorySelected = (String) categorybox.getSelectedItem();
                String category = categoryTyped.isEmpty() ? categorySelected : categoryTyped;
 
                String priceText = priceField.getText().trim();
                String memo = memoField.getText().trim();
 
                if (priceText.isEmpty()) {//空白チェック
		            JOptionPane.showMessageDialog(frame, "金額を入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        for (int i = 0; i < priceText.length(); i++) {
		            char c = priceText.charAt(i);
		            if (c < '0' || c > '9') {
		                JOptionPane.showMessageDialog(frame, "金額は0以上の整数で入力してください。", "入力エラー", JOptionPane.ERROR_MESSAGE);
		                return;
		            }
		        }
		        
                BigDecimal price = priceText.isEmpty() ? BigDecimal.ZERO : new BigDecimal(priceText);
                BigDecimal expense = price.multiply(BigDecimal.valueOf(-1)); // 支出はマイナス
 
                Household item = new Household(date, category, expense, memo);
 
                // 既存読み込み
                List<Household> list = new ArrayList<>();
                if (Files.exists(DATA_PATH)) {
                    try (BufferedReader reader = Files.newBufferedReader(DATA_PATH, StandardCharsets.UTF_8)) {
                        List<Household> loaded = GSON.fromJson(reader, LIST_TYPE);
                        if (loaded != null) list = loaded;
                    }
                }
 
                // 追加
                list.add(item);
 
                // 親フォルダ
                Path parent = DATA_PATH.getParent();
                if (parent != null) {
                    Files.createDirectories(parent);
                }
 
                // 保存
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