package com.vmcomms.ptemagic.service.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for generating random Strings.
 */
public final class DateUtil {
	public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
	
	/**
	 * @return yyyyMM
	 */
    public static String currentMonth() {
    	SimpleDateFormat mdyFormat = new SimpleDateFormat("yyyyMM");
		return mdyFormat.format(new Date());
    }
	
	public static String date2str(Date date, String format){
		SimpleDateFormat mdyFormat = new SimpleDateFormat("dd/MM/yyyy");
		return mdyFormat.format(date);
	}
	
	public static String date2str(Date date){
		SimpleDateFormat mdyFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
		return mdyFormat.format(date);
	}
}
