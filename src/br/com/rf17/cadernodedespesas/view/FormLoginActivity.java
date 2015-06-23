package br.com.rf17.cadernodedespesas.view;

import br.com.rf17.cadernodedespesas.R;
import br.com.rf17.cadernodedespesas.model.Cfg;
import br.com.rf17.cadernodedespesas.service.DadosSessao;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormLoginActivity extends Activity {
		
	private Button btn_form_login_ok;
	private EditText edit_form_login_password;
	
	public Cfg cfg;
	
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.form_login);
			
			edit_form_login_password = (EditText) findViewById(R.id.edit_form_login_password);
			
			btn_form_login_ok = (Button) findViewById(R.id.btn_form_login_ok); //Botao entrar/ok			
			btn_form_login_ok.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {					
					
					String senha = edit_form_login_password.getText().toString();
					
					if (DadosSessao.getConfig().getSenha().equals(senha)) {//Verifica se a senha foi digitada corretamente
						Intent i = new Intent(FormLoginActivity.this, MainActivity.class);
						startActivity(i);
						finish();
					} else {
						Toast.makeText(FormLoginActivity.this, "Senha incorreta!", Toast.LENGTH_LONG).show();
					}
				}
			});
			
		} catch (Exception e) {
			Toast.makeText(FormLoginActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * Variavel que define se o bd foi atualizado e se houveram erros na atualizacao
	 * e caso houver, abre um dialog na tela com a descricao do erro e nao deixa o usuario
	 * acessar o sistema.
	 * 
	 * Obs: Essa variavel é modificada na classe DataBaseHandler caso houver erros, e manda para cá
	 * a mensagem de erro que ocorreu, para mostrar na tela.
	 * 
	 */
	public static String erro_bd = null;
	
	/**
	 * Abre dialog de erros do update do bd
	 */
	public void dialogErroUpdate(String erro) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Erro grave ao atualizar banco de dados do aplicativo! (Motivo: "+erro+")");

		builder.setNeutralButton("Fechar", new DialogInterface.OnClickListener() {//Fecha app
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();// Fecha dialog
				finish();
				System.exit(0);//Fecha app
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();// Mostra dialog
	}
		
	//## Botao fisico voltar do android ##
	@Override
    public void onBackPressed() {//Botao de voltar do android
        AlertDialog.Builder builder = new AlertDialog.Builder(this);   	 
    	builder.setMessage("Você tem certeza que deseja sair do aplicativo?");
    	     	 
    	builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {    	
    	    public void onClick(DialogInterface dialog, int which) {// Code that is executed when clicking YES    	        
    	        dialog.dismiss();//Fecha dialog
    	        sair();//Sai do app
    	    }    	 
    	});    	     	 
    	builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int which) {// Code that is executed when clicking NO
    			dialog.dismiss();//Fecha dialog
    	    }    	 
    	});
        
        AlertDialog alert = builder.create();
    	alert.show();//Mostra dialog        
    }	
	public void sair(){//Sai do aplicativo
		super.onBackPressed();//Executa metodo nativo do botao voltar do android
	}
	//## Botao fisico voltar do android ##
	
}