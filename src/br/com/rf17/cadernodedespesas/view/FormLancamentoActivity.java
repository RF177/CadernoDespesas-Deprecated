package br.com.rf17.cadernodedespesas.view;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import br.com.rf17.cadernodedespesas.R;
import br.com.rf17.cadernodedespesas.dao.LancamentoDAO;
import br.com.rf17.cadernodedespesas.model.Lancamento;
import br.com.rf17.cadernodedespesas.util.StringFunctions;
import br.com.rf17.cadernodedespesas.util.Validation;
import android.net.ParseException;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

public class FormLancamentoActivity extends Activity {
		
	private Button btn_form_lancamento_voltar, btn_form_lancamento_salvar, btn_form_lancamento_excluir;
	private TextView edit_form_lancamento_valor, edit_form_lancamento_data, edit_form_lancamento_descricao;
	
	public static Lancamento lancamento;
	
	private LancamentoDAO lancamentoDAO = new LancamentoDAO(this);
	
	protected void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);
			setContentView(R.layout.form_lancamento);

			btn_form_lancamento_voltar = (Button) findViewById(R.id.btn_form_config_voltar);
			btn_form_lancamento_salvar = (Button) findViewById(R.id.btn_form_config_salvar);
			btn_form_lancamento_excluir = (Button) findViewById(R.id.btn_form_lancamento_excluir);
			
			edit_form_lancamento_valor = (TextView) findViewById(R.id.edit_form_config_senha);
			edit_form_lancamento_data = (TextView) findViewById(R.id.edit_form_config_confirm_senha);
			edit_form_lancamento_descricao = (TextView) findViewById(R.id.edit_form_lancamento_descricao);
			
			String ID = getIntent().getStringExtra("ID");//ID que vem do mainActivity
			
			if (ID != null) {//Editar
				//novo = false;
				lancamentoDAO.open();
				lancamento = lancamentoDAO.getById(Integer.parseInt(ID));//Busca lancamento no banco de dados
				lancamentoDAO.close();
			} else {//Novo
				//novo = true;				
			}
			
			/** Pega a data atual */
			final Calendar cal = Calendar.getInstance();
			pYear = cal.get(Calendar.YEAR);
			pMonth = cal.get(Calendar.MONTH);
			pDay = cal.get(Calendar.DAY_OF_MONTH);
			updateDisplay();// Mostra a data atual no TextView
			
			btn_form_lancamento_voltar = (Button) findViewById(R.id.btn_form_config_voltar); //Botao voltar			
			btn_form_lancamento_voltar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(FormLancamentoActivity.this, MainActivity.class);
		        	startActivity(i);	
		        	finish();
				}
			});	
			
			btn_form_lancamento_salvar = (Button) findViewById(R.id.btn_form_config_salvar); //Botao salvar			
			btn_form_lancamento_salvar.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					try {
						submitForm();
					} catch (ParseException e) {
						Toast.makeText(FormLancamentoActivity.this, "ERRO!", Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			});
			
			btn_form_lancamento_excluir = (Button) findViewById(R.id.btn_form_lancamento_excluir); //Botao excluir			
			btn_form_lancamento_excluir.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent i = new Intent(FormLancamentoActivity.this, MainActivity.class);
		        	startActivity(i);	
		        	finish();
				}
			});
			
			edit_form_lancamento_data.setOnClickListener(new View.OnClickListener() {//Campo data
				@SuppressWarnings("deprecation")
				public void onClick(View v) {
					showDialog(DATE_DIALOG_ID);
				}
			});
			
			Log.v("", lancamento.getId_lancamento()+"");
			
			if (lancamento.getId_lancamento() == null) {
				btn_form_lancamento_excluir.setVisibility(View.GONE);//Esconde botao excluir lancamento
				edit_form_lancamento_valor.requestFocus();//Da focus no campo de valor ao abrir a tela
			} else {
				btn_form_lancamento_excluir.setOnClickListener(new View.OnClickListener() { //Botao Excluir lancamento
					public void onClick(View v) {
						dialogExcluirLancamento();// Abre dialog para confirmar exclusao do lancamento
					}
				});
			}
			
			lancamentoToForm(lancamento);
			
		} catch (Exception e) {
			Toast.makeText(FormLancamentoActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	/**
	 * Abre dialog para confirmar exclusao do lancamento
	 */
	public void dialogExcluirLancamento() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Você tem certeza que deseja excluir esta despesa?");

		builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {// Se clicar em SIM, exclui o lancamento
				dialog.dismiss();// Fecha dialog

				lancamentoDAO.open();
				lancamentoDAO.delete(lancamento);//DELETA LANCAMENTO DO BD
				lancamentoDAO.close();
				
				Toast.makeText(FormLancamentoActivity.this, "Despesa excluída com sucesso!", Toast.LENGTH_SHORT).show();

				Intent i = new Intent(FormLancamentoActivity.this, MainActivity.class);
				startActivity(i);
				finish();
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
	
	private void lancamentoToForm(Lancamento Lancamento) {
		try{			
			edit_form_lancamento_valor.setText(Lancamento.getValor() == 0.0 ? "" : StringFunctions.formataDouble(Lancamento.getValor(),2));//Valor
			edit_form_lancamento_data.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Lancamento.getData()));//Data
			edit_form_lancamento_descricao.setText(lancamento.getDescricao());//Descricao		
		} catch (Exception e) {
			Toast.makeText(FormLancamentoActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}	
	
	private void submitForm() throws ParseException {
		try{
			lancamento.setValor(Validation.formataVerificaValor(edit_form_lancamento_valor.getText()));
			lancamento.setData(new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(edit_form_lancamento_data.getText().toString()));
			lancamento.setDescricao(edit_form_lancamento_descricao.getText().toString());
			
			if (lancamento.getValor() == 0.0) {
				Toast.makeText(FormLancamentoActivity.this, "Valor igual a zero (0,00), não foi possível salvar!", Toast.LENGTH_LONG).show();
			} else {
				lancamentoDAO.open();
				
				// ##SALVA NO BD
				if (lancamento.getId_lancamento() == null) {
					lancamento.setId_lancamento(lancamentoDAO.insert(lancamento));//Novo					
				} else {
					lancamentoDAO.update(lancamento);// Atualiza
				}
				// ##SALVA NO BD

				lancamentoDAO.close();

				Toast.makeText(FormLancamentoActivity.this, "Salvo com sucesso!", Toast.LENGTH_SHORT).show();
				
				Intent i = new Intent(FormLancamentoActivity.this, MainActivity.class);
				startActivity(i);
				finish();
			}
			
		} catch (Exception e) {
			Toast.makeText(FormLancamentoActivity.this, "Erro: "+e.getMessage(), Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	
	// ##DATA##
	// http://www.botskool.com/geeks/how-create-date-picker-dialog-selecting-date-android
	
	private int pYear;
	private int pMonth;
	private int pDay;
	
	/** Este inteiro vai definir com exclusividade o diálogo a ser utilizado para a exibição do selecionador de data. */
	static final int DATE_DIALOG_ID = 0;

	/** 'Callback' recebida quando o usuário "pega" uma data na caixa de diálogo */
	private DatePickerDialog.OnDateSetListener pDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			pYear = year;
			pMonth = monthOfYear;
			pDay = dayOfMonth;
			updateDisplay();
		}
	};

	/** Atualiza a data no TextView */
	private void updateDisplay() {
		edit_form_lancamento_data.setText(new StringBuilder().append(pDay).append("/").append(pMonth + 1).append("/").append(pYear).append(" "));
	}

	/** Cria novo dialog para selecionar a data */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DATE_DIALOG_ID:
				return new DatePickerDialog(this, pDateSetListener, pYear, pMonth, pDay);
		}
		return null;
	}
	// ##DATA##
	
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