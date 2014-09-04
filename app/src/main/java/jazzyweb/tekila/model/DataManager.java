package jazzyweb.tekila.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Objects;

public class DataManager {
    private MySqliteHelper dbHelper;
    private SQLiteDatabase database;

    public DataManager(Context context){
        dbHelper = new MySqliteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /**
     * @param nombre
     * @return el id asociado al grupo creado o -1 si falla la inserción
     */
    public Long createGrupo(String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        long id = database.insert(MySqliteHelper.TABLE_GRUPOS, null, values);

        return id;
    }

    /**
     * @param id
     * @return number of deleted rows (1) o 0
     */
    public int deleteGrupo(Long id){
        int deletedRows = database.delete(MySqliteHelper.TABLE_GRUPOS,
                MySqliteHelper.COLUMN_ID + "=" + id, null);

        return deletedRows;
    }

    /**
     * @param id
     * @param nombre
     * @return number of updated rows
     */
    public int updateGrupo(Long id, String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        int updatedRows = database.update(MySqliteHelper.TABLE_GRUPOS,
                values,  MySqliteHelper.COLUMN_ID + "=" + id, null);

        return updatedRows;
    }

    /**
     * @param nombre
     * @return el id asociado al grupo creado o -1 si falla la inserción
     */
    public Long createUsuario(String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        long id = database.insert(MySqliteHelper.TABLE_USUARIOS, null, values);

        return id;
    }

    /**
     * @param id
     * @return number of deleted rows (1) o 0
     */
    public int deleteUsuario(Long id){
        String[] ids = {Long.toString(id)};
        int deletedRows = database.delete(MySqliteHelper.TABLE_USUARIOS,
                MySqliteHelper.COLUMN_ID + " = ?", ids);

        return deletedRows;
    }

    /**
     * @param id
     * @param nombre
     * @return number of updated rows
     */
    public int updateUsuario(Long id, String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        int updatedRows = database.update(MySqliteHelper.TABLE_USUARIOS,
                values,  MySqliteHelper.COLUMN_ID + "=" + id, null);

        return updatedRows;
    }

    public Long asociaUsuarioAGrupo(Long idUsuario, Long idGrupo){
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_grupo", idGrupo);

        long id = database.insert(MySqliteHelper.TABLE_USUARIO_GRUPO, null, values);

        return id;
    }

    public int desasociaUsuarioDeGrupo(Long idUsuario, Long idGrupo){
        String[] ids = {Long.toString(idUsuario), Long.toString(idGrupo)};
        int deletedRows = database.delete(MySqliteHelper.TABLE_USUARIO_GRUPO,
                MySqliteHelper.COLUMN_IDUSUARIO + " = ? AND " + MySqliteHelper.COLUMN_IDGRUPO + " = ?", ids );

        return deletedRows;
    }

    public Long createCompra(String nombre, float cantidad){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);

        Long id = database.insert(MySqliteHelper.TABLE_COMPRAS, null, values);

        return id;
    }

    public int deleteCompra(Long id){
        String[] ids = {Long.toString(id)};

        int deletedRows = database.delete(MySqliteHelper.TABLE_COMPRAS, MySqliteHelper.COLUMN_IDCOMPRA + " = ?", ids);

        return deletedRows;
    }

    public  int updateCompra(Long id, String nombre, float cantidad){
        String[] ids = {Long.toString(id)};
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);

        int updatedRows = database.update(MySqliteHelper.TABLE_COMPRAS,values,
                MySqliteHelper.COLUMN_IDCOMPRA + " = ?", ids);

        return updatedRows;
    }


}
