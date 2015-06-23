package br.com.rf17.cadernodedespesas.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.net.ConnectivityManager;

public class TesteConexao {

	/**
	 * Verifica se aparelho esta conectado em alguma rede (rede movel ou wifi)
	 * 
	 * @param context
	 * @return boolean
	 */
	public static boolean isConectadoInternet(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {//Se conectado na rede movel
				return true;
			} else if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {//Se conectado no wifi
				return true;
			} else {//Se desconectado
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Verifica se URL est� online e se consegue conexao com ela
	 * 	(Usado para verificar se a sincronizacao ir� usar a url interna ou externa)
	 * 
	 * @param url
	 * @return
	 */
	public static boolean isURLConecta(String url) {
	    HttpURLConnection connection = null;
	    try {
	        connection = (HttpURLConnection) new URL(url).openConnection();//Abre a conexao com a url
	        connection.setConnectTimeout(10000);//Tempo em ms (milisegundos) que vai tentar realizar a conex�o
	        connection.connect();//Conecta na url	        
	        return true;	        
	    } catch (IOException exception) {//Se nao conseguir conectar, d� uma exception e retorna false
	    	return false;
	    }finally{
	    	if (connection != null) {//Se conexao ainda conectada 
	    		connection.disconnect();//desconecta
	        }
	    }
	    
	}

	/*
	public static boolean Conectado(Context context) {
		try {
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			// String LogSync = null;
			// String LogToUserTitle = null;
			if (cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected()) {
				// LogSync += "\nConectado a Internet 3G ";
				// LogToUserTitle += "Conectado a Internet 3G ";
				// handler.sendEmptyMessage(0);
				// Log.d(TAG,"Status de conex�o 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
				return true;
			} else if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected()) {
				// LogSync += "\nConectado a Internet WIFI ";
				// LogToUserTitle += "Conectado a Internet WIFI ";
				// handler.sendEmptyMessage(0);
				// Log.d(TAG,"Status de conex�o Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
				return true;
			} else {
				// LogSync += "\nN�o possui conex�o com a internet ";
				// LogToUserTitle += "N�o possui conex�o com a internet ";
				// handler.sendEmptyMessage(0);
				// Log.e(Tag,"Status de conex�o Wifi: "+cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected());
				// Log.e(TAG,"Status de conex�o 3G: "+cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnected());
				return false;
			}
		} catch (Exception e) {
			// Log.e(Tag,e.getMessage());
			return false;
		}
	}
	*/
}
