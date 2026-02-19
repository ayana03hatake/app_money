package com.example.money_add_app;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class Main extends JFrame implements ActionListener{

    JPanel cardPanel;
    CardLayout layout;

    public static void main(String[] args) {
        Main frame = new Main();
        frame.setTitle("家計簿アプリ");
        frame.setSize(900, 600);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Main() {
        
        // Home画面
        HomePanel homePanel = new HomePanel(this);
        

        // 入力画面
        InputPanel inputPanel = new InputPanel(this);
        inputPanel.setBackground(Color.DARK_GRAY);
        

        // 一覧画面
        DetailPanel detailPanel = new DetailPanel(this);
        detailPanel.setBackground(Color.LIGHT_GRAY);
        
      
        // CardLayout用パネル
        cardPanel = new JPanel();
        layout = new CardLayout();
        cardPanel.setLayout(layout);

        cardPanel.add(homePanel, "homePanel");
        cardPanel.add(inputPanel, "inputPanel");
        cardPanel.add(detailPanel, "detailPanel");

        // カード移動用ボタン
        JButton firstButton = new JButton("Home");
        firstButton.addActionListener(this);
        firstButton.setActionCommand("homePanel");

        JButton secondButton = new JButton("入力");
        secondButton.addActionListener(this);
        secondButton.setActionCommand("inputPanel");

        JButton thirdButton = new JButton("一覧");
        thirdButton.addActionListener(this);
        thirdButton.setActionCommand("detailPanel");

        JPanel btnPanel = new JPanel();
        btnPanel.add(firstButton);
        btnPanel.add(secondButton);
        btnPanel.add(thirdButton);
     
        // cardPanelとカード移動用ボタンをJFrameに配置
        Container contentPane = getContentPane();
        contentPane.add(cardPanel, BorderLayout.CENTER);
        contentPane.add(btnPanel, BorderLayout.PAGE_END);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        layout.show(cardPanel, cmd);
    }
}


