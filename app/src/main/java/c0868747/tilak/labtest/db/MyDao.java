package c0868747.tilak.labtest.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import c0868747.tilak.labtest.model.FavLocation;

@Dao
public interface MyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addLocation(FavLocation category);

    @Query("Select * from fav_places")
    LiveData<List<FavLocation>> getAllLocations();
    @Query("Select * from fav_places where title == :name")
    LiveData<List<FavLocation>> getAllLocations(String name);

    @Delete
    void deleteLocation(FavLocation category);

    @Query("DELETE FROM fav_places")
    void deleteAll();

}
