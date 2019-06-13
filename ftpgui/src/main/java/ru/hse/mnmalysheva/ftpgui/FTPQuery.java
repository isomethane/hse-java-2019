package ru.hse.mnmalysheva.ftpgui;

import org.jetbrains.annotations.NotNull;

/** This class represents FTP query. **/
class FTPQuery {
    /** Query type. **/
    final FTPQueryType type;
    /** File path. **/
    final String path;

    FTPQuery(@NotNull FTPQueryType type, @NotNull String path) {
        this.type = type;
        this.path = path;
    }
}
