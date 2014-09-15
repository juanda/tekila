package jazzyweb.tekila.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import jazzyweb.tekila.db.TekilaSqliteHelper;

public class DataBaseManager {
    private TekilaSqliteHelper dbHelper;
    private SQLiteDatabase database;

    public DataBaseManager(Context context){
        dbHelper = TekilaSqliteHelper.getInstance(context);
    }

    public void open() throws SQLiteException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // GRUPOS

    /**
     * @param nombre
     * @return el id asociado al grupo creado o -1 si falla la inserción
     */
    public Long createGrupo(String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        long id = database.insert(TekilaSqliteHelper.TABLE_GRUPOS, null, values);

        return id;
    }

    /**
     * @param id
     * @return number of deleted rows (1) o 0
     */
    public int deleteGrupo(Long id){
        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_GRUPOS,
                TekilaSqliteHelper.COLUMN_ID + "=" + id, null);

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
        int updatedRows = database.update(TekilaSqliteHelper.TABLE_GRUPOS,
                values,  TekilaSqliteHelper.COLUMN_ID + "=" + id, null);

        return updatedRows;
    }

    // USUARIOS

    /**
     * @param nombre
     * @return el id asociado al grupo creado o -1 si falla la inserción
     */
    public Long createUsuario(String nombre){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        long id = database.insert(TekilaSqliteHelper.TABLE_USUARIOS, null, values);

        return id;
    }

    /**
     * @param id
     * @return number of deleted rows (1) o 0
     */
    public int deleteUsuario(Long id){
        String[] ids = {Long.toString(id)};
        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_USUARIOS,
                TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

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
        int updatedRows = database.update(TekilaSqliteHelper.TABLE_USUARIOS,
                values,  TekilaSqliteHelper.COLUMN_ID + "=" + id, null);

        return updatedRows;
    }

    // Asociación USUARIOS-GRUPOS

    public Long asociaUsuarioAGrupo(Long idUsuario, Long idGrupo){
        ContentValues values = new ContentValues();
        values.put("id_usuario", idUsuario);
        values.put("id_grupo", idGrupo);

        long id = database.insert(TekilaSqliteHelper.TABLE_USUARIO_GRUPO, null, values);

        return id;
    }

    public int desasociaUsuarioDeGrupo(Long idUsuario, Long idGrupo){
        String[] ids = {Long.toString(idUsuario), Long.toString(idGrupo)};
        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_USUARIO_GRUPO,
                TekilaSqliteHelper.COLUMN_IDUSUARIO + " = ? AND " + TekilaSqliteHelper.COLUMN_IDGRUPO + " = ?", ids );

        return deletedRows;
    }

    // COMPRAS

    public Long createCompra(String nombre, Double cantidad, Long idGrupo, Long datetime){
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);
        values.put("id_grupo", idGrupo);
        values.put("datetime", datetime);

        Long id = database.insert(TekilaSqliteHelper.TABLE_COMPRAS, null, values);

        return id;
    }

    public int deleteCompra(Long id){
        String[] ids = {Long.toString(id)};

        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_COMPRAS, TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

        return deletedRows;
    }

    public  int updateCompra(Long id, String nombre, float cantidad, Long idGrupo, Long datetime){
        String[] ids = {Long.toString(id)};
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);
        values.put("id_grupo", idGrupo);
        values.put("datetime", datetime);

        int updatedRows = database.update(TekilaSqliteHelper.TABLE_COMPRAS,values,
                TekilaSqliteHelper.COLUMN_IDCOMPRA + " = ?", ids);

        return updatedRows;
    }


    // PAGOS

    public Long createPago(Double cantidad, Long idUsuario, Long idCompra){
        ContentValues values = new ContentValues();
        values.put("cantidad", cantidad);
        values.put("id_usuario", idUsuario);
        values.put("id_compra", idCompra);

        Long id = database.insert(TekilaSqliteHelper.TABLE_PAGOS, null, values);

        return id;
    }

    public int deletePago(Long id){
        String[] ids = {Long.toString(id)};

        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_PAGOS, TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

        return deletedRows;
    }


    public int updatePago(Long id, Double cantidad, Long idUsuario, Long idCompra){
        String[] ids = {Long.toString(id)};
        ContentValues values = new ContentValues();
        values.put("cantidad", cantidad);
        values.put("id_usuario", idUsuario);
        values.put("id_compra", idCompra);

        int updatedRows = database.update(TekilaSqliteHelper.TABLE_PAGOS, values, TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

        return updatedRows;
    }

    // PARTICIPACIONES

    public Long createParticipacion(Double porcentaje, Long idUsuario, Long idCompra){
        ContentValues values = new ContentValues();
        values.put("porcentaje", porcentaje);
        values.put("id_usuario", idUsuario);
        values.put("id_compra", idCompra);

        Long id = database.insert(TekilaSqliteHelper.TABLE_PARTICIPACIONES, null, values);

        return id;
    }

    public int deleteParticipacion(Long id){
        String[] ids = {Long.toString(id)};

        int deletedRows = database.delete(TekilaSqliteHelper.TABLE_PARTICIPACIONES, TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

        return deletedRows;
    }


    public int updateParticipacion(Long id, Double porcentaje, Long idUsuario, Long idCompra){
        String[] ids = {Long.toString(id)};
        ContentValues values = new ContentValues();
        values.put("cantidad", porcentaje);
        values.put("id_usuario", idUsuario);
        values.put("id_compra", idCompra);

        int updatedRows = database.update(TekilaSqliteHelper.TABLE_PARTICIPACIONES, values, TekilaSqliteHelper.COLUMN_ID + " = ?", ids);

        return updatedRows;
    }
}
