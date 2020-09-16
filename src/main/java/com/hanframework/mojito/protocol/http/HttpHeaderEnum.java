package com.hanframework.mojito.protocol.http;


/**
 * @author liuxin
 * 2020-09-16 22:32
 */
public enum HttpHeaderEnum {

    ACCEPT("Accept", ""),

    ACCEPT_CHARSET("Accept-Charset", ""),

    ACCEPT_ENCODING("Accept-Encoding", ""),

    ACCEPT_LANGUAGE("Accept-Language", ""),

    ACCEPT_RANGES("Accept-Ranges", ""),

    ACCEPT_PATCH("Accept-Patch", ""),

    ACCESS_CONTROL_ALLOW_CREDENTIALS("Access-Control-Allow-Credentials", ""),

    ACCESS_CONTROL_ALLOW_HEADERS("Access-Control-Allow-Headers", ""),

    ACCESS_CONTROL_ALLOW_METHODS("Access-Control-Allow-Methods", ""),

    ACCESS_CONTROL_ALLOW_ORIGIN("Access-Control-Allow-Origin", ""),

    ACCESS_CONTROL_EXPOSE_HEADERS("Access-Control-Expose-Headers", ""),

    ACCESS_CONTROL_MAX_AGE("Access-Control-Max-Age", ""),

    ACCESS_CONTROL_REQUEST_HEADERS("Access-Control-Request-Headers", ""),

    ACCESS_CONTROL_REQUEST_METHOD("Access-Control-Request-Method", ""),

    AGE("Age", ""),

    ALLOW("", ""),

    AUTHORIZATION("", ""),

    CACHE_CONTROL("", ""),

    CONNECTION("", ""),

    CONTENT_BASE("", ""),

    CONTENT_ENCODING("", ""),

    CONTENT_LANGUAGE("", ""),

    CONTENT_LENGTH("", ""),

    CONTENT_LOCATION("", ""),

    CONTENT_TRANSFER_ENCODING("", ""),

    CONTENT_MD5("", ""),

    CONTENT_RANGE("", ""),

    CONTENT_TYPE("", ""),

    COOKIE("", ""),

    DATE("", ""),

    ETAG("", ""),

    EXPECT("", ""),

    EXPIRES("", ""),

    FROM("", ""),

    HOST("", ""),

    IF_MATCH("", ""),

    IF_MODIFIED_SINCE("", ""),

    IF_NONE_MATCH("", ""),

    IF_RANGE("", ""),

    IF_UNMODIFIED_SINCE("", ""),

    LAST_MODIFIED("", ""),

    LOCATION("", ""),

    MAX_FORWARDS("", ""),

    ORIGIN("", ""),

    PRAGMA("", ""),

    PROXY_AUTHENTICATE("", ""),

    PROXY_AUTHORIZATION("", ""),

    RANGE("", ""),

    REFERER("", ""),

    RETRY_AFTER("", ""),

    SEC_WEBSOCKET_KEY1("", ""),

    SEC_WEBSOCKET_KEY2("", ""),

    SEC_WEBSOCKET_LOCATION("", ""),

    SEC_WEBSOCKET_ORIGIN("", ""),

    SEC_WEBSOCKET_PROTOCOL("", ""),

    SEC_WEBSOCKET_VERSION("", ""),

    SEC_WEBSOCKET_KEY("", ""),

    SEC_WEBSOCKET_ACCEPT("", ""),

    SERVER("", ""),

    SET_COOKIE("", ""),

    SET_COOKIE2("", ""),

    TE("", ""),

    TRAILER("", ""),

    TRANSFER_ENCODING("", ""),

    UPGRADE("", ""),

    USER_AGENT("", ""),

    VARY("", ""),

    VIA("", ""),

    WARNING("", ""),

    WEBSOCKET_LOCATION("", ""),

    WEBSOCKET_ORIGIN("", ""),

    WEBSOCKET_PROTOCOL("", ""),

    WWW_AUTHENTICATE("", "");

    private String name;

    private String value;


    HttpHeaderEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
