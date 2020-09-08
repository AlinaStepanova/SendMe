package com.avs.sendme.provider;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(
        authority = SendMeProvider.AUTHORITY,
        database = SendMeDatabase.class)
public final class SendMeProvider {

    public static final String AUTHORITY = "com.avs.sendme.provider";


    @TableEndpoint(table = SendMeDatabase.SEND_ME_MESSAGES)
    public static class SendMeMessages {

        @ContentUri(
                path = "messages",
                type = "vnd.android.cursor.dir/messages",
                defaultSort = SendMeContract.COLUMN_DATE + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/messages");
    }

    @TableEndpoint(table = SendMeDatabase.SEND_ME_TEST)
    public static class SendMeTest {

        @ContentUri(
                path = "test",
                type = "vnd.android.cursor.dir/test",
                defaultSort = SendMeContractTest.COLUMN_ID + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/test");
    }
}
