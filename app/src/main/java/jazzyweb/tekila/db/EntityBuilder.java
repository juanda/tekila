package jazzyweb.tekila.db;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jazzyweb.tekila.model.Grupo;
import jazzyweb.tekila.model.Pago;
import jazzyweb.tekila.model.Participacion;
import jazzyweb.tekila.model.Usuario;

public class EntityBuilder<E> {

    E entity;
    SQLiteDatabase database;


    public EntityBuilder(E e, SQLiteDatabase db){
        entity = e;
        database = db;
    }

    public E get(Long id){
        return get(id, null);
    }

    public E get(Long id, String[] fields){


        String[] nativeFields = getNativeTypeFields(entity);

        String[] args = new String[] {String.valueOf(id)};

        Cursor c = database.query(TekilaSqliteHelper.TABLE_USUARIOS, nativeFields, "_id=?", args, null, null, null);

        try {
            if (c.moveToFirst()) {

                for (int i = 0; i < nativeFields.length; i++){

                    Field idField  = entity.getClass().getDeclaredField(nativeFields[i]);
                    idField.setAccessible(true);
                    idField.setLong(entity, id);
                    String methodCamelCase = EntityBuilder.toCamelCase(nativeFields[i]);

                    entity.getClass().getMethod("set" + methodCamelCase).invoke(entity, c.get);
                }

                setId(usuario, id);
                usuario.setNombre(nombre);
                if(full){
                    List<Participacion> participaciones = getParticipacionesFromUsuario(usuario);
                    List<Pago> pagos = getPagosFromUsuario(usuario);
                    List<Grupo> grupos = getGrupoFromUsuario(usuario);

                    usuario.setParticipaciones(participaciones);
                    usuario.setPagos(pagos);
                    usuario.setGrupos(grupos);
                }
            }
        }catch (Exception e){

        }

        return usuario;
    }

    private String[] getNativeTypeFields(E entity){

        ArrayList<String> _fields = new ArrayList<String>();

        for(Field field : entity.getClass().getDeclaredFields()){
            if(field.getType().isPrimitive()){
                _fields.add(field.getType().getName());
            }
        }

        String[] simpleArray = new String[ _fields.size() ];
        _fields.toArray( simpleArray );

        return simpleArray;
    }

    static String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
            camelCaseString = camelCaseString + toProperCase(part);
        }
        return camelCaseString;
    }

    static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
