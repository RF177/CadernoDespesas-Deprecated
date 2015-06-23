package br.com.rf17.cadernodedespesas.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.rf17.cadernodedespesas.R;
import br.com.rf17.cadernodedespesas.dao.CfgDAO;
import br.com.rf17.cadernodedespesas.dao.LancamentoDAO;
import br.com.rf17.cadernodedespesas.model.Data;
import br.com.rf17.cadernodedespesas.model.Lancamento;
import br.com.rf17.cadernodedespesas.util.StringFunctions;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
		
	private LancamentoDAO lancamentoDAO = new LancamentoDAO(this);
	private CfgDAO cfgDAO = new CfgDAO(this);
	
	private List<Lancamento> lancamentos;		
	
	private Button btn_main_novo_lancamento;	
	private LinearLayout linear_listview_lancamentos;	
	private TextView saldo;
	private Spinner spinner_main_periodo;
	
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.main);
	
			btn_main_novo_lancamento = (Button) findViewById(R.id.btn_main_novo_lancamento); 
			spinner_main_periodo = (Spinner) findViewById(R.id.spinner_main_periodo);
			linear_listview_lancamentos = (LinearLayout) findViewById(R.id.linear_listview_lancamentos);
			saldo = (TextView) findViewById(R.id.saldo);
			
			saldo.setText(" <- Adicione uma despesa");
			
			cfgDAO.open();
			List<Data> list_periodo = cfgDAO.ListMonths();
			cfgDAO.close();
			ArrayAdapter<Data> dataAdapter = new ArrayAdapter<Data>(this, R.layout.spinner_item, list_periodo);
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner_main_periodo.setAdapter(dataAdapter);
			
									
			btn_main_novo_lancamento.setOnClickListener(new View.OnClickListener() {//Botao nova despesa
				public void onClick(View v) {
					FormLancamentoActivity.lancamento = new Lancamento();
					Intent i = new Intent(MainActivity.this, FormLancamentoActivity.class);
		        	startActivity(i);	
		        	finish();
				}
			});	
			
			/*
			imgbtn_main_cfg = (ImageButton) findViewById(R.id.imgbtn_main_cfg); //Botao cfg			
			imgbtn_main_cfg.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, FormCfgActivity.class);
		        	startActivity(i);	
		        	finish();
				}
			});
			*/
			
			spinner_main_periodo.setOnItemSelectedListener(new OnItemSelectedListener() {//Listener periodo
			    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {//Quando periodo selecionado
			    	Data data = (Data) spinner_main_periodo.getSelectedItem();
			    	filtrar(data.getDate());//Filtra pelo periodo/mes selecionado
			    }
			    public void onNothingSelected(AdapterView<?> parentView) { }//Quando periodo nao for selecionado
			});
			
		} catch (Exception e){
			
		}
	}
	
	@SuppressLint("InflateParams")
	public void filtrar(Date date){
		try{
			
			lancamentoDAO.open();
			lancamentos = lancamentoDAO.ListAll(date);
			lancamentoDAO.close();
			
			linear_listview_lancamentos.removeAllViews();//Remove a lista antiga da tela/view, para criar novamente
			
			double saldo_total = 0.0;
			
			for(Lancamento lancamento : lancamentos){			
				LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View mLinearView = inflater.inflate(R.layout.main_row, null);//Pega layout/view 
				mLinearView.setId(lancamento.getId_lancamento().intValue());//Define a id que cada linha/row irá ter
	
				TextView main_row_valor = (TextView) mLinearView.findViewById(R.id.main_row_valor);
				TextView main_row_descricao = (TextView) mLinearView.findViewById(R.id.main_row_descricao);
				TextView main_row_data = (TextView) mLinearView.findViewById(R.id.main_row_data);
				
				main_row_valor.setText(StringFunctions.getPrecoFormatado(lancamento.getValor()));//Linha que mostra o valor			
				main_row_descricao.setText(lancamento.getDescricao());//Linha que mostra a descricao
				main_row_data.setText("Data: " + new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(lancamento.getData()));//Linha que mostra a data				
				
				saldo_total += lancamento.getValor();
				
				linear_listview_lancamentos.addView(mLinearView);;//Adiciona a lista na tela/view novamente
						 			
				mLinearView.setOnClickListener(new OnClickListener() {//Quando uma row selecionado
					@Override
					public void onClick(View v) {	
						Intent intent = new Intent(MainActivity.this, FormLancamentoActivity.class); 		
					    intent.putExtra("ID", ""+mLinearView.getId());//Passa id do lancamento selecionado, para pesquisar no bd antes de mostrar na tela
					    startActivity(intent);
					    finish();								
					}
				});
			}
			
			if(saldo_total != 0.0){
				saldo.setText("Total: " + StringFunctions.getPrecoFormatado(saldo_total));
			}
			
		} catch (Exception e) {
			Toast.makeText(MainActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean onKeyDown(int keycode, KeyEvent e) {
	    switch(keycode) {
	        case KeyEvent.KEYCODE_MENU:
	        	Log.v("", "AQUIIIIIIIIIIIIIII");
	        	Intent i = new Intent(MainActivity.this, FormCfgActivity.class);
	        	startActivity(i);	
	        	finish();
	            return true;
	    }

	    return super.onKeyDown(keycode, e);
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