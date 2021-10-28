package main.java.entity;

import lombok.*;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Counter {
    private String name;
    private int count;
}