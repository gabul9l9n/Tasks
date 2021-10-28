package main.java.controller;

import main.java.entity.Counter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/counter")
public class CounterController {
    List<Counter> counterList = new ArrayList<>();

    @PostMapping("/new")
    public void createCounter(@RequestBody Counter counter) {
        if (counterList.size() == 0 || counterList.stream().noneMatch(c -> c.getName().equals(counter.getName().toLowerCase()))) {
            Counter c = new Counter();
            c.setName(counter.getName());
            counterList.add(counter);
        } else
            throw new RuntimeException();
    }

    @GetMapping("/{name}")
    public String getCounterByName(@PathVariable String name) {
        return counterList.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()).get(0).toString();
    }

    @DeleteMapping("/{name}")
    public void deleteCounter(@PathVariable String name) {
        counterList.remove(counterList.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()).get(0));
    }

    @PostMapping("/{name}/increase")
    public void increaseCounter(@PathVariable String name) {
        Counter counter = counterList.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()).get(0);
        counter.setCount(counter.getCount() + 1);
    }

    @PostMapping("/{name}/decrease")
    public void decreaseCounter(@PathVariable String name) {
        Counter counter = counterList.stream().filter(c -> c.getName().equals(name)).collect(Collectors.toList()).get(0);
        counter.setCount(counter.getCount() - 1);
    }

    @GetMapping("/all")
    public String getAllCounter() {
        return counterList.size() == 0 ? "There are 0 counters" : counterList.toString();
    }

    @GetMapping("/all/count")
    public int getAllCounterSum() {
        return counterList.stream().map(Counter::getCount).reduce(0, Integer::sum);
    }

    @GetMapping("/all/name")
    public String getAllCounterName() {
        return counterList.stream().map(Counter::getName).collect(Collectors.toList()).toString();
    }
}