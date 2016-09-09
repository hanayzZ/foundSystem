package com.hanay.foundsystem.chat.bean;

import com.hanay.foundsystem.chat.commons.GlobalStrings;


public class SearchEntity {

	private static int LOWER_BOUNDRY_OF_AGE = 5;
	private static int UPPER_BOUNDRY_OF_AGE = 80;
	public static int FEMALE_GENDER = 0;
	public static int MALE_GENDER = 1;
	public static int BOTH_GENDER = 2;
	public static int SEARCH_BY_NAME = 0;
	public static int SEARCH_BY_ELSE = 1;

	private int searchType = SEARCH_BY_NAME;
	private int ageLower = LOWER_BOUNDRY_OF_AGE;
	private int ageUpper = UPPER_BOUNDRY_OF_AGE;
	private int sex = BOTH_GENDER;  // 0 for lady, 1 for guy, 10 for both
	private String name = "xx";

	public SearchEntity(int type, int lage, int uage, int sex00, String name00) {
		if(type == SEARCH_BY_NAME || type == SEARCH_BY_ELSE) {
			this.searchType = type;
		}
		if(lage >= LOWER_BOUNDRY_OF_AGE && lage <= UPPER_BOUNDRY_OF_AGE) {
			this.ageLower = lage;
		}
		if(uage >= LOWER_BOUNDRY_OF_AGE && uage <= UPPER_BOUNDRY_OF_AGE) {
			this.ageUpper = uage;
		}
		if(sex00 == FEMALE_GENDER || sex00 == MALE_GENDER || sex00 == BOTH_GENDER) {
			this.sex = sex00;
		}

		if(name00.length() > 0) {
			this.name = name00;
		}
	}

	public SearchEntity(String str0) {
		String[] strArr0 = str0.split(GlobalStrings.entityDivider);
		this.searchType = Integer.parseInt(strArr0[0]);
		this.ageLower = Integer.parseInt(strArr0[1]);
		this.ageUpper = Integer.parseInt(strArr0[2]);
		this.sex = Integer.parseInt(strArr0[3]);
		this.name = strArr0[4];
	}

	public String toString() {
		String str0 = this.searchType + GlobalStrings.entityDivider;
		str0 += this.ageLower + GlobalStrings.entityDivider;
		str0 += this.ageUpper + GlobalStrings.entityDivider;
		str0 += this.sex + GlobalStrings.entityDivider;
		str0 += this.name + GlobalStrings.entityDivider;
		return str0;
	}

	public int getType() {
		return searchType;
	}

	public int getLowerAge() {
		return ageLower;
	}

	public int getUpperAge() {
		return ageUpper;
	}

	public int getSex() {
		return sex;
	}

	public String getName() {
		return name;
	}

}