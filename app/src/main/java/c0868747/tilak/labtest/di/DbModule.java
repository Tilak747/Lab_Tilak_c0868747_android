package c0868747.tilak.labtest.di;

import android.content.Context;

import androidx.room.Room;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import c0868747.tilak.labtest.db.MyDB;
import c0868747.tilak.labtest.db.MyDao;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@InstallIn(SingletonComponent.class)
@Module()
public class DbModule {

    @Provides
    @Singleton
    MyDB provideDB(@ApplicationContext Context context) {
        return Room.databaseBuilder(context, MyDB.class, "AndroidLabTestDB")
//                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @Singleton
    MyDao provideDbDao(MyDB appDatabase) {
        return appDatabase.myDao();
    }

    private static final int NUMBER_OF_THREADS = 4;
    @Provides
    @Singleton
    ExecutorService provideExecutorService(MyDB appDatabase){
        return Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    }

}
