package c0868747.tilak.labtest.ui;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import c0868747.tilak.labtest.db.MyDao;
import c0868747.tilak.labtest.model.FavLocation;

public class MainRepo {

    MyDao myDao;
    ExecutorService executorService;

    @Inject
    public MainRepo(MyDao myDao,ExecutorService executorService){
        this.myDao = myDao;
        this.executorService = executorService;
    }

    public LiveData<List<FavLocation>> getAllLocations(){
        return myDao.getAllLocations();
    }
    public LiveData<List<FavLocation>> getAllLocations(String name){
        return myDao.getAllLocations(name);
    }

    public void addLocation(FavLocation location){
        executorService.execute( () -> {
            myDao.addLocation(location);
        });
    }
    public void deleteLocation(FavLocation location){
        executorService.execute( () -> {
            myDao.deleteLocation(location);
        });
    }

}
