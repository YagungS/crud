package com.pract.crud.util;

import java.util.Map;

public interface Constant {
    Map<String, String> INITIAL_SETTINGS= Map.of(
            "biometric_login", "false",
        "push_notification", "false",
        "sms_notification", "false",
        "show_onboarding", "false",
        "widget_order", "1,2,3,4,5");

    String WIDGEDT_ORDER = "widget_order";
}
