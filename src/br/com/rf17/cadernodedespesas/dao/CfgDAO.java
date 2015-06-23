package br.com.rf17.cadernodedespesas.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.rf17.cadernodedespesas.dao.db.DataBaseHandler;
import br.com.rf17.cadernodedespesas.model.Cfg;
import br.com.rf17.cadernodedespesas.model.Data;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CfgDAO {

	private SQLiteDatabase database;
	private DataBaseHandler dbHelper;

	public CfgDAO(Context context) {
		dbHelper = new DataBaseHandler(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Cfg GetConfiguracoes() throws Exception {
		try {
			String selectQuery = "SELECT * FROM cfg;";
			Cursor cursor = database.rawQuery(selectQuery, null);
			cursor.moveToFirst();
			Cfg cfg = null;

			while (!cursor.isAfterLast()) {
				cfg = cursorToEntity(cursor);
				cursor.moveToNext();
			}
			cursor.close();

			return cfg;
		} catch (Exception e) {
			throw new Exception("Erro ao verificar se senha cadastrada!");
		}
	}

	public List<Data> ListMonths() throws Exception {
		try {
			String selectQuery = " select "
					+ " case strftime('%m', date(data)) "
					+ " when '01' then 'Janeiro' "
					+ " when '02' then 'Fevereiro' "
					+ " when '03' then 'Março' " 
					+ " when '04' then 'Abril' "
					+ " when '05' then 'Maio' " 
					+ " when '06' then 'Junho' "
					+ " when '07' then 'Julho' " 
					+ " when '08' then 'Agosto' "
					+ " when '09' then 'Setembro' "
					+ " when '10' then 'Outubro' "
					+ " when '11' then 'Novembro' "
					+ " when '12' then 'Dezembro' " 
					+ " else '' end as month_name, "
					+ " strftime('%Y', data) as year,"
					+ " strftime('%m', date(data)) as month  "
					+ " from lancamento GROUP BY month, year ORDER BY data desc; ";
			Cursor cursor = database.rawQuery(selectQuery, null);
			cursor.moveToFirst();
			List<Data> meses = new ArrayList<Data>();

			while (!cursor.isAfterLast()) {	
				String descricao_mes = cursor.getString(0);
				int ano = cursor.getInt(1);
				int mes = cursor.getInt(2);
				
				Calendar calendar = Calendar.getInstance();
				calendar.set(ano, mes-1, 1);
				
				Data data = new Data();
				data.setDescricao(descricao_mes+" / "+ano);
				data.setDate(calendar.getTime());
				
				meses.add(data);
				cursor.moveToNext();				
			}					
			cursor.close();

			return meses;
			
		} catch (Exception e) {
			throw new Exception("Erro ao listar meses!");
		}
	}

	public long insert(Cfg cfg) {
		return database.insert("cfg", null, cfgToContentValues(cfg));
	}

	public int update(Cfg cfg) {
		return database.update("cfg", cfgToContentValues(cfg),
				"id_cfg=" + cfg.getId_cfg(), null);
	}

	private Cfg cursorToEntity(Cursor cursor) throws ParseException {
		Cfg cfg = new Cfg();
		cfg.setId_cfg(cursor.getLong(0));
		cfg.setSenha(cursor.getString(1));
		return cfg;
	}

	private ContentValues cfgToContentValues(Cfg config) {
		ContentValues values = new ContentValues();
		values.put("id_cfg", config.getId_cfg());
		values.put("senha", config.getSenha());
		return values;
	}
}