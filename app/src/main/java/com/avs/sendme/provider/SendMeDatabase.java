package com.avs.sendme.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = SendMeDatabase.VERSION)
public class SendMeDatabase {

    public static final int VERSION = 1;

    @Table(SendMeContract.class)
    public static final String SEND_ME_MESSAGES = "send_me_messages";

}
