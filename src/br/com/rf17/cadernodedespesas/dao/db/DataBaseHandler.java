package br.com.rf17.cadernodedespesas.dao.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import br.com.rf17.cadernodedespesas.util.FileUtils;
import br.com.rf17.cadernodedespesas.view.FormLoginActivity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHandler extends SQLiteOpenHelper {
	    
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "CadernoDeDespesas_DATABASE";
    Context contextActivity = null;
    
    
    public DataBaseHandler(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	contextActivity = context;
    }

    @Override
	public void onCreate(SQLiteDatabase db) {
		
		//CONFIGURACOES
		db.execSQL("CREATE TABLE cfg("
				+ " id_cfg INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " senha TEXT NOT NULL); ");
    	
		//LANCAMENTO
		db.execSQL("CREATE TABLE lancamento("
				+ " id_lancamento INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " valor NUMERIC NOT NULL,"
				+ " data TEXT NOT NULL,"
				+ " descricao TEXT); ");
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			int contador = 0;// Contar o número de atualizacoes, para mostrar na tela

			Toast.makeText(contextActivity, "Atualizando banco de dados...", Toast.LENGTH_LONG).show();

			if (oldVersion < 2) {/* Adicionado coluna de configuracoes (senha) */
				//CONFIGURACOES
				db.execSQL("CREATE TABLE cfg("
						+ " id_cfg INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ " senha TEXT NOT NULL); ");
		    	
				contador++;
			}

			Toast.makeText(contextActivity, "Atualização do banco de dados concluída com sucesso! (Número de atualizações: "+contador+")", Toast.LENGTH_LONG).show();

		} catch (SQLException e) {
			FormLoginActivity.erro_bd = e.getMessage();
		}
	}

	/**
	 * Copia um arquivo de banco de dados (caminho da variavel db_import) para o banco de dados da aplicacao, ou seja: faz uma restauracao de outros dados
	 * 
	 * ATENÇÃO: Usar com cuidado, pois irá sobrescrever todos os dados locais!
	 * 	 
	 */
	public boolean importDatabase(String db_local, String db_import) throws IOException {
	    close();// Fecha o a conexao com o bd 
	    File newDb = new File(db_import);//Arquivo que ira ser importado
	    File oldDb = new File(db_local);//Arquivo do bd local
	    if (newDb.exists()) {//Se achar arquivo para ser restaurado
	        FileUtils.copyFile(new FileInputStream(newDb), new FileOutputStream(oldDb));//Faz a transferencia	        
	        getWritableDatabase().close();// Acessa a db criada, para o DataBaseHandler armazenar e marcar como criada.
	        return true;//Retorna para dizer que foi importado com sucesso
	    }
	    return false;//Retorna para dizer que deu erro
	}
	
}
