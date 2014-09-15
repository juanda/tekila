package jazzyweb.tekila.db;

import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Arrays;

public class TekilaSqliteHelper extends SQLiteOpenHelper {
    private static final String TAG = "TekilaSqliteHelper";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_IDGRUPO = "id_grupo";
    public static final String COLUMN_IDUSUARIO = "id_usuario";
    public static final String COLUMN_IDCOMPRA = "id_compra";
    public static final String COLUMN_CANTIDAD = "cantidad";
    public static final String COLUMN_PORCENTAJE = "porcentaje";

    public static final String TABLE_GRUPOS = "grupos";
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_USUARIO_GRUPO = "usuario_grupo";
    public static final String TABLE_PAGOS = "pagos";
    public static final String TABLE_PARTICIPACIONES = "participaciones";
    public static final String TABLE_COMPRAS = "compras";

    public static final String DATABASE_FILE = "tekila.sql";
    public static final String DATABASE_NAME = "tekila.db";
    private static final int DATABASE_VERSION = 4;
    private static final int BUFFER_SIZE = 2048;
    private static SQLiteDatabase db;
    private static TekilaSqliteHelper instance;
    private static Context context;

    /**
     * Constructor instantiating every member.
     *
     * @param context the context to set.
     * @param name the name to set.
     * @param factory the factory to set.
     * @param version the version to set.
     */
    private TekilaSqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        TekilaSqliteHelper.context = context;
    }

    /**
     * Creates the database from a script within a transaction.
     * Rollbacks the transaction if an error occurs.
     *
     * @param db the {@link SQLiteDatabase} object.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        db.beginTransaction();

        try {
            readDatabaseScript(db);
            db.setTransactionSuccessful();
        } catch (IOException ioe) {
            Log.e(TAG, ioe.getMessage());
        } catch (SQLException sqle) {
            Log.e(TAG, sqle.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Reads the database script from a path determined internally and executes every SQL
     * statement in the script.
     *
     * @param db the {@link SQLiteDatabase} object.
     * @throws IOException if the database script wasn't found.
     * @throws SQLException in case a SQL statement was malformed.
     */
    private void readDatabaseScript(SQLiteDatabase db) throws IOException, SQLException {
        InputStream script = context.getAssets().open(DATABASE_FILE);

        byte[] buffer = new byte[BUFFER_SIZE];

        for (int byteRead = script.read(); byteRead != -1; byteRead = script.read()) {
            // resets the buffer
            Arrays.fill(buffer, (byte) 0);

            // reads a SQL statement to the buffer
            for (int i = 0; byteRead != -1 && i != BUFFER_SIZE; byteRead = script.read(), i++) {
                buffer[i] = (byte) byteRead;

                if (byteRead == ';')
                    break;
            }

// execute the SQL statement from the buffer
            if (byteRead != -1)
                db.execSQL(new String(buffer));
        }
    }

    /**
     * Runs the onCreate method, since the database script will drop the tables upon beginning.
     *
     * @param db the {@link SQLiteDatabase} object.
     * @param oldVersion the old version id.
     * @param newVersion the new version id.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO_GRUPO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRUPOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPACIONES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAGOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPRAS);
        onCreate(db);
    }

    /*
    * (non-Javadoc)
    * @see android.database.sqlite.SQLiteOpenHelper#close()
    */
    @Override
    public synchronized void close() {
        if (instance != null)
            db.close();
    }

    /**
     * Retrieves a thread-safe instance of the singleton object {@link DBMS} and opens the database
     * with writing permissions.
     *
     * @param context the context to set.
     * @return the singleton instance.
     */
    public static synchronized TekilaSqliteHelper getInstance(Context context) {
        if (instance == null) {
            instance = new TekilaSqliteHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
            db = instance.getWritableDatabase();
        }

        return instance;
    }

    /**
     * Counts the rows of a given table, using a method from {@link DatabaseUtils}.
     *
     * @param table the table from where to count the rows.
     * @return the number of entries of the given table.
     */
    public int getNumberRows(String table) {
        return (int) DatabaseUtils.queryNumEntries(db, table);
    }
}
