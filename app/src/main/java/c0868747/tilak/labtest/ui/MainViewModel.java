package c0868747.tilak.labtest.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import c0868747.tilak.labtest.model.FavLocation;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class MainViewModel extends ViewModel {

    MainRepo repo;

    @Inject
    public MainViewModel(MainRepo repo){
        this.repo = repo;
    }


    public LiveData<List<FavLocation>> getAllLocations(String name){
        if(name.isEmpty()){
            return repo.getAllLocations();
        }
        return repo.getAllLocations(name);
    }

    public void addLocation(FavLocation location){
        repo.addLocation(location);
    }
    public void deleteLocation(FavLocation location){
        repo.deleteLocation(location);
    }

}
