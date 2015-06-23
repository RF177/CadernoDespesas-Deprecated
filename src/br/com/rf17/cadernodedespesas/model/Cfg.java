package br.com.rf17.cadernodedespesas.model;

import java.io.Serializable;

public class Cfg implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private Long id_cfg;
	private String senha;
		
	public Cfg() {
		super();		
	}

	public Long getId_cfg() {
		return id_cfg;
	}

	public void setId_cfg(Long id_cfg) {
		this.id_cfg = id_cfg;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

}
