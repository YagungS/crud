package com.pract.crud.util;

public interface ErrorMsg {

    String MSG_NOT_FOUND = "Cannot find resource with id %d";

    String MSG_IS_EXIST = "Record with unique value %s already exist in the system";

    String MSG_BAD_REQUEST = "Invalid value for field %s, rejected value %s";

    String MSG_INTERNAL_ERROR = "System error, we're unable to process your request at the moment";

}
