package jazzyweb.tekila.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.model.Compra;
import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;

public class ModelManager {

    private SQLiteDatabase database;

    public ModelManager(Context context) {
        DataBaseManager dataBaseManager = new DataBaseManager(context);
        database = dataBaseManager.getDatabase();
    }

    public List<Usuario> getUsuariosFromGrupo(Long idGrupo){

        List<Usuario> _usuarios = new ArrayList<Usuario>();
        String[] args = new String[] {String.valueOf(idGrupo)};

        String query = "SELECT u._id, u.nombre FROM " +TekilaSqliteHelper.TABLE_USUARIOS + " as u, "
                + TekilaSqliteHelper.TABLE_USUARIO_GRUPO + " as ug "
                + "WHERE ug.id_grupo=? and u._id=ug.id_usuario";

        Cursor c = database.rawQuery(query, args);

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    String nombre = c.getString(1);

                    Usuario usuario = new Usuario();
                    setId(usuario, id);
                    usuario.setNombre(nombre);

                    List<Participacion> participaciones = getParticipacionesFromUsuario(usuario.getId());
                    List<Pago> pagos = getPagosFromUsuario(usuario.getId());

                    usuario.setParticipaciones(participaciones);
                    usuario.setPagos(pagos);

                    _usuarios.add(usuario);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return _usuarios;
    }

    public List<Compra> getComprasFromGrupo(Long idGrupo){
        List<Compra> _compras = new ArrayList<Compra>();

        String[] campos = new String[] { "_id", "nombre", "cantidad", "datetime" };
        String[] args = new String[] {String.valueOf(idGrupo)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_COMPRAS, campos, "id_grupo=?", args, null, null, "datetime desc");

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    String nombre = c.getString(1);
                    Double cantidad = c.getDouble(2);
                    Long datetime = c.getLong(3);

                    Compra compra = new Compra();
                    setId(compra, id);
                    compra.setNombre(nombre);
                    compra.setCantidad(cantidad);
                    compra.setDatetime(datetime);

                    List<Participacion> participaciones = getParticipacionesFromCompra(compra.getId());
                    List<Pago> pagos = getPagosFromCompra(compra.getId());

                    compra.setParticipaciones(participaciones);
                    compra.setPagos(pagos);

                    _compras.add(compra);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return _compras;
    }

//    public List<Participacion> getParticipacionesFromUsuario(Usuario usuario, Boolean full){
//        Long idCompra = usuario.getId();
//
//        List<Participacion> _participaciones = new ArrayList<Participacion>();
//        String[] campos = new String[] { "_id", "id_compra", "porcentaje" };
//        String[] args = new String[] {String.valueOf(usuario.getId())};
//
//        Cursor c = database.query(TekilaSqliteHelper.TABLE_PARTICIPACIONES, campos, "id_usuario=?", args, null, null, null);
//
//        try {
//            if (c.moveToFirst()) {
//                do {
//                    Long id = c.getLong(0);
//                    Long id_compra = c.getLong(1);
//                    Double porcentaje = c.getDouble(2);
//
//                    Participacion participacion = new Participacion();
//                    setId(participacion, id);
//                    participacion.setUsuario(usuario);
//                    participacion.setPorcentaje(porcentaje);
//
//                    if(full){
//                        Compra compra = getCompra(id_compra);
//                        participacion.setCompra(compra);
//                    }
//
//                    _participaciones.add(participacion);
//
//                } while (c.moveToNext());
//            }
//        }catch (Exception e) {
//            Log.i(this.getClass().getName(), e.getMessage());
//        }
//
//        return  _participaciones;
//    }

    public List<Participacion> getParticipacionesFromCompra(Long idCompra){

        List<Participacion> _participaciones = new ArrayList<Participacion>();

        String[] campos = new String[] { "_id", "id_usuario", "porcentaje" };
        String[] args = new String[] {String.valueOf(idCompra)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_PARTICIPACIONES, campos, "id_compra=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    Long id_usuario = c.getLong(1);
                    Double porcentaje = c.getDouble(2);

                    Participacion participacion = new Participacion();
                    setId(participacion, id);
                    participacion.setPorcentaje(porcentaje);

                    Usuario usuario = getUsuario(id_usuario);
                    participacion.setUsuario(usuario);

                    _participaciones.add(participacion);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return  _participaciones;
    }

    public List<Pago> getPagosFromCompra(Long idCompra){

        List<Pago> _pagos = new ArrayList<Pago>();

        String[] campos = new String[] { "_id", "id_usuario", "cantidad" };
        String[] args = new String[] {String.valueOf(idCompra)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_PAGOS, campos, "id_compra=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    Long id_usuario = c.getLong(1);
                    Double cantidad = c.getDouble(2);

                    Pago pago = new Pago();
                    setId(pago, id);
                    pago.setCantidad(cantidad);

                    Usuario usuario = getUsuario(id_usuario);
                    pago.setUsuario(usuario);

                    _pagos.add(pago);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return  _pagos;
    }

    public List<Participacion> getParticipacionesFromUsuario(Long idUsuario){

        List<Participacion> _participaciones = new ArrayList<Participacion>();

        String[] campos = new String[] { "_id", "id_compra", "porcentaje" };
        String[] args = new String[] {String.valueOf(idUsuario)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_PARTICIPACIONES, campos, "id_usuario=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    Long id_compra = c.getLong(1);
                    Double porcentaje = c.getDouble(2);

                    Participacion participacion = new Participacion();
                    setId(participacion, id);
                    participacion.setPorcentaje(porcentaje);

                    Compra compra = getCompra(id_compra);
                    participacion.setCompra(compra);

                    _participaciones.add(participacion);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return  _participaciones;
    }

    public List<Pago> getPagosFromUsuario(Long idUsuario){

        List<Pago> _pagos = new ArrayList<Pago>();

        String[] campos = new String[] { "_id", "id_compra", "cantidad" };
        String[] args = new String[] {String.valueOf(idUsuario)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_PAGOS, campos, "id_usuario=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                do {
                    Long id = c.getLong(0);
                    Long id_compra = c.getLong(1);
                    Double cantidad = c.getDouble(2);

                    Pago pago = new Pago();
                    setId(pago, id);
                    pago.setCantidad(cantidad);

                    Compra compra = getCompra(id_compra);
                    pago.setCompra(compra);

                    _pagos.add(pago);

                } while (c.moveToNext());
            }
        }catch (Exception e) {
            Log.i(this.getClass().getName(), e.getMessage());
        }finally {
            c.close();
        }

        return  _pagos;
    }

    public Usuario getUsuario(Long id){

        Usuario usuario = new Usuario();

        String[] campos = new String[] { "nombre" };
        String[] args = new String[] {String.valueOf(id)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_USUARIOS, campos, "_id=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                String nombre = c.getString(0);
                setId(usuario, id);
                usuario.setNombre(nombre);
            }
        }catch (Exception e){
            Log.i(this.getClass().getName(), e.getMessage());
        } finally {
            c.close();
        }

        return usuario;
    }

