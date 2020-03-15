package com.mbt.usermanagement.util;


import com.mbt.usermanagement.beans.*;
import com.mbt.usermanagement.repository.DesignationRepository;
import com.mbt.usermanagement.repository.PermissionListRepo;
import com.mbt.usermanagement.repository.PermissionRepo;
import com.mbt.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class PreRequists extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DesignationRepository designationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private PermissionRepo permissionRepo;

    @Autowired
    private PermissionListRepo permissionListRepo;

    public void createRole(){
        Designation designation = new Designation();
        designation.setDesignation("ADMIN");
        designationRepository.save(designation);
    }

    public void createPermissionList() {

        PermissionList plist = new PermissionList();
        plist.setType(ApiType.POST);
        plist.setUrl("/api/");
        plist.setDescription("All Access");
        permissionListRepo.save(plist);

        PermissionList plist1 = new PermissionList();
        plist1.setType(ApiType.DELETE);
        plist1.setUrl("/api/");
        plist1.setDescription("All Access");
        permissionListRepo.save(plist1);

        PermissionList plist2 = new PermissionList();
        plist2.setType(ApiType.PUT);
        plist2.setUrl("/api/");
        plist2.setDescription("All Access");
        permissionListRepo.save(plist2);

        PermissionList plist3 = new PermissionList();
        plist3.setType(ApiType.GET);
        plist3.setUrl("/api/");
        plist3.setDescription("All Access");
        permissionListRepo.save(plist3);



    }



    public void createPermission(){
        List<PermissionList> list = new ArrayList<>();
        for (PermissionList s : permissionListRepo.findAll()) {
            PermissionBean permission = new PermissionBean();
            permission.setId(1);
            permission.setDesignation(designationRepository.findByDesignation("ADMIN"));
            permission.setPermitList(s);
            permissionRepo.saveAndFlush(permission);
        }
    }


    public void createUser(){
        User user = new User();
        user.setActive(1);
        user.setEmail("ali@gmail.com");
        user.setName("MBT");
        user.setPassword("12345678");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setDesignation(designationRepository.findByDesignation("ADMIN"));
        user.setDesignationName("ADMIN");
        userRepository.save(user);
    }

}


