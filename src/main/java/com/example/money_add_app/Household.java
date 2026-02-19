package com.example.money_add_app;
 
import java.math.BigDecimal;
import java.util.List;
 
 
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
	private String date; //日付
    private String category; //商品カテゴリー
    private BigDecimal price; // お金
    private String memo; //メモ
    public Household(  String date, String category, BigDecimal price, String memo) {
    	this.date =  date;
		this.category = category;
		this.price = price;
		this.memo = memo;
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
 
	public String getDate() {
		return date;
	}
 
	public void setDate(String date) {
		this.date = date;
	}
 
	public String getMemo() {
		return memo;
	}
 
	public void setMemo(String memo) {
		this.memo = memo;
	}
}