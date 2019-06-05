package ru.hse.mnmalysheva.ftpgui;

class Query {
    final QueryType type;
    final String path;

    Query(QueryType type, String path) {
        this.type = type;
        this.path = path;
    }
}
