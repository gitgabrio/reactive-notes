package com.example;

import org.junit.Test;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static junit.framework.Assert.assertTrue;

public class RxJavaUnitTest {
    String result="";

    // Simple subscription to a fix value
    @Test
    public void returnAValue(){
        result = "";
        Observable<String> observer = Observable.just("Hello"); // provides datea
        observer.subscribe(s -> result=s); // Callable as subscriber
        assertTrue(result.equals("Hello"));
    }

    public List<Todo> getTodos() {
        List<Todo> todosFromWeb = new ArrayList<>();
        IntStream.range(0, 50).forEach(value -> todosFromWeb.add(new Todo()));
        return todosFromWeb;
    }
}