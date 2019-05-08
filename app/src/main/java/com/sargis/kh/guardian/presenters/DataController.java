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

    private int page = 1;
    public void increasePageBy(int count) {
        page += count;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }


    private int pageSize = 20;

    public int getPageSize() {
        return pageSize;
    }

}