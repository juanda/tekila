package jazzyweb.tekila.db;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jazzyweb.tekila.utils.StreamReader;

public class LoadTestData{

    private Map<Long, Long> grupos;
    private Map<Long, Long> usuarios;
    private Map<Long, Long> compras;

    private ModelManager modelManager;
    private String jsonDataTest;

    public LoadTestData(String fileTest, Context context) throws IOException{
        Log.i("TEKILA_LOADTESTDATA", "en loadtestdata " + fileTest);
        InputStream is = context.getAssets().open(fileTest);

        try {
            jsonDataTest = StreamReader.convertStreamToString(is);
            
            modelManager = new ModelManager(context);
        }catch (IOException e){
            Log.i(this.getClass().getName(), "Error abriendo el fichero " + fileTest);
        }
    }

    public void load() throws IOException {

        try {
            JSONObject json = new JSONObject(jsonDataTest);

            grupos = createGrupos(json);
            usuarios = createUsuarios(json);
            compras = createCompras(json);

        }catch (JSONException e){
            Log.i(this.getClass().getName(), "Error leyendo el json con los datos de prueba." + jsonDataTest);
            Log.i(this.getClass().getName(), e.getMessage());
        }


    }

    private Map<Long, Long> createGrupos(JSONObject json) throws IOException{

        HashMap<Long, Long> _grupos = new HashMap<Long, Long>();
        try {
            JSONArray arrGrupos = json.getJSONArray("grupos");
            for(int i=0; i< arrGrupos.length(); i++){
                JSONObject objGrupo =arrGrupos.getJSONObject(i);
                Long idInJson = objGrupo.getLong("id");
                String nombre = objGrupo.getString("nombre");

                Long id = modelManager.createGrupo(nombre);
                Log.i(this.getClass().getName(), "creado grupo " + nombre + " con id " + id);
                _grupos.put(idInJson, id);
                Log.i(this.getClass().getName(), "añadido al mapa de grupos el par " + idInJson + "," + id);

            }
        }catch (JSONException e){
            Log.i(this.getClass().getName(), "createGrupos: No puedo leer el array de objetos" );
            Log.i(this.getClass().getName(), e.getMessage());
        }
        return _grupos;
    }

    private Map<Long, Long> createUsuarios(JSONObject json) throws IOException{

        HashMap<Long, Long> _usuarios = new HashMap<Long, Long>();
        try{
            JSONArray arrUsuarios = json.getJSONArray("usuarios");
            for(int i = 0; i < arrUsuarios.length(); i++){
                JSONObject objUsuario = arrUsuarios.getJSONObject(i);
                Long idInJson = objUsuario.getLong("id");
                String nombre = objUsuario.getString("nombre");
                JSONArray arrGrupos = objUsuario.getJSONArray("grupos");

                Long id = modelManager.createUsuario(nombre);
                Log.i(this.getClass().getName(), "creado usuario " + nombre + " con id " + id);

                _usuarios.put(idInJson, id);
                Log.i(this.getClass().getName(), "añadido al mapa de usuarios el par " + idInJson + "," + id);

                for(int j = 0; j < arrGrupos.length(); j++){
                    Long idGrupo = grupos.get(arrGrupos.getLong(j));
                    modelManager.asociaUsuarioAGrupo(id, idGrupo);
                    Log.i(this.getClass().getName(), "asociado el usuario " + nombre + " con id " + id + " al grupo " + idGrupo);
                }
            }
        }catch (JSONException e){
            Log.i(this.getClass().getName(), "createUsuarios: No puedo leer el array de objetos" );
            Log.i(this.getClass().getName(), e.getMessage());
        }

        return _usuarios;
    }

    private Map<Long, Long> createCompras(JSONObject json) throws IOException{
        HashMap<Long, Long> _compras = new HashMap<Long, Long>();

        try{
            JSONArray arrCompras = json.getJSONArray("compras");
            for(int i = 0; i < arrCompras.length(); i++){
                JSONObject objCompra = arrCompras.getJSONObject(i);
                Long idInJson = objCompra.getLong("id");
                Long idGrupo = objCompra.getLong("grupo");
                String nombre = objCompra.getString("nombre");
                Double cantidad = objCompra.getDouble("cantidad");
                String dateTimeString = objCompra.getString("datetime");
                JSONObject objParticipantes = objCompra.getJSONObject("participantes");
                JSONObject objPagos = objCompra.getJSONObject("pagos");

                Date fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dateTimeString);
                Long datetime = fecha.getTime();
                Long id = modelManager.createCompra(nombre,cantidad, grupos.get(idGrupo), datetime);
                Log.i(this.getClass().getName(), "creada la compra " + nombre + " con id " + id);
                _compras.put(idInJson, id);
                Log.i(this.getClass().getName(), "añadido al mapa de compras el par " + idInJson + "," + id);


                for(Iterator<String> iter = objParticipantes.keys(); iter.hasNext();) {
                    String key = iter.next();
                    Double porcentaje = (Double) objParticipantes.get(key);

                    Long idUsuario = Long.parseLong(key);
                    Long idPart = modelManager.createParticipacion(porcentaje, idUsuario, id);
                    Log.i(this.getClass().getName(), "creado la participación con id "
                            + idPart + " nombre compra '" + nombre + "' porcentaje "
                            + porcentaje + " usuario " + idUsuario + " compra " + id);

                }

                for(Iterator<String> iter = objPagos.keys(); iter.hasNext();) {
                    String key = iter.next();
                    Double cantidadPago = (Double) objPagos.get(key);

                    Long idUsuario = Long.parseLong(key);
                    Long idPago = modelManager.createPago(cantidadPago, Long.parseLong(key), id);
                    Log.i(this.getClass().getName(), "creado el pago con id "
                            + idPago + " nombre compra '" + nombre + "' cantidad "
                            + cantidad + " usuario " + idUsuario + " compra " + id);
                }

            }

        }catch (JSONException e){
            Log.i(this.getClass().getName(), "createCompras: No puedo leer el array de objetos" );
            Log.i(this.getClass().getName(), e.getMessage());
        }catch (ParseException e){
            Log.i(this.getClass().getName(), "createCompras: No puedo parsear la fecha" );
        }


        return _compras;
    }

}
