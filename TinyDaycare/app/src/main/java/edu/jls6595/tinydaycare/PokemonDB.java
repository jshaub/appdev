package edu.jls6595.tinydaycare;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class PokemonDB extends SQLiteOpenHelper {

    interface OnDBReadyListener {
        void onDBReady(SQLiteDatabase db);
    }

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "pokemon.db";
    private static final String CREATE_TABLES =
            "CREATE TABLE adopted (" +
                    "id INTEGER PRIMARY KEY, " +
                    "type INTEGER CHECK(type >= 0 and type <=2));";
    private static final String DROP_TABLES =
            "DROP TABLE IF EXISTS creatures;";

    private static PokemonDB db;

    private PokemonDB(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, DB_VERSION);
    }

    public static synchronized PokemonDB getInstance(Context context) {
        if(db == null) {
            db = new PokemonDB(context);
        }

        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        sqLiteDatabase.execSQL(DROP_TABLES);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int old_version, int new_version) {
        onUpgrade(sqLiteDatabase, old_version, new_version);
    }

    public void getDatabase(OnDBReadyListener listener) {
        new OpenDBAsyncTask().execute(listener);
    }

    public boolean addPokemonToDB(Pokemon c) {
        // TODO: Implement code to add Pokemon and relevent data to the database for future use
        return true;
    }

    private static class OpenDBAsyncTask extends AsyncTask<OnDBReadyListener, Void, SQLiteDatabase> {
        OnDBReadyListener listener;

        @Override
        protected SQLiteDatabase doInBackground(OnDBReadyListener... params) {
            listener = params[0];
            return PokemonDB.db.getWritableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db) {
            listener.onDBReady(db);
        }

    } // End of AsyncTask class

} // End of PokemonDB class
