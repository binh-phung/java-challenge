package jp.co.axa.apidemo.controllers;

import javassist.NotFoundException;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        Employee employee =  employeeService.getEmployee(employeeId);
        if(employee != null)
            return ResponseEntity.ok(employee);
        else
            return  ResponseEntity.notFound().build();
    }

    @PostMapping("/employees")
    public  ResponseEntity<Employee> addEmployee(@RequestBody  Employee employee){
        employeeService.addEmployee(employee);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(employee.getId())
                .toUri();
        return ResponseEntity.created(location).body(employee);
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        if(employeeService.isExisting(employeeId)){
            this.employeeService.deleteEmployee(employeeId);

        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        employee.setId(employeeId);
        if(employeeService.isExisting(employeeId)){
            this.employeeService.updateEmployee(employee);
            return ResponseEntity.ok().build();
        }
        else
            return ResponseEntity.notFound().build();
    }

}
