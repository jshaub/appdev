package edu.jls6595.tinydaycare;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

public class PokemonDB extends SQLiteOpenHelper {

    interface OnDBReadyListener {
        void onDBReady(SQLiteDatabase db);
    }

    public static final int DB_VERSION = 2;
    public static final String DB_NAME = "pokemon.db";
    private static final String CREATE_TABLES =
            "CREATE TABLE pokemon (" +
                    "id INTEGER PRIMARY KEY," +
                    "current INTEGER NOT NULL," +
                    "hatched INTEGER NOT NULL," +
                    "readyToHatch INTEGER NOT NULL," +
                    "currentSteps INTEGER NOT NULL," +
                    "eggSprite INTEGER NOT NULL," +
                    "hatchedSprite INTEGER NOT NULL," +
                    "type TEXT NOT NULL," +
                    "cost INTEGER NOT NULL);";

    private static final String DROP_TABLES =
            "DROP TABLE IF EXISTS pokemon;";

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
        Log.d("PokemonDB", "creating table");
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
