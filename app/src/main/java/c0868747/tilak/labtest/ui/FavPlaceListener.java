package c0868747.tilak.labtest.ui;

import c0868747.tilak.labtest.model.FavLocation;

public interface FavPlaceListener {
    void viewLocation(FavLocation location,int position);
    void delete(FavLocation location,int position);
}
