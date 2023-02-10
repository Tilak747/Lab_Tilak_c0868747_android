package c0868747.tilak.labtest.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import c0868747.tilak.labtest.model.FavLocation;

@Database(entities = {FavLocation.class},version = 1,exportSchema = false)
public abstract class MyDB extends RoomDatabase {

    public abstract MyDao myDao();

}
