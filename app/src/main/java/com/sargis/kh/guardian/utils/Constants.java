package com.sargis.kh.guardian.utils;

public interface Constants {

    String API_KEY = "c192f963-b123-4413-9d94-a5d10324c59d";

    interface PayloadKey {
        String RESULT = "RESULT";
    }

    interface ActionKey {

    }

    interface SharedPreferences {
        String NAME = "DataPref";
        String LAST_WEB_PUBLICATION_DATE = "lastWebPublicationDate";
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
        int JOB_SCHEDULER_ID = 0;
    }

    interface Notification {
        int NOTIFICATION_ID = 0;
        String NOTIFICATION_CHANNEL_NAME = "dataJobServiceChannelName";
        String NOTIFICATION_CHANNEL_ID = "dataJobServiceChannelId";
    }

}