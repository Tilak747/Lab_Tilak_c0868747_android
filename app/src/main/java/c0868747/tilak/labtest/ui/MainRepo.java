package c0868747.tilak.labtest.ui;

import java.util.concurrent.ExecutorService;

import javax.inject.Inject;

import c0868747.tilak.labtest.db.MyDao;

public class MainRepo {

    MyDao myDao;
    ExecutorService executorService;

    @Inject
    public MainRepo(MyDao myDao,ExecutorService executorService){
        this.myDao = myDao;
        this.executorService = executorService;
    }

}
