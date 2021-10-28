package main.java;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.JVM)
public class CounterControllerTest {
    private final String URN_COUNTER = "/counter";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createCounter() throws Exception {
        this.mockMvc.perform(post(URN_COUNTER + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"counter1\"}"))
                .andExpect(status().isOk());
        this.mockMvc.perform(post(URN_COUNTER + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"counter5\"}"))
                .andExpect(status().isOk());
    }

    @Test(expected = NestedServletException.class)
    public void createAlreadyExistingCounter() throws Exception {
        this.mockMvc.perform(post(URN_COUNTER + "/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"counter1\"}")
                )
                .andExpect(status().is5xxServerError());
    }

    @Test
    public void getCounterByName() throws Exception {
        this.mockMvc.perform(get(URN_COUNTER + "/counter1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Counter(name=counter1, count=0)"));
    }

    @Test
    public void deleteCounter() throws Exception {
        this.mockMvc.perform(delete(URN_COUNTER + "/counter1"))
                .andExpect(status().isOk());
    }

    @Test
    public void increaseCounter() throws Exception {
        this.mockMvc.perform(post(URN_COUNTER + "/counter5/increase"))
                .andExpect(status().isOk());
    }

    @Test
    public void decreaseCounter() throws Exception {
        this.mockMvc.perform(post(URN_COUNTER + "/counter5/decrease"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllCounter() throws Exception {
        this.mockMvc.perform(get(URN_COUNTER + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("[Counter(name=counter5, count=0)]"));
    }

    @Test
    public void getAllCounterSum() throws Exception {
        this.mockMvc.perform(get(URN_COUNTER + "/all/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("0"));
    }

    @Test
    public void getAllCounterName() throws Exception {
        this.mockMvc.perform(get(URN_COUNTER + "/all/name"))
                .andExpect(status().isOk())
                .andExpect(content().string("[counter5]"));
    }
}