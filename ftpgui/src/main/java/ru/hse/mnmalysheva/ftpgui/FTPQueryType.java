package ru.hse.mnmalysheva.ftpgui;

/** This class represents FTP query type. **/
enum FTPQueryType {
    LIST(FTPQueryType.LIST_CODE), GET(FTPQueryType.GET_CODE);

    private static final int LIST_CODE = 1;
    private static final int GET_CODE = 2;
    private final int code;

    FTPQueryType(int code) {
        this.code = code;
    }

    /** Returns query code. **/
    int getCode() {
        return code;
    }

    /**
     * Converts query code to FTPQueryType.
     * @throws IllegalArgumentException if code is not supported.
     **/
    static FTPQueryType fromCode(int code) {
        switch (code) {
            case LIST_CODE:
                return LIST;
            case GET_CODE:
                return GET;
            default:
                throw new IllegalArgumentException("Code is not supported");
        }
    }
}
