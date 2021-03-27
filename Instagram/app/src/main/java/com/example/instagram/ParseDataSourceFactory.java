package com.example.instagram;

import androidx.annotation.NonNull;
import androidx.paging.DataSource;

public class ParseDataSourceFactory extends DataSource.Factory<Integer, Post>{

    public ParsePositionalDataSource dataSource;

    @NonNull
    @Override
    public DataSource<Integer, Post> create() {
        dataSource = new ParsePositionalDataSource();
        return dataSource;
    }
}
