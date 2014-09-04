package jazzyweb.tekila.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_IDGRUPO = "id_grupo";
    public static final String COLUMN_IDUSUARIO = "id_usuario";
    public static final String COLUMN_IDCOMPRA = "id_compra";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_CONCEPTO = "concepto";
    public static final String COLUMN_PORCENTAJE = "porcentaje";

    public static final String TABLE_GRUPOS = "grupos";
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_USUARIO_GRUPO = "usuario_grupo";
    public static final String TABLE_PAGOS = "pagos";
    public static final String TABLE_PARTICIPACIONES = "participaciones";
    public static final String TABLE_COMPRAS = "compras";

    private static final String DATABASE_NAME = "tekila.db";
    private static final int DATABASE_VERSION = 3;

    // Database creation sql statement
    private static final String DATABASE_CREATE_GRUPOS = "create table "
            + TABLE_GRUPOS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOMBRE + " text not null unique);";

    private static final String DATABASE_CREATE_USUARIOS = "create table "
            + TABLE_USUARIOS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOMBRE + " text not null unique);";

    private static final String DATABASE_CREATE_USUARIO_GRUPO = "create table "
            + TABLE_USUARIO_GRUPO + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_IDUSUARIO + " INTEGER, "
            + COLUMN_IDGRUPO + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_IDUSUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_IDGRUPO + " ) REFERENCES " + TABLE_GRUPOS + "(" + COLUMN_ID + ")"
            + ");";

    private static final String DATABASE_CREATE_COMPRAS = "create table "
            + TABLE_COMPRAS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_NOMBRE + " text not null unique, "
            + COLUMN_CANTIDAD + " REAL"
            + ");";

    private static final String DATABASE_CREATE_PAGOS = "create table "
            + TABLE_PAGOS + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_CANTIDAD + " REAL, "
            + COLUMN_IDCOMPRA + " INTEGER, "
            + COLUMN_IDUSUARIO + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_IDUSUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_IDCOMPRA + ") REFERENCES " + TABLE_COMPRAS + "(" + COLUMN_ID + ")"
            + ");";

    private static final String DATABASE_CREATE_PARTICIPACIONES = "create table "
            + TABLE_PARTICIPACIONES + "(" + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_PORCENTAJE + " REAL, "
            + COLUMN_IDCOMPRA + " INTEGER, "
            + COLUMN_IDUSUARIO + " INTEGER, "
            + "FOREIGN KEY(" + COLUMN_IDUSUARIO + ") REFERENCES " + TABLE_USUARIOS + "(" + COLUMN_ID + "), "
            + "FOREIGN KEY(" + COLUMN_IDCOMPRA + ") REFERENCES " + TABLE_COMPRAS + "(" + COLUMN_ID + ")"
            + ");";

    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database){
        database.execSQL(DATABASE_CREATE_GRUPOS);
        database.execSQL(DATABASE_CREATE_USUARIOS);
        database.execSQL(DATABASE_CREATE_USUARIO_GRUPO);
        database.execSQL(DATABASE_CREATE_COMPRAS);
        database.execSQL(DATABASE_CREATE_PAGOS);
        database.execSQL(DATABASE_CREATE_PARTICIPACIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySqliteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRUPOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO_GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPRAS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPACIONES);
        onCreate(db);
    }
}