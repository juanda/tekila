package jazzyweb.tekila.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.db.TekilaSqliteHelper;
import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;

public class DataBaseManager {
    private TekilaSqliteHelper dbHelper;
    private SQLiteDatabase database;

    public DataBaseManager(Context context) {
        dbHelper = TekilaSqliteHelper.getInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        if (database == null) {
            open();
        }
        return database;
    }
}