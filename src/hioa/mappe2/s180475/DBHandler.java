package hioa.mappe2.s180475;

import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler {
	private Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public static final String DB_NAME = "birthdays";
	public static final int DB_VERSION = 1;
	public static final String TABEL_PERSONS = "persons";
	public static final String TABEL_PERSONAL_MESSAGES = "messages";
	public static final String ID = "_id";
	public static final String FIRST_NAME = "first_name";
	public static final String LAST_NAME = "last_name";
	public static final String DATE = "date";
	public static final String MESSAGE = "message";
	public static final String HAS_PERSONAL_MESSAGE = "has_personal_message";
	
	public DBHandler(Context c){
		context = c;
		DBHelper = new DatabaseHelper(context);
	}
	
	public DBHandler open() throws SQLException{
		db = DBHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		db.close();
	}
	
	public boolean insert(String table, ContentValues cv){
		if(table.equals(TABEL_PERSONAL_MESSAGES))
			return db.insert(TABEL_PERSONAL_MESSAGES,null,cv) > 0;
		return db.insert(TABEL_PERSONS,null,cv) > 0;
	}
	
	public boolean update(String table, ContentValues cv){
		if(table.equals(TABEL_PERSONAL_MESSAGES))
			return db.update(TABEL_PERSONAL_MESSAGES, cv, ID + "='" + cv.get(ID) + "'", null) > 0;
		
		return db.update(TABEL_PERSONS, cv, ID + "='" + cv.get(ID) + "'", null) > 0;
	}
	
	public boolean delete(String table, String whereClause, String[] whereArgs){
		return db.delete(table, whereClause, whereArgs) > 0;
	}
	
	//return todays birthdays
	public Cursor todaysBirthdays(){
		Calendar cal = Calendar.getInstance();
		int m = cal.get(Calendar.MONTH) + 1;		//gets the month of today
		int d = cal.get(Calendar.DAY_OF_MONTH);		//gets the day of today
		Cursor cur = db.query(TABEL_PERSONS, null, DATE + " like '%"+d+"."+m+"%'", null, null, null, null);
		return cur;
	}
	
	public Cursor allBirthdays(){
		Cursor cur = db.query(TABEL_PERSONS, null, null,null,null,null,null);
		return cur;
	}
	
	public String getPrivateMessage(String id){
		Cursor cur = db.query(TABEL_PERSONAL_MESSAGES, null, ID + "='" + id + "'", null, null, null, null);
		if(cur.moveToFirst()){
			return cur.getString(1);
		}
		return null;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		private Context con;
		public DatabaseHelper(Context c){
			super(c, DB_NAME, null, DB_VERSION);
			con = c;
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//Creates the persons-tabel
			String sql = "create table " + TABEL_PERSONS + " (" +
				ID + " text primary key, " +
				FIRST_NAME + " text not null, " +
				LAST_NAME + " text not null, " +
				DATE + " text not null, " +
				HAS_PERSONAL_MESSAGE + " integer default 0);";
			db.execSQL(sql);
			
			//Creates the messages-tabel
			String sql2 = "create table " + TABEL_PERSONAL_MESSAGES + " (" +
				ID + " text primary key, " +
				MESSAGE + " text not null);";
			db.execSQL(sql2);
			ContentValues cv = new ContentValues();
			cv.put(ID, "99999999");
			cv.put(MESSAGE, con.getString(R.string.defaultMessage));
			db.insert(TABEL_PERSONAL_MESSAGES, null, cv);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("drop table if exists " + TABEL_PERSONS);
			db.execSQL("drop table if exists " + TABEL_PERSONAL_MESSAGES);
			onCreate(db);
		}
	} //end of DatabaseHelper	
	
}
