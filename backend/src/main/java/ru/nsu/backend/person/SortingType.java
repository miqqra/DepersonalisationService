package ru.nsu.backend.person;

public enum SortingType {
    ASC(1), DESC(-1);

    public final int sortType;

    SortingType(int sortType) {
        this.sortType = sortType;
    }
}
