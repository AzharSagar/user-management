package com.mbt.usermanagement.controller;


import com.mbt.usermanagement.beans.Department;
import com.mbt.usermanagement.repository.DepartmentRepository;
import com.mbt.usermanagement.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/department")
public class DepartmentController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @PostMapping
    public ResponseEntity saveOrUpdateDepartment(@RequestBody Department department){
        if(department == null){
            return  new ResponseEntity("Department fields are Empty", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(departmentRepository.save(department));
    }
    @PutMapping
    public ResponseEntity updateDepartment(@RequestBody Department department){
        if(department == null){
            return  new ResponseEntity("Department fields are Empty", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(departmentRepository.save(department));
    }


    @DeleteMapping("{id}")
    public ResponseEntity deleteDepartment(@PathVariable Integer id){
        if(!departmentRepository.existsById(id)){
            return new ResponseEntity("Department Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(designationRepository.getDesignationsByDepartmentId(id).size()!=0){
            return new ResponseEntity("This Department has Designations Plz Delete its Designations First", HttpStatus.BAD_REQUEST);
        }
        departmentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("{id}")
    public ResponseEntity getDepartment(@PathVariable Integer id){
        if(departmentRepository.existsById(id)){
            return ResponseEntity.ok(departmentRepository.getOne(id));
        }
        else return null;
    }

    @GetMapping
    public ResponseEntity getAllDepartments(){
        return ResponseEntity.ok(departmentRepository.findAll());
    }

}
