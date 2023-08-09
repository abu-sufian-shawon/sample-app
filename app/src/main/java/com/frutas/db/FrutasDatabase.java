package com.frutas.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.frutas.db.entity.CartEntity;

@Database(entities = {CartEntity.class}, exportSchema = false, version = 1)
public abstract class FrutasDatabase extends RoomDatabase {
    public static FrutasDatabase mInstance = null;

    public abstract FrutasDAO frutasDAO();

    public static synchronized FrutasDatabase getInstance(Context context){
        if(mInstance == null) {
            mInstance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    FrutasDatabase.class,
                    "frutas_db"
            ).build();
        }

        return mInstance;
    }
}
