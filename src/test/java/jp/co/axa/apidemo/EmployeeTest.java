package jp.co.axa.apidemo;


import com.fasterxml.jackson.databind.ObjectMapper;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.security.TokenGenerator;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class EmployeeTest {

    private Long NOT_EXIST_EMPID = -1L;
    private MockMvc mockMvc;
    private String AUTH_USER_NAME = "user1";
    private Employee testEmployee;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private TokenGenerator tokenGenerator;

    @BeforeEach
    public void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(springSecurityFilterChain).build();
        this.testEmployee = new Employee();
        this.testEmployee.setName("employee1");
        this.testEmployee.setDepartment("department1");
        this.testEmployee.setSalary(10000);
    }

    // Test if call api without authentication
    @Test
    public void testCallGetEmployeesWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/api/v1/employees")).andExpect(status().isForbidden());
    }

    @Test
    public void testCallGetEmployeeWithoutAuth() throws Exception {
        this.mockMvc.perform(get("/api/v1/employee/{employeeId}", NOT_EXIST_EMPID)).andExpect(status().isForbidden());
    }

    @Test
    public void testCallPostEmployeeWithoutAuth() throws Exception {
        this.mockMvc.perform(post("/api/v1/employees")).andExpect(status().isForbidden());
    }

    @Test
    public void testCallPutEmployeeWithoutAuth() throws Exception {
        this.mockMvc.perform(put("/api/v1/employees/{employeeId}", NOT_EXIST_EMPID)).andExpect(status().isForbidden());
    }

    @Test
    public void testCallDeletEmployeeWithoutAuth() throws Exception {
        this.mockMvc.perform(put("/api/v1/employees/{employeeId}", NOT_EXIST_EMPID)).andExpect(status().isForbidden());
    }
    // End test.


    @Test
    public void testGetEmployeesWithToken() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/employees")
                        .header("Authorization", getToken(AUTH_USER_NAME)))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetEmployeeWithToken() throws Exception {
        this.mockMvc.perform(
                get("/api/v1/employees/{employeeId}", NOT_EXIST_EMPID)
                        .header("Authorization", getToken(AUTH_USER_NAME)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostEmployeeWithToken() throws Exception {
        this.mockMvc.perform(
                post("/api/v1/employees").characterEncoding("UTF-8")
                        .header("Authorization", getToken(AUTH_USER_NAME))
                        .content(new ObjectMapper().writeValueAsString(this.testEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name").value(testEmployee.getName()))
                .andExpect(jsonPath("$.department").value(testEmployee.getDepartment()))
                .andExpect(jsonPath("$.salary").value(testEmployee.getSalary()))
        ;
    }

    @Test
    public void testPutNonExistingEmployeeWithToken() throws Exception {
        testEmployee.setId(1000L);
        this.mockMvc.perform(
                put("/api/v1/employees/{empId}",testEmployee.getId()).characterEncoding("UTF-8")
                        .header("Authorization", getToken(AUTH_USER_NAME))
                        .content(new ObjectMapper().writeValueAsString(this.testEmployee))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    public void testPutExistingEmployeeWithToken() throws Exception {
        assertTrue(false,"Need more time to mock data or integrate with a persistent database");
    }

    @Test
    public void testDeleteNonExistEmployee() throws Exception {
        this.testEmployee.setId(1000L);
        this.mockMvc.perform(
                delete("/api/v1/employees/{empId}",testEmployee.getId())
                        .characterEncoding("UTF-8")
                        .header("Authorization", getToken(AUTH_USER_NAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }

    @Test
    public void testDeleteNExistEmployee() throws Exception {
        this.testEmployee.setId(1000L);
        this.mockMvc.perform(
                delete("/api/v1/employees/{empId}",testEmployee.getId())
                        .characterEncoding("UTF-8")
                        .header("Authorization", getToken(AUTH_USER_NAME))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
        ;
    }


    private String getToken(String userName) {
        return "Bearer " + tokenGenerator.generateToken(userName);
    }
}
