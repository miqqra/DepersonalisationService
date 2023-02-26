package ru.nsu.backend.database;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DatabaseProperty {
    private String url;
    private String username;
    private String password;
    private String classDriver;
}
