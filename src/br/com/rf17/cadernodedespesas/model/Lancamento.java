package br.com.rf17.cadernodedespesas.model;

import java.io.Serializable;
import java.util.Date;

public class Lancamento implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id_lancamento;
	//private int tipo;//1-Receita / 2-Despesa
	private Date data;
	private double valor;
	private String descricao;	

	public Lancamento() {
		super();
		data = new Date();
	}

	public Long getId_lancamento() {
		return id_lancamento;
	}

	public void setId_lancamento(Long id_lancamento) {
		this.id_lancamento = id_lancamento;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public double getValor() {
		return valor;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	/*
	//Aqui esta a mágica, sem este método você terá uma exceção - http://aqueleblogdeandroid.blogspot.com.br/2011/02/utilizando-spinner-com-objetos.html
	public String toString() {  
		return (this.getDescricao());  
	}  	
	*/
}
