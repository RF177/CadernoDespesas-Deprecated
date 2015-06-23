package br.com.rf17.cadernodedespesas.view;

import br.com.rf17.cadernodedespesas.R;
import br.com.rf17.cadernodedespesas.dao.CfgDAO;
import br.com.rf17.cadernodedespesas.service.DadosSessao;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashScreen extends Activity {

	private static int SPLASH_TIME_OUT = 2000;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen);

		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (verificaSenhaCadastrada()) {
					Intent i = new Intent(SplashScreen.this, FormLoginActivity.class);
					startActivity(i);
					finish();						
				} else {//redireciona para tela de cadastro da senha
					Intent i = new Intent(SplashScreen.this, FormCfgActivity.class);
					startActivity(i);
					finish();
				}				
			}
		}, SPLASH_TIME_OUT);
	}
	
	/**
	 * Verifica se existe uma senha configurada no banco de dados
	 * 
	 * @return
	 */
	public boolean verificaSenhaCadastrada() {
		try{
			CfgDAO cfgDAO = new CfgDAO(this);
			cfgDAO.open();
			DadosSessao.setConfig(cfgDAO.GetConfiguracoes());
			cfgDAO.close();
			
			return DadosSessao.getConfig() == null ? false : true;
			
		} catch (Exception e) {
			Toast.makeText(SplashScreen.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return false;
		}		
	}

}