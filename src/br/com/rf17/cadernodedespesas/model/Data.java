package br.com.rf17.cadernodedespesas.model;

import java.util.Date;

public class Data {

	private String descricao;
	private Date date;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	//Aqui esta a m�gica, sem este m�todo voc� ter� uma exce��o - http://aqueleblogdeandroid.blogspot.com.br/2011/02/utilizando-spinner-com-objetos.html
	public String toString() {  
		return (this.getDescricao());  
	}

}
