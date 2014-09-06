package jazzyweb.tekila.model;

import android.content.Context;
import android.database.sqlite.SQLiteException;

import java.sql.SQLException;

/**
 * Created by juanda on 5/09/14.
 */
public class LoadTestData{

    public static void load(Context context) throws SQLiteException{
        DataBaseManager dbManager = new DataBaseManager(context);
        dbManager.open();

        dbManager.createGrupo("Tekila");
        dbManager.createGrupo("Harinawers");
    }
}
