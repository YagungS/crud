package com.pract.crud.util;

public interface ErrorCodes {
    public static final int CODE_NOT_FOUND = 3000;

    public static final int CODE_IS_EXIST = 30001;

    public static final int CODE_BAD_REQUEST = 30002;

    public static final int CODE_INTERNAL_ERROR = 80000;

    public static final String MSG_NOT_FOUND = "Cannot find resource with id %d";

    public static final String MSG_IS_EXIST = "Record with unique value %s already exist in the system";

    public static final String MSG_BAD_REQUEST = "Invalid value for field %s, rejected value %s";

    public static final String MSG_INTERNAL_ERROR = "System error, we're unable to process your request at the moment";


}
