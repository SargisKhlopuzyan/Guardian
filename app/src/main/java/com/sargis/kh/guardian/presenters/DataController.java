package com.sargis.kh.guardian.presenters;

public class DataController {

    private static DataController instance = null;

    private DataController() { }

    public static DataController getInstance() {
        if (instance == null) {
            synchronized (DataController.class) {
                if (instance == null) {
                    instance = new DataController();
                }
            }
        }
        return instance;
    }

    //Loaded Tries Count
    private int loadedTriesCount = 1;

    public void increaseLoadedTriesBy(int count) {
        loadedTriesCount += count;
    }

    public void setLoadedTriesCount(int loadedTriesCount) {
        this.loadedTriesCount = loadedTriesCount;
    }

    public int getLoadedTriesCount() {
        return loadedTriesCount;
    }

}