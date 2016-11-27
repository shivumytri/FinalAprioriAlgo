package com.bms.pm.apriwithpart.datadiscretizaton;

public class DataDiscretizationUtils {

	public String getDesignation(String token) {

		String result;

		if ("Prof".equals(token)) {
			result = "1";
		} else if ("Asop".equals(token)) {
			result = "2";
		} else if ("Assp".equals(token)) {
			result = "3";
		} else {
			result = "null";
		}
		return result;
	}

	public String getDepartment(String token) {
		
		String result;
		
		if (token.equals("CSE")) {
			result = "4";
		} else if (token.equals("ECE") || token.equals("EC")) {
			result = "5";
		} else if (token.equals("EEE") || token.equals("EE")) {
			result = "6";
		} else if (token.equals("IT") || token.equals("IIC")) {
			result = "7";
		} else if (token.equals("ISE")) {
			result = "8";
		} else if (token.equals("TCE")) {
			result = "9";
		} else if (token.equals("MBA")) {
			result = "10";
		} else if (token.equals("ME")) {
			result = "11";
		} else if (token.equals("MECH")) {
			result = "12";
		} else if (token.equals("CIVIL") || token.equals("CE")) {
			result = "13";
		} else if (token.equals("IEM")) {
			result = "14";
		} else if (token.equals("ARCH")) {
			result = "15";
		} else if (token.equals("INST")) {
			result = "16";
		} else if (token.equals("CHEMICAL")) {
			result = "17";
		} else if (token.equals("BIOTECH")) {
			result = "18";
		} else if (token.equals("MCA")) {
			result = "19";
		} else if (token.equals("MNGS")) {
			result = "20";
		} else if (token.equals("IPE") || token.equals("IPM")) {
			result = "21";
		} else if (token.equals("AE")) {
			result = "22";
		} else if (token.equals("CTM") || token.equals("CIM")) {
			result = "23";
		} else if (token.equals("PST")) {
			result = "24";
		} else {
			result = "null";
		}
		return result;
	}

	public String getPGDegree(String token) {

		String result;
		if (token.equals("YES")) {
			result = "25";
		} else if (token.equals("NO")) {
			result = "26";
		} else {
			result = "null";
		}
		return result;
	}

	public String getPhDDegree(String token) {

		String result;

		if (token.equals("YES")) {
			result = "27";
		} else if (token.equals("NO")) {
			result = "28";
		} else {
			result = "null";
		}
		return result;
	}

	public String getUGClass(String token) {

		String result = null;

		if (token.equals("S")) {
			result = "29";
		} else if (token.equals("F")) {
			result = "30";
		} else if (token.equals("D")) {
			result = "31";
		} else if (token.equals("NA")) {
			result = "32";
		} else {
			result = "null";
		}
		return result;
	}

	public String getPGClass(String token) {

		String result = null;

		if (token.equals("S")) {
			result = "33";
		} else if (token.equals("F")) {
			result = "34";
		} else if (token.equals("D")) {
			result = "35";
		} else if (token.equals("NA")) {
			result = "36";
		} else {
			result = "null";
		}
		return result;
	}

	public String getTeachingExp(double value) {

		String result = null;

		if (value >= 0 && value < 1) {
			result = "37";
		} else if (value >= 1 && value <= 5) {
			result = "38";
		} else if (value >= 5 && value <= 10) {
			result = "39";
		} else if (value >= 10) {
			result = "40";
		} else {
			result = "null";
		}

		return result;
	}

	public String getIndustryExp(double value) {

		String result = null;

		if (value == 0 || value < 1) {
			result = "41";
		} else if (value >= 1 && value <= 6) {
			result = "42";
		} else if (value >= 6 && value <= 10) {
			result = "43";
		} else if (value >= 11) {
			result = "44";
		} else {
			result = "null";
		}

		return result;
	}

	public String getResearchExp(double value) {

		String result = null;

		if (value >= 0 && value < 1) {
			result = "45";
		} else if (value >= 1 && value <= 5) {
			result = "46";
		} else if (value >= 6 && value <= 10) {
			result = "47";
		} else if (value >= 11) {
			result = "48";
		} else {
			result = "null";
		}

		return result;
	}

