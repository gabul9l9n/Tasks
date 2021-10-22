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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.JVM)
public class EmployeeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    String URN_employee = "/employee";

    @Test
    public void getEmployeeBySurname() throws Exception {
        this.mockMvc.perform(get(URN_employee + "?surname=ivanov"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[\n" +
                        "    {\n" +
                        "        \"id\": 1,\n" +
                        "        \"name\": \"Ivan\",\n" +
                        "        \"surname\": \"Ivanov\",\n" +
                        "        \"position\": \"Manager\",\n" +
                        "        \"salary\": 80000\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"id\": 5,\n" +
                        "        \"name\": \"Andrey\",\n" +
                        "        \"surname\": \"Ivanov\",\n" +
                        "        \"position\": \"Software Developer\",\n" +
                        "        \"salary\": 88000\n" +
                        "    }\n" +
                        "]"));
    }

    @Test
    public void getAllEmployee() throws Exception {
        this.mockMvc.perform(get(URN_employee + "/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("[Employee{id=1, name='Ivan', surname='Ivanov'}, " +
                        "Employee{id=2, name='Pyotr', surname='Petrov'}, " +
                        "Employee{id=3, name='Sasha', surname='Blinov'}, " +
                        "Employee{id=4, name='Vasya', surname='Pupkin'}, " +
                        "Employee{id=5, name='Andrey', surname='Ivanov'}]"));
    }

    @Test
    public void addEmployee() throws Exception {
        this.mockMvc.perform(post(URN_employee + "/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "\"name\": \"Aleks\",\n" +
                        "\"surname\": \"Gabul\",\n" +
                        "\"position\": \"Software Developer\",\n" +
                        "\"salary\": 130000\n" +
                        "}"))
                .andExpect(status().isOk())
                .andExpect(content().string("6"));
    }

    @Test
    public void changeSalary() throws Exception {
        this.mockMvc.perform(post(URN_employee + "/salary")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "    \"id\": 1,\n" +
                        "    \"salary\": 80000\n" +
                        "}"))
                .andExpect(status().isOk());
    }
}