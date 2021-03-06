package br.com.rf17.cadernodedespesas.util;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DataUtils {
	
	public static String setDateBd(Date date) {	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
		return dateFormat.format(date);
	}
		
	public static Date getDateBd(String date) throws ParseException {
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());		
			return dateFormat.parse(date);
		} catch (ParseException e) {		
			throw e;
		}
	}
		
	public static String setDateTimeBd(Date date) {	
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//
		return dateFormat.format(date);
	}
		
	public static Date getDateTimeBd(String date) throws ParseException {
		try {
		    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());		
			return dateFormat.parse(date);
		} catch (ParseException e) {		
			throw e;
		}		
	}
	
	/**
	 * Calcula o numero/intervalo de dias entre as 2 datas
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int intervaloDias (Date d1, Date d2) { 
		int result = (int)((d1.getTime() - d2.getTime()) / 86400000L); 
		return result; 
	}
	
	/** 
     * Converte uma String para um objeto Date. Caso a String seja vazia ou nula,  
     * retorna null - para facilitar em casos onde formul�rios podem ter campos 
     * de datas vazios. 
     * @param data String no formato dd/MM/yy a ser formatada 
     * @return Date Objeto Date ou null caso receba uma String vazia ou nula 
     * @throws Exception Caso a String esteja no formato errado 
     */  
    @SuppressLint("SimpleDateFormat")
	public static Date stringToDate(String data) {   
        if (data == null || data.equals(""))  
            return null;  
                  
        Date date = null;  
        try {  
        	DateFormat formatter = new SimpleDateFormat("dd/MM/yy");  
        	date = (java.util.Date)formatter.parse(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}            
        return date;  
    }
    
    /**
     * Converte Date para string, j� formatada
     * 
     * @param date
     * @return
     */
	@SuppressLint("SimpleDateFormat")
	public static String dateToString(Date date) {   
        if (date == null)  
            return null;  
        
        DateFormat df = new SimpleDateFormat("dd/MM/yy");
        return df.format(date);
    } 
}