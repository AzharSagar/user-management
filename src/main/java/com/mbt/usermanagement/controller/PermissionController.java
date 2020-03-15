package com.mbt.usermanagement.controller;


import com.mbt.usermanagement.beans.Designation;
import com.mbt.usermanagement.beans.PermissionBean;
import com.mbt.usermanagement.beans.PermissionList;
import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.DesignationRepository;
import com.mbt.usermanagement.repository.PermissionListRepo;
import com.mbt.usermanagement.repository.PermissionRepo;
import com.mbt.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    private PermissionListRepo permissionListRepo;

    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity createPermission(@RequestBody PermissionList permissionList){

        if(permissionList == null){
            return new ResponseEntity("Empty Fields", HttpStatus.BAD_REQUEST);
        }
        if(permissionList.getType() == null){
            return new ResponseEntity("Please Mention Types", HttpStatus.BAD_REQUEST);
        }
        if(permissionList.getUrl() == null){
            return new ResponseEntity("Please URL Types", HttpStatus.BAD_REQUEST);
        }
        if(permissionListRepo.getByTypeAndUrl(permissionList.getType().toString(), permissionList.getUrl()) != null) {
            return new ResponseEntity("This type of Permission is Already Saved", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(permissionListRepo.save(permissionList));
    }

    @GetMapping("{id}")
    public ResponseEntity getPermissionById(@PathVariable Integer id){
        if(!permissionListRepo.existsById(id)){
            return new ResponseEntity("Permission Not Found", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(permissionListRepo.getOne(id));
    }



    @GetMapping("/assign1/{id}")
    public ResponseEntity getAssignPermissionByUserId(@PathVariable Integer id){
        if(!userRepository.existsById(id)){
            return new ResponseEntity("User Not Foundteytr", HttpStatus.BAD_REQUEST);
        }
        User user1 = userRepository.getOne(id);


      if((user1.getDesignation().getId()) == null){
         return new ResponseEntity<String>("designation id null",HttpStatus.BAD_REQUEST);
      }
        Designation designation = designationRepository.getOne(user1.getDesignation().getId());

        System.out.println(user1.getDesignation().getId());
        return ResponseEntity.ok(permissionRepo.getPermissonListByDesignationId(designation.getId()));

    }



    @GetMapping
    public ResponseEntity getAllPermissions(){
        return ResponseEntity.ok(permissionListRepo.findAll());
    }

    @PostMapping("/designation/delete")
    public ResponseEntity deletePermissionFromDesignation(@RequestBody PermissionBean permission){
        List<PermissionList> list = new ArrayList<>();
        if(permission.getDesignationId() == null || permission.getPermissionListId() == null){
            return new ResponseEntity("Designation Id or Permission Id not Found", HttpStatus.BAD_REQUEST);
        }
        Integer designationId = permission.getDesignationId();
        Integer permissionId =  permission.getPermissionListId();

        if(permissionRepo.getPerticularPermissionOfDesignation(permissionId,designationId) == null){
          return new ResponseEntity("Permission Not Found", HttpStatus.BAD_REQUEST);
        }
        PermissionBean permissionBean = permissionRepo.getPerticularPermissionOfDesignation(permissionId,designationId);
        permissionRepo.deleteById(permissionBean.getId());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/assign")
    public ResponseEntity assignPermission(@RequestBody PermissionBean permission){
        if(!designationRepository.existsById(permission.getDesignationId())){
            return new ResponseEntity("Designation doesn't exist!", HttpStatus.NOT_FOUND);
        }
        Designation designation = designationRepository.getOne(permission.getDesignationId());
        for(Integer i: permission.getPermissionList()){
            PermissionBean perm = new PermissionBean();
            if(!permissionListRepo.existsById(i)){
                return new ResponseEntity("Permission doesn't exist!", HttpStatus.NOT_FOUND);
            }
            PermissionList singlePermission = permissionListRepo.getOne(i);
            perm.setDesignation(designation);
            perm.setPermitList(singlePermission);
            permissionRepo.saveAndFlush(perm);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/retain")
    public ResponseEntity retainPermissions(@RequestBody PermissionBean permission){
        if(!designationRepository.existsById(permission.getDesignationId())){
            return new ResponseEntity("Designation doesn't exist!", HttpStatus.NOT_FOUND);
        }
        Designation designation = designationRepository.getOne(permission.getDesignationId());
        for(Integer i: permission.getPermissionList()){
            PermissionBean perm = new PermissionBean();
            if(permissionListRepo.existsById(i)){
                return new ResponseEntity("Permission doesn't exist!", HttpStatus.NOT_FOUND);
            }
            PermissionList singlePermission = permissionListRepo.getOne(i);
            perm.setDesignation(designation);
            perm.setPermitList(singlePermission);
            permissionRepo.save(perm);
        }
        return ResponseEntity.ok().build();
    }
}
