package com.sargis.kh.guardian.utils;

public interface Constants {

    String API_KEY = "c192f963-b123-4413-9d94-a5d10324c59d";

    interface PayloadKey {
        String RESULT = "RESULT";
    }

    interface ActionKey {

    }

    interface ResultKey {

    }

    interface RequestCode {
        int ARTICLE_VIEW_ACTIVITY_REQUEST_CODE = 0;

    }

    interface Loader {
        int ID_LOADER_PINNED_DATA = 0;
        int ID_LOADER_SAVED_DATA = 1;
        int ID_LOADER_PINNED_AND_SAVED_DATA = 2;
        int ID_LOADER_ITEM_BY_ID = 3;
    }

    interface JobScheduler {
        int JOB_SCHEDULER_ID = 11;
    }
}