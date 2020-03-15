package com.mbt.usermanagement.controller;

import com.mbt.usermanagement.beans.Department;
import com.mbt.usermanagement.beans.Designation;
import com.mbt.usermanagement.repository.DepartmentRepository;
import com.mbt.usermanagement.repository.DesignationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/designation")
public class DesignationController {

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @PostMapping
    public ResponseEntity saveDesignation(@RequestBody Designation designation){
        if(designation == null){
            return new ResponseEntity("Please fill the fields", HttpStatus.BAD_REQUEST);
        }

        if(designation.getDepartmentId()!=null){
            if(!departmentRepository.existsById(designation.getDepartmentId())){
                return new ResponseEntity("Department Not Found", HttpStatus.BAD_REQUEST);
            } else {
                designation.setDepartment(departmentRepository.getOne(designation.getDepartmentId()));
            }
        }
        Department department = departmentRepository.getOne(designation.getDepartmentId());
        designation.setDepartmentName(department.getDepartmentName());

      return ResponseEntity.ok(designationRepository.save(designation));
    }

    @GetMapping("{id}")
    public ResponseEntity getDesignation(@PathVariable Integer id){
        if(designationRepository.existsById(id)){

            Designation d = designationRepository.getOne(id);
            d.setDepartmentId(d.getDepartment().getId());
            return ResponseEntity.ok(d);
        }
        else return null;
    }

    @GetMapping
    public ResponseEntity getAllDesignations(){
        List<Designation> list = new ArrayList();
        designationRepository.findAllByOrderByIdDesc().forEach(i->{
            Designation d = new Designation();
            if(i.getDesignation()!=null){
                if(!i.getDesignation().equals("ADMIN")){
                    d.setId(i.getId());
                    d.setDesignation(i.getDesignation());
                    d.setDepartmentName(i.getDepartmentName());
                    d.setDepartment(i.getDepartment());
                    d.setDepartmentId(i.getDepartment().getId());
                    d.setActive(i.getActive());
                    list.add(d);
                }
            }

        });
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteDesignation(@PathVariable Integer id){
       if(!designationRepository.existsById(id)){
           return new ResponseEntity("Designation Not Found", HttpStatus.BAD_REQUEST);
       }
       designationRepository.deleteById(id);
       return ResponseEntity.ok().build();
    }

    @GetMapping("/department/{id}")
    public ResponseEntity getDepartmentWiseDesignation(@PathVariable Integer id){
        return ResponseEntity.ok(designationRepository.findByDepartment(departmentRepository.getOne(id)));
    }

    @PutMapping
    public ResponseEntity updateDesignation(@RequestBody Designation designation){
        if(designation.getId() == null){
            return new ResponseEntity("Designation Id is Null", HttpStatus.BAD_REQUEST);
        }
        else if(!designationRepository.existsById(designation.getId())){
            return new ResponseEntity("Designation Not Found", HttpStatus.BAD_REQUEST);
        }
        if(designation.getDepartmentId() != null){
            if(!departmentRepository.existsById(designation.getDepartmentId())){

            }else{
                designation.setDepartment(departmentRepository.getOne(designation.getDepartmentId()));
                Department department = departmentRepository.getOne(designation.getDepartmentId());
                designation.setDepartmentName(department.getDepartmentName());
            }
        }
        return ResponseEntity.ok(designationRepository.saveAndFlush(designation));
    }



}
