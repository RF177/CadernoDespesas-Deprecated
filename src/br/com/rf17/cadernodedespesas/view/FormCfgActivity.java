package br.com.rf17.cadernodedespesas.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import br.com.rf17.cadernodedespesas.R;
import br.com.rf17.cadernodedespesas.dao.CfgDAO;
import br.com.rf17.cadernodedespesas.dao.db.DataBaseHandler;
import br.com.rf17.cadernodedespesas.model.Cfg;
import br.com.rf17.cadernodedespesas.service.DadosSessao;
import android.net.ParseException;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FormCfgActivity extends Activity {
		
	private Button btn_form_config_voltar, btn_form_config_salvar, btn_form_config_salvar_dados, btn_form_config_restaurar_dados;
	private EditText edit_form_config_senha, edit_form_config_confirm_senha;
	private TextView textView_form_config_msg_cadastro_senha;
	
	private Context context = this;
	
	private CfgDAO cfgDAO = new CfgDAO(this);
	
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.form_cfg);
			
			btn_form_config_voltar = (Button) findViewById(R.id.btn_form_config_voltar);
			btn_form_config_salvar = (Button) findViewById(R.id.btn_form_config_salvar);
			btn_form_config_salvar_dados = (Button) findViewById(R.id.btn_form_config_salvar_dados);
			btn_form_config_restaurar_dados = (Button) findViewById(R.id.btn_form_config_restaurar_dados);
			
			edit_form_config_senha = (EditText) findViewById(R.id.edit_form_config_senha);
			edit_form_config_confirm_senha = (EditText) findViewById(R.id.edit_form_config_confirm_senha);
			
			textView_form_config_msg_cadastro_senha = (TextView) findViewById(R.id.textView_form_config_msg_cadastro_senha);
			
			if (DadosSessao.getConfig() == null) {
				btn_form_config_voltar.setVisibility(View.GONE);//Esconde botao voltar
				btn_form_config_salvar_dados.setVisibility(View.GONE);//Esconde botao voltar
				btn_form_config_restaurar_dados.setVisibility(View.GONE);//Esconde botao voltar
			} else {
				textView_form_config_msg_cadastro_senha.setVisibility(View.GONE);//Esconde a msg de cadastro de senha
				
				btn_form_config_voltar.setOnClickListener(new View.OnClickListener() { //Botao voltar
					public void onClick(View v) {
						Intent i = new Intent(FormCfgActivity.this, MainActivity.class);
			        	startActivity(i);	
			        	finish();
					}
				});
				
				btn_form_config_salvar_dados = (Button) findViewById(R.id.btn_form_config_salvar_dados); //Botao salvar dados			
				btn_form_config_salvar_dados.setOnClickListener(new View.OnClickListener() {
					@SuppressWarnings("resource")
					public void onClick(View v) {
						try {
							if (Environment.getExternalStorageDirectory().canWrite()) {// Verifica se existe permissao de escrita/write
								final File currentDB = getDatabasePath("CadernoDeDespesas_DATABASE");
								File backupDB = new File(Environment.getExternalStorageDirectory(), "/download/CadernoDeDespesas_database_backup.db");

								FileChannel src = new FileInputStream(currentDB).getChannel();
								FileChannel dst = new FileOutputStream(backupDB).getChannel();
								dst.transferFrom(src, 0, src.size());
								src.close();
								dst.close();

								Toast.makeText(getApplicationContext(), "Backup do banco de dados criado com sucesso! O arquivo foi salvo dentro da pasta 'download' do seu aparelho, com o nome de 'CadernoDeDespesas_database_backup.db'", Toast.LENGTH_LONG).show();
							} else {
								throw new Exception("O aplicativo não conseguiu obter permissão para salvar o backup dos dados");
							}
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(), "Erro ao realizar backup do banco de dados! (Motivo: "+e.getMessage()+")", Toast.LENGTH_LONG).show();
						}
					}
				});
	
				btn_form_config_restaurar_dados = (Button) findViewById(R.id.btn_form_config_restaurar_dados); //Botao restaurar dados			
				btn_form_config_restaurar_dados.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						dialogConfirmaImport();//Abre dialog para confirmar
					}
				});				
				
			}	
			
			btn_form_config_salvar = (Button) findViewById(R.id.btn_form_config_salvar); //Botao salvar			
			btn_form_config_salvar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try {
						submitForm();
					} catch (ParseException e) {
						Toast.makeText(FormCfgActivity.this, "ERRO!", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			});
			
		} catch (Exception e) {
			Toast.makeText(FormCfgActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	private void submitForm() throws ParseException {
		try{
			if(!edit_form_config_senha.getText().toString().equals(edit_form_config_confirm_senha.getText().toString())){
				throw new Exception("Senhas informadas diferentes!");
			}
			
			DadosSessao.setConfig(new Cfg());
			
			DadosSessao.getConfig().setSenha(edit_form_config_senha.getText().toString());
			
			cfgDAO.open();
			
			// ##SALVA NO BD
			if (DadosSessao.getConfig().getId_cfg() == null) {
				DadosSessao.getConfig().setId_cfg(cfgDAO.insert(DadosSessao.getConfig()));//Novo					
			} else {
				cfgDAO.update(DadosSessao.getConfig());// Atualiza
			}
			// ##SALVA NO BD

			cfgDAO.close();

			Toast.makeText(FormCfgActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
			
			Intent i = new Intent(FormCfgActivity.this, MainActivity.class);
			startActivity(i);
			finish();
			
		} catch (Exception e) {
			Toast.makeText(FormCfgActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
		
	/**
	 * Abre dialog para confirmar import do bd
	 */
	public void dialogConfirmaImport() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Você tem certeza que deseja importar/restaurar o banco de dados? (Todas as informações salvas localmente, serão perdidas) ");

		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {// Se clicar em SIM, exclui o pedido
				dialog.dismiss();// Fecha dialog

				try {
					DataBaseHandler dbHelper = new DataBaseHandler(context);
				    SQLiteDatabase database = dbHelper.getWritableDatabase();//Open
				    
				    String db_local = database.getPath();
				    String db_import = Environment.getExternalStorageDirectory().getPath()+"/download/CadernoDeDespesas_database_backup.db";
				    
					if(dbHelper.importDatabase(db_local, db_import)){
				        Toast.makeText(FormCfgActivity.this, "Importação/Restauração do banco de dados realizado com sucesso!", Toast.LENGTH_LONG).show();
				    }else{
				    	Toast.makeText(FormCfgActivity.this, "Arquivo para ser restaurado não encontrado! O arquivo deve estar localizado dentro da pasta 'download' do seu aparelho, com o nome de 'bkp.db'", Toast.LENGTH_LONG).show();
					}							
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(FormCfgActivity.this, "Erro ao realizar importação/restauração do banco de dados! (Motivo: "+e.getMessage()+")", Toast.LENGTH_LONG).show();
				}
			}
		});
		builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {// Se clicar em NAO, nao faz nada
				dialog.dismiss();// Fecha dialog
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