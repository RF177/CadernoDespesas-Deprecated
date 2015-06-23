package br.com.rf17.cadernodedespesas.util;

import java.math.RoundingMode;
import java.text.NumberFormat;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;

public class StringFunctions {

	/**
	 * Remove acentos do texto
	 * 
	 * @param sTexto
	 * @return
	 */
	public static String clearAccents(String sTexto) {
		
		if (sTexto == null )
			return "";
					
		char cVals[] = sTexto.toCharArray();
		
		for (int i = 0; i < cVals.length; i++) {
			cVals[i] = clearAccent(cVals[i]);
		}
		return new String(cVals);
	}
	
	/**
	 * Funcao para ser usada em uma pesquisa no banco de dados (sqlite)
	 * 
	 * @param nome_coluna (nome da coluna que vai ser pesquisado Ex.: 'razao')
	 * @return 
	 */
	public static String clearAccentsBancoDados(String nome_coluna){
		return " replace(replace(replace(replace(replace(replace(replace(replace(replace(replace(replace( "
			   + " lower("+nome_coluna+"), "
			   + " 'á','a'), 'ã','a'), 'â','a'), 'é','e'), 'ê','e'), 'í','i'), 'ó','o') ,'õ','o') ,'ô','o'),'ú','u'), 'ç','c') ";
	}
	
	/**
	 * Remove acento do caracter
	 * 
	 * @param char
	 * @return char
	 */
	public static char clearAccent(char cKey) {
		char cTmp = cKey;
		if (isContained(cTmp, "ãâáàä"))
			cTmp = 'a';
		else if (isContained(cTmp, "ÃÂÁÀÄ"))
			cTmp = 'A';
		else if (isContained(cTmp, "êéèë"))
			cTmp = 'e';
		else if (isContained(cTmp, "ÊÉÈË"))
			cTmp = 'E';
		else if (isContained(cTmp, "îíìï"))
			cTmp = 'i';
		else if (isContained(cTmp, "ÎÍÌÏ"))
			cTmp = 'I';
		else if (isContained(cTmp, "õôóòö"))
			cTmp = 'o';
		else if (isContained(cTmp, "ÕÔÓÒÖ"))
			cTmp = 'O';
		else if (isContained(cTmp, "ûúùü"))
			cTmp = 'u';
		else if (isContained(cTmp, "ÛÚÙÜ"))
			cTmp = 'U';
		else if (isContained(cTmp, "ç"))
			cTmp = 'c';
		else if (isContained(cTmp, "Ç"))
			cTmp = 'C';
		
		return cTmp;    
	}
	
	public static boolean isContained(char cTexto, String sTexto) {
		boolean bRetorno = false;
		for (int i = 0; i < sTexto.length(); i++) {
			if (cTexto == sTexto.charAt(i)) {
				bRetorno = true;
				break;
			}
		}
		return bRetorno;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    }	    
	    return true;// only got here if we didn't return false
	}
	
	public static boolean isLong(String s) {
	    try {
	    	Long.parseLong(s);	         
	    } catch(NumberFormatException e) { 
	        return false; 
	    }	    
	    return true;// only got here if we didn't return false
	}
	
	/**
     * Utilizado para formatar double para reais
     * 
     * @param double numero (Ex.: '12.95')
     * @return String formatada (Ex.: 'R$ 12,95') 
     */
	public static String getPrecoFormatado(double valor) {
		NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
		numberFormat.setMaximumFractionDigits(2);
		numberFormat.setMinimumFractionDigits(2);
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);
		return numberFormat.format(valor);		
	}
	
	/**
	 * Formata Double para String com virgulas e 2 casas decimais 
	 * 
	 * @param casasDecimais (Número de casas decimais maximais e minimas depois da virgula)
	 * @param valor numero (Ex.: '1212.95')
	 * @return String formatada (Ex.: '1.212,95' (se param casasDecimais igual a 2) 
	 */
	public static String formataDouble(double valor, int casasDecimais) {
		NumberFormat numberFormat = NumberFormat.getInstance();
		numberFormat.setMaximumFractionDigits(casasDecimais);
		numberFormat.setMinimumFractionDigits(casasDecimais);
		numberFormat.setRoundingMode(RoundingMode.HALF_UP);//Arrendodamento
		return numberFormat.format(valor);	    
	}
	
	/**
	 * Formata o ddd e o telefone em apenas uma string
	 * 
	 * 		Ex.: Ddd passado como parametro: 48
	 * 			 telefone passado como paramentro: 99188602	
	 * 			 entao irá retornar: (48) 9918-8602
	 * 
	 * @param ddd
	 * @param telefone
	 * @return telefoneFormatado (Ddd e telefone formatados em uma mesma string)
	 */
	public static String formataTelefone(Integer ddd, String telefone){
		String telefoneFormatado = "";
		
		if(ddd != null && ddd != 0){//Se tiver ddd
			telefoneFormatado += "("+ddd+") ";
		}
		
		if(telefone.length() == 8){//Telefone normal, com 8 digitos (Ex.: 9918-8602)
			telefoneFormatado += telefone.substring(0, 4) + "-" + telefone.substring(4, 8);
		}else if (telefone.length() == 9) {//Telefone com 9 digitos (Ex.: 99918-8602)
			telefoneFormatado += telefone.substring(0, 5) + " - " + telefone.substring(5, 9);
		}else if(telefone.length() == 11){//Telefone com 11 digitos (Ex.: 0800 729 0500)
			if(telefone.substring(0, 1).equalsIgnoreCase("0")){//Se começar com 0 (apenas 0800)
				telefoneFormatado += telefone.substring(0, 4) + " " + telefone.substring(4, 7) + " " + telefone.substring(7, 11);
			}else{//Se nao começar com 0, nao formata
				telefoneFormatado += telefone;
			}
		}else{//Se com outro tamanho de digitos, nao formata
			telefoneFormatado += telefone;
		}
		
		return telefoneFormatado;
	}
	
	/**
	 * Formata a inscricao (CNPJ ou CPF) que foi passada
	 * 
	 * 		Ex.: Inscricao passada como parametro: 06047418988	
	 * 			 entao irá retornar: 060.474.189-88
	 * 
	 * @param inscricao (CNPJ ou CPF)
	 * @return inscricao formatada
	 */
	public static String formataInscricao(String inscricao){
		if(inscricao.length() > 11){
			return new CNPJFormatter().format(inscricao);
		}else{
			return new CPFFormatter().format(inscricao);
		}
	}
	
}
