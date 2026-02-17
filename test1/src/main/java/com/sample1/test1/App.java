package com.sample1.test1;

import java.io.FileWriter; // ファイル書き込みのためのインポート
import java.io.IOException; // 例外処理のためのインポート
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson; // Gsonライブラリのインポート
public class App {
    public static void main(String[] args) {
    	List<Household> householdList = new ArrayList<>();
    	Household household1 = new Household("house", BigDecimal.valueOf(1000));
    	Household household2 = new Household("house", BigDecimal.valueOf(3400));
        householdList.add(household1);
        householdList.add(household2);
    	
        // Gsonのインスタンスを作成
        Gson gson = new Gson(); // JSON変換用のGsonオブジェクト
        // JSONファイルに書き込む
        try (FileWriter writer = new FileWriter("householdList.json")) { // employee.jsonに書き込む
            gson.toJson(householdList, writer); // オブジェクトをJSON形式に変換して書き込む
        } catch (IOException e) {
            e.printStackTrace(); // エラーメッセージを表示
        }
    }
}

class HouseholdList{
	private List<Household> householdList;

	public List<Household> getHouseholdList() {
		return householdList;
	}

	public void setHouseholdList(List<Household> householdList) {
		this.householdList = householdList;
	}
	
	
}

class  Household{
    private String category; //商品カテゴリー
    private BigDecimal price; // お金
    
    public Household(String category, BigDecimal price) {
		this.category = category;
		this.price = price;
	}
    
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
    
}