package ml.oscarmorton.frasescelebres.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ml.oscarmorton.frasescelebres.TipoUsuario;


public class  DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";
    private static final int DB_VERSION = 1;
    private static final String CREATE_SIMPLE_USER = "INSERT INTO users (username, password, esAdmin) VALUES('frases', 'frases', ' " + TipoUsuario.NOADMIN.toString() + "');";
    private static final String CREATE_ADMIN_USER ="INSERT INTO users (username, password, esAdmin) VALUES('frasesAdmin', 'frasesAdmin', '" + TipoUsuario.ADMIN.toString() + "');";

    private static DBHelper sInstace = null;
    private TipoUsuario tipoUsuario;

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DBNAME, factory, version);
    }
    private DBHelper(@Nullable Context context) {
        super(context, DBNAME, null, DB_VERSION);
    }

    public static synchronized DBHelper getInstance(Context context){
        if(sInstace == null) {
            sInstace = new DBHelper(context.getApplicationContext());

        }
        return sInstace;
    }



    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table users(username TEXT primary key, password TEXT, esAdmin TEXT)");
        MyDB.execSQL(CREATE_SIMPLE_USER);
        MyDB.execSQL(CREATE_ADMIN_USER);




    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");

    }



    public Boolean insertData(String username, String password, TipoUsuario tipoUsuario) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        contentValues.put("esAdmin", tipoUsuario.toString());

        long result = MyDB.insert("users", null, contentValues);

        if (result == 1) return false;
        else
            return true;
    }
    public Boolean checkusername(String username){

        SQLiteDatabase MyDB = this.getWritableDatabase();

        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});

        if(cursor.getCount() > 0){

            return true;
        }else{
            return  false;
        }

    }

    public TipoUsuario checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("select * from users where username = ? and password = ?", new String[]{username,password});

        if(cursor.getCount()>0){
            cursor.moveToFirst();
            String tipoUsuario = cursor.getString(cursor.getColumnIndex("esAdmin"));

            if(tipoUsuario.equalsIgnoreCase("ADMIN")){
                return TipoUsuario.ADMIN;
            }else{
                return TipoUsuario.NOADMIN;
            }
        }else{
            return TipoUsuario.INCORRECTO;
        }
    }


}
