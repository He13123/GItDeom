package com.example.gitdeom.net;

import java.io.IOException;

public interface OkHttpLo<T> {
    void onResponse(T obj);

    void onFailure(IOException obj);
}
