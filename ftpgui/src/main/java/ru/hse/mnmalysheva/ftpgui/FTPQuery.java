package ru.hse.mnmalysheva.ftpgui;

class FTPQuery {
    final FTPQueryType type;
    final String path;

    FTPQuery(FTPQueryType type, String path) {
        this.type = type;
        this.path = path;
    }
}
