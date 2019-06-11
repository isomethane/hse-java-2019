package ru.hse.mnmalysheva.ftpgui;

public enum FTPQueryType {
    LIST(1), GET(2);

    private final int code;

    FTPQueryType(int code) {
        this.code = code;
    }

    int getCode() {
        return code;
    }

    static FTPQueryType fromCode(int code) {
        if (code == LIST.code) {
            return LIST;
        }
        if (code == GET.code) {
            return GET;
        }
        throw new IllegalArgumentException("Code is not supported");
    }
}