    public Compra getCompra(Long id){

        Compra compra = new Compra();

        String[] campos = new String[] { "nombre", "cantidad", "id_grupo", "datetime" };
        String[] args = new String[] {String.valueOf(id)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_COMPRAS, campos, "_id=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                String nombre = c.getString(0);
                Double cantidad = c.getDouble(1);
                Long id_grupo = c.getLong(2);
                Long datetime = c.getLong(3);
                setId(compra, id);
                compra.setNombre(nombre);
                compra.setCantidad(cantidad);
                compra.setDatetime(datetime);

                Grupo grupo = getGrupo(id_grupo);

                compra.setGrupo(grupo);

            }
        }catch (Exception e){
            Log.i(this.getClass().getName(), e.getMessage());
        } finally {
            c.close();
        }

        return compra;
    }

    public Grupo getGrupo(Long id){

        Grupo grupo = new Grupo();

        String[] campos = new String[] { "nombre" };
        String[] args = new String[] {String.valueOf(id)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_GRUPOS, campos, "_id=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {
                String nombre = c.getString(0);
                setId(grupo, id);
                grupo.setNombre(nombre);
            }
        }catch (Exception e){
            Log.i(this.getClass().getName(), e.getMessage());
        } finally {
            c.close();
        }

        return grupo;
    }

//    public Compra getCompra(Long id){
//        Compra compra = new Compra();
//
//        String[] campos = new String[] { "concepto", "cantidad" };
//        String[] args = new String[] {String.valueOf(id)};
//
//        Cursor c = database.query(TekilaSqliteHelper.TABLE_COMPRAS, campos, "_id=?", args, null, null, null);
//
//        try {
//            if (c.moveToFirst()) {
//
//                String concepto = c.getString(0);
//                Double cantidad = c.getDouble(1);
//
//                setId(compra, id);
//                compra.setConcepto(concepto);
//                if(full){
//                    List<Participacion> participaciones = getParticipacionesFromCompra(compra);
//                    List<Pago> pagos = getPagosFromCompra(compra);
//
//
//                    compra.setParticipaciones(participaciones);
//                    compra.setPagos(pagos);
//                }
//            }
//        }catch (Exception e){
//            Log.i(this.getClass().getName(), e.getMessage());
//        }
//
//        return compra;
//    }

    public int getNumParticipantes(Long idGrupo){

        String query = "select count(*) from " + TekilaSqliteHelper.TABLE_USUARIO_GRUPO
                + " where id_grupo=" + idGrupo;
        Cursor mCount= database.rawQuery(query, null);
        mCount.moveToFirst();
        int count= mCount.getInt(0);

        mCount.close();

        return count;
    }

    private void setId(Object entity, Long id) throws NoSuchFieldException, IllegalAccessException{
        Field idField  = entity.getClass().getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(entity, Long.valueOf(id));
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

    public  int updateCompra(Long id, String nombre, Double cantidad, Long idGrupo, Long datetime){
        String[] ids = {String.valueOf(id)};
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);
        values.put("id_grupo", idGrupo);
        values.put("datetime", datetime);

        int updatedRows = database.update(TekilaSqliteHelper.TABLE_COMPRAS,values,
                TekilaSqliteHelper.COLUMN_ID + "=?", ids);

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

    public int deletePagos(Long idCompra){
        String[] ids = {String.valueOf(idCompra)};

        int deleteRows = database.delete(TekilaSqliteHelper.TABLE_PAGOS, TekilaSqliteHelper.COLUMN_IDCOMPRA + "=?", ids);

        return deleteRows;
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

    public int deleteParticipaciones(Long idCompra){
        String[] ids = {String.valueOf(idCompra)};

        int deleteRows = database.delete(TekilaSqliteHelper.TABLE_PARTICIPACIONES, TekilaSqliteHelper.COLUMN_IDCOMPRA + "=?", ids);

        return deleteRows;
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
