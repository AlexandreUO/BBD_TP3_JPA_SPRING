package com.isep.testjpa.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.isep.testjpa.repository.EmpRepository;
import com.isep.testjpa.model.Emp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class SimpleController {

    @Autowired
    private EmpRepository empRepository;

    @RequestMapping(value="/", method= RequestMethod.GET)
    public String hello(@Param("name") String name) {
        return "Hello " + name;
    }

    @RequestMapping(value="/employees", method= RequestMethod.GET)
    public List<Emp> getEmployees() {
        return empRepository.findAll();
    }

    //Récupérer un employee par son Id
    @GetMapping(value="/employees/{id}")
    public List<Emp> getEmployeeById(@PathVariable long id) {
        List<Long> myLittleWorkaroung = new ArrayList<Long>();
        myLittleWorkaroung.add(id);
        return empRepository.findAllById(myLittleWorkaroung);
    }

    @PostMapping(value="/employees")
    public Emp addEmployee(@RequestBody Emp emp){
        return empRepository.save(emp);
    }

    @PutMapping("/employees/{id}")
    public Emp replaceEmployee(@RequestBody Emp newEmployee, @PathVariable Long id) {
        return empRepository.findById(id)
                .map(employee -> {
                    employee.setEname(newEmployee.getEname());
                    employee.setEfirst(newEmployee.getEfirst());
                    employee.setMgr(newEmployee.getMgr());
                    employee.setJob(newEmployee.getJob());
                    employee.setSal(newEmployee.getSal());
                    return empRepository.save(employee);
                })
                .orElseGet(() -> {
                    newEmployee.setEmpno(id);
                    return empRepository.save(newEmployee);
                });
    }

    @DeleteMapping(value="/employees/{id}")
    public void deleteEmployee(@PathVariable long id) {
        empRepository.deleteById(id);
    }
}
