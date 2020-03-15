package com.mbt.usermanagement.service;


import com.mbt.usermanagement.beans.PermissionBean;
import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.PermissionListRepo;
import com.mbt.usermanagement.repository.PermissionRepo;
import com.mbt.usermanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@Service("AuthorityService")

public class AuthorityService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PermissionRepo permissionRepo;

	@Autowired
	private PermissionListRepo permissionListRepo;

	public AuthorityService() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}


	public boolean hasAuthority(  String pri,  String permit, String type) {

		User user = userRepository.findByEmail(pri);
		int count = 0;
		Boolean b = false;

		for (PermissionBean i : permissionRepo.getPermissionByDesignation(user.getDesignation().getId())) {
			PermissionBean p = permissionRepo.getOne(i.getId());
			if(p.getPermitList().getUrl()!=null){
				if (permit.contains(p.getPermitList().getUrl()) && type.contains(p.getPermitList().getType().toString()) || b) {
					b = true;
					break;
				}
			}
		}
		return b;
	}
}
