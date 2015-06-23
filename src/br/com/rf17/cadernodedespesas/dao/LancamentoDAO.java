package br.com.rf17.cadernodedespesas.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.rf17.cadernodedespesas.dao.db.DataBaseHandler;
import br.com.rf17.cadernodedespesas.model.Lancamento;
import br.com.rf17.cadernodedespesas.util.DataUtils;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
 
public class LancamentoDAO {
 
    private SQLiteDatabase database;
    private DataBaseHandler dbHelper;
 
    public LancamentoDAO(Context context) {
        dbHelper = new DataBaseHandler(context);
    }
 
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
 
    public void close() {
        dbHelper.close();
    }
    
    
	@SuppressLint("SimpleDateFormat")
	public List<Lancamento> ListAll(Date date) throws ParseException {
        List<Lancamento> lancamentos = new ArrayList<Lancamento>();
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DATE, 1);
        Date firstDate = cal.getTime();

        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE)); // changed calendar to cal
        Date lastDate = cal.getTime();
        
        String data1 = new SimpleDateFormat("yyyy-MM-dd").format(firstDate);
        String data2 = new SimpleDateFormat("yyyy-MM-dd").format(lastDate);
        
        Log.v("", "## data1: " + data1);
        Log.v("", "## data2: " + data2);
        
        String selectQuery = "SELECT * FROM lancamento WHERE data >= '"+data1+"' AND data <= '"+data2+"' ORDER BY data desc, id_lancamento desc ;" ;        
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        
        while (!cursor.isAfterLast()) {
        	Lancamento lancamento = cursorToEntity(cursor);
        	lancamentos.add(lancamento);
            cursor.moveToNext();
        }        
        cursor.close();
        return lancamentos;    	
    }    

    public Lancamento getById(long id_lancamento) throws ParseException{
    	Lancamento formaPagamento = null;
		String selectQuery = "SELECT * FROM lancamento WHERE id_lancamento="+id_lancamento;	
    	Cursor cursor = database.rawQuery(selectQuery, null);
    	if (cursor != null && cursor.getCount() > 0){
    	    cursor.moveToFirst();    	    
    	    formaPagamento = cursorToEntity(cursor);     	                	 
    	}
    	cursor.close();    	
    	return formaPagamento;    	    	       	  
    }    
               
    public long insert(Lancamento lancamento) { 
        return database.insert("lancamento", null, lancamentoToContentValues(lancamento));    
    }     
     
    public int update(Lancamento lancamento) {     
        return database.update("lancamento", lancamentoToContentValues(lancamento),"id_lancamento="+lancamento.getId_lancamento(), null);         
    }
     
    public void delete(Lancamento lancamento) {              
        database.delete("lancamento", "id_lancamento="+lancamento.getId_lancamento(), null);
    }
            
	private Lancamento cursorToEntity(Cursor cursor) throws ParseException {
		Lancamento lancamento = new Lancamento();
		lancamento.setId_lancamento(cursor.getLong(0));
		lancamento.setValor(cursor.getDouble(1));
		lancamento.setData(cursor.getString(2) == null ? null : DataUtils.getDateBd(cursor.getString(2)));
		lancamento.setDescricao(cursor.getString(3));
		return lancamento;
    }		
	
	private ContentValues lancamentoToContentValues(Lancamento lancamento) {		
		ContentValues values = new ContentValues();
		values.put("id_lancamento", lancamento.getId_lancamento());
		values.put("valor", lancamento.getValor());
		if(lancamento.getData() != null){values.put("data",DataUtils.setDateBd(lancamento.getData()));}
		values.put("descricao", lancamento.getDescricao());
		return values;
	}	
}