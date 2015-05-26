package com.science.strangertofriend.bean;

/**
 * @description
 * 
 * @author 幸运Science 陈土燊
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @2015-5-19
 * 
 */

public class SortModel {

	private String name;// 显示的数据
	private String sortLetters;// 显示数据拼音的首字母
	private String avaterUrl;// 头像URL

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getAvaterUrl() {
		return avaterUrl;
	}

	public void setAvaterUrl(String avaterUrl) {
		this.avaterUrl = avaterUrl;
	}

}