	public String getNatPapPub(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "49";
		} else if (value >= 1 && value <= 5) {
			result = "50";
		} else if (value >= 6 && value <= 10) {
			result = "51";
		} else if (value >= 11) {
			result = "52";
		} else {
			result = "null";
		}
		return result;
	}

	public String getIntPapPub(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "53";
		} else if (value >= 1 && value <= 5) {
			result = "54";
		} else if (value >= 6 && value <= 10) {
			result = "55";
		} else if (value >= 11) {
			result = "56";
		} else {
			result = "null";
		}
		return result;
	}

	public String getNatPapConf(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "57";
		} else if (value >= 1 && value <= 5) {
			result = "58";
		} else if (value >= 6 && value <= 10) {
			result = "59";
		} else if (value >= 11) {
			result = "60";
		} else {
			result = "null";
		}

		return result;
	}

	public String getIntPapConf(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "61";
		} else if (value >= 1 && value <= 5) {
			result = "62";
		} else if (value >= 6 && value <= 10) {
			result = "63";
		} else if (value >= 11) {
			result = "64";
		} else {
			result = "null";
		}

		return result;
	}

	public String getPhDInstitute(String token) {

		String result = null;
		if (token.equals("3")) {
			result = "65";
		} else if (token.equals("1")) {
			result = "66";
		} else if (token.equals("2")) {
			result = "67";
		} else if (token.equals("NA")) {
			result = "68";
		} else {
			result = "null";
		}

		return result;
	}

	public String getNoScholarWorking(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "69";
		} else if (value >= 1 && value <= 5) {
			result = "70";
		} else if (value >= 6) {
			result = "71";
		} else {
			result = "null";
		}
		return result;
	}

	public String getUGGuidance(String token) {

		String result = null;

		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "72";
		} else if (value >= 1 && value <= 5) {
			result = "73";
		} else if (value >= 6) {
			result = "74";
		} else {
			result = "null";
		}

		return result;
	}

	public String getPGGuidance(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value >= 0 && value <= 10) {
			result = "75";
		} else if (value >= 10 && value <= 20) {
			result = "76";
		} else if (value >= 21) {
			result = "77";
		} else {
			result = "null";
		}

		return result;
	}

	public String getNoofBooksPublished(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "78";
		} else if (value >= 1 && value <= 5) {
			result = "79";
		} else if (value >= 6) {
			result = "80";
		} else {
			result = "null";
		}

		return result;
	}

	public String getPatent(String token) {

		String result = null;
		int value = Integer.parseInt(token);
		if (value == 0) {
			result = "81";
		} else if (value >= 1 && value <= 5) {
			result = "82";
		} else if (value >= 6) {
			result = "83";
		} else {
			result = "null";
		}

		return result;
	}

	public String getProfMembership(String token) {

		String result = null;
		if (token.equals("YES")) {
			result = "84";
		} else if (token.equals("NO")) {
			result = "85";
		} else {
			result = "null";
		}
		return result;
	}

	public String getConsultancyActivity(String token) {

		String result = null;
		if (token.equals("YES")) {
			result = "86";
		} else if (token.equals("NO")) {
			result = "87";
		} else {
			result = "null";
		}
		return result;
	}

	public String getAwards(String token) {

		String result = null;
		if (token.equals("YES")) {
			result = "88";
		} else if (token.equals("NO")) {
			result = "89";
		} else {
			result = "null";
		}
		return result;
	}

	public String getGrantFetched(String token) {
		String result = null;
		if (token.equals("YES")) {
			result = "90";
		} else if (token.equals("NO")) {
			result = "91";
		} else {
			result = "null";
		}
		return result;
	}

	public String getInteractionWithProfInstitute(String token) {

		String result = null;
		if (token.equals("YES")) {
			result = "92";
		} else if (token.equals("NO")) {
			result = "93";
		} else {
			result = "null";
		}
		return result;
	}

	public String getCollege(String token) {

		String result = null;
		if (token.equals("AIT")) {
			result = "94";
		} else if (token.equals("BMSCE")) {
			result = "95";
		} else if (token.equals("Gayatri")) {
			result = "96";
		} else if (token.equals("KLSGIT")) {
			result = "97";
		} else if (token.equals("NIE")) {
			result = "98";
		} else if (token.equals("NMIT")) {
			result = "99";
		} else if (token.equals("RVCE")) {
			result = "100";
		} else if (token.equals("SIT")) {
			result = "101";
		} else if (token.equals("SJC")) {
			result = "102";
		} else if (token.equals("TKM-KOLAM")) {
			result = "103";
		} else {
			result = "null";
		}
		return result;
	}

}
