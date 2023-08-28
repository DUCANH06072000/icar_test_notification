package com.tatv.baseapp.utils.tpms;

import android.content.Context;
import android.util.Log;

import com.tatv.baseapp.helper.AppFinal;
import com.tatv.baseapp.data.shared.BaseSharedPreference;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class TireUtil {
	private BaseSharedPreference pref;

	public TireUtil(Context context) {
		pref = new BaseSharedPreference(context);
	}

	public String getTempValue(int value) {
		int type = pref.getTempUnit();
		return type == 0 ? Integer.toString(value) : Integer.toString((int) (32 + value * 1.8));
	}

	public int getTemp(int value) {
		int type = pref.getTempUnit();
		return type == 0 ? value : (int) (32 + value * 1.8);
	}



	public String getTempUnit() {
		int type = pref.getTempUnit();
		return type == 0 ? "°C" : "°F";
	}



	public String getPressValue(float value){
		int type = pref.getPressUnit();
		double v2;
		switch (type){
			case AppFinal.ConfigParams.PSI:
				v2 = value * 14.5;
				break;
			case AppFinal.ConfigParams.KPA:
				v2 = value * 100.0;
				return  String.valueOf(Math.round(v2));
			case AppFinal.ConfigParams.BAR:
			default:
				v2 = value * 1.0;
				break;
		}
		DecimalFormat df = new DecimalFormat("0",
				DecimalFormatSymbols.getInstance(Locale.ENGLISH));
		df.setMaximumFractionDigits(1);
		return df.format(v2);
	}

	public float getPress(float value){
		int type = pref.getPressUnit();
		double v2;
		switch (type){
			case AppFinal.ConfigParams.PSI:
				v2 = value * 14.5;
				break;
			case AppFinal.ConfigParams.KPA:
				v2 = value * 100.0;
				break;
			case AppFinal.ConfigParams.BAR:
			default:
				v2 = value * 1.0;
				break;
		}
		return (float) v2;
	}


	public String getPressUnit(){
		int type = pref.getPressUnit();
		switch (type){
			case AppFinal.ConfigParams.PSI:
				return  "Psi";
			case AppFinal.ConfigParams.KPA:
				return  "Kpa";
			case AppFinal.ConfigParams.BAR:
			default:
				return "Bar";
		}
	}

	/**
	 * Kiểm tra cảnh báo áp suất lốp
	 * */
	public boolean isTirePressWarning(float tirePress){
		return tirePress != 0 &&  tirePress < pref.getPressMin() || tirePress > pref.getPressMax();
	}

	public boolean isTirePressHighWarning(float tirePress){
		return tirePress != 0 && tirePress > pref.getPressMax();
	}

	public boolean isTirePressLowWarning(float tirePress){
		return tirePress != 0 &&  tirePress < pref.getPressMin();
	}

	/**
	 * Kiểm tra cảnh báo nhiệt độ lốp
	 * */
	public boolean isTireTempWarning(int tireTemp){
		return tireTemp != 0 && tireTemp > pref.getTempMax();
	}
}
