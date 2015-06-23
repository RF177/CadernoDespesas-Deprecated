package br.com.rf17.cadernodedespesas.service;

import br.com.rf17.cadernodedespesas.model.Cfg;

public class DadosSessao {

	private static Cfg config;

	public static Cfg getConfig() {
		return config;
	}

	public static void setConfig(Cfg config) {
		DadosSessao.config = config;
	}

}
