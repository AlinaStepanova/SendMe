package com.avs.sendme;

import static android.provider.ContactsContract.CommonDataKinds.Identity.NAMESPACE;

public class Values {

    public static final int NOTIFICATION_ID             = 1;
    public static final String CHANNEL_NAME             = "SENDME";
    public static final String CHANNEL_ID               = NAMESPACE + ".sendme";
    private final String SERVER_URI                     = "https://squawkerfcmserver.udacity.com";
}
