package com.mbt.usermanagement.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbt.usermanagement.beans.UploadFileResponse;
import com.mbt.usermanagement.beans.User;
import com.mbt.usermanagement.repository.DepartmentRepository;
import com.mbt.usermanagement.repository.DesignationRepository;
import com.mbt.usermanagement.repository.UserRepository;
import com.mbt.usermanagement.service.AuthorityService;
import com.mbt.usermanagement.service.FileStorageService;
import com.mbt.usermanagement.service.JWTService;
import com.mbt.usermanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import org.springframework.core.io.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
	
	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JWTService jwtService;

	@Autowired
	private DepartmentRepository departmentRepository;

	@Autowired
	private DesignationRepository designationRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageService fileStorageService;



	@PostMapping("/api/user")
	public ResponseEntity<?> saveUser(@RequestParam String user, @RequestParam(value = "file") MultipartFile file) throws Exception {

		User user2 = new User();
		ObjectMapper mapper = new ObjectMapper();
		User user1 = mapper.readValue(user, User.class);

        if(user1.getEmail()==null){
            return new ResponseEntity<String>("User Email Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(user1.getDesignationId()==null){
            return new ResponseEntity<String>("User Designation Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(user1.getDepartmentId()==null){
            return new ResponseEntity<String>("User Department Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(user1.getPassword() == null){
            return new ResponseEntity<String>("Password Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(file == null){
            return new ResponseEntity<String>("Please Select Profile Picture", HttpStatus.BAD_REQUEST);
        }
        else if(user1.getContact() == null){
            return new ResponseEntity<String>("Contact Not Found", HttpStatus.BAD_REQUEST);
        }
        else if(user1.getName() == null){
            return new ResponseEntity<String>("Please Enter Name Of User", HttpStatus.BAD_REQUEST);
        }
        else if(departmentRepository.getOne(user1.getDepartmentId()) == null){
			return new ResponseEntity<String>("Department Not Found", HttpStatus.BAD_REQUEST);
		}
        else if(designationRepository.getOne(user1.getDesignationId()) == null){
			return new ResponseEntity<String>("Designation Not Found", HttpStatus.BAD_REQUEST);
		}

		User user3 = userService.findUserByEmail(user1.getEmail());
        if(user3 != null){
			return new ResponseEntity<String>("Email Already Exists!", HttpStatus.BAD_REQUEST);
		}


		user2.setDepartment(departmentRepository.getOne(user1.getDepartmentId()));
		user2.setDesignation(designationRepository.getOne(user1.getDesignationId()));
		user2.setDesignationName(designationRepository.getOne(user1.getDesignationId()).getDesignation());
		user2.setPassword(passwordEncoder.encode(user1.getPassword()));
		user2.setContact(user1.getContact());
		user2.setName(user1.getName());
		user2.setEmail(user1.getEmail());
		if(user1.getAboutDesc()!=null){
			user2.setAboutDesc(user1.getAboutDesc());
		}
		if(user1.getAddress()!=null){
			user2.setAddress(user1.getAddress());
		}
		if(user1.getAltContact()!=null){
			user2.setAltContact(user1.getAltContact());
		}
		if(user1.getAltEmail()!=null){
			user2.setAltEmail(user1.getAltEmail());
		}

		user2.setActive(1);



		String fileName = fileStorageService.storeFile(file);


		user2.setImage(fileName);




		return ResponseEntity.ok(userService.addUser(user2));
	}


	
	@GetMapping("/api/user/{id}")
	public ResponseEntity<?> getUser(@PathVariable Integer id){
		if(userService.getUser(id) == null) {
			return new ResponseEntity<String>("User Not Exists", HttpStatus.BAD_REQUEST);
		}
		return ResponseEntity.ok(userService.getUser(id));
	}
	
	@DeleteMapping("/api/user/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Integer id){

		if(userService.getUser(id) == null) {
			return new ResponseEntity<String>("User not Existed", HttpStatus.BAD_REQUEST);
		}
		userService.deleteUser(id);
		return ResponseEntity.ok().build();
	}


	@PostMapping("/api/user/change-designation")
	public ResponseEntity updateDesignationOfUser(@RequestBody User user){
		if(user.getId()==null){
			return new ResponseEntity<String>("User Id is Null", HttpStatus.BAD_REQUEST);
		}
		else if(!userRepository.existsById(user.getId())){
			return new ResponseEntity<String>("User Id Null", HttpStatus.BAD_REQUEST);
		}
		else if(user.getDesignationId()==null){
			return new ResponseEntity<String>("Designation Id is Null", HttpStatus.BAD_REQUEST);
		}
		else if(!designationRepository.existsById(user.getDesignationId())){
			return new ResponseEntity<String>("Designation is Null", HttpStatus.BAD_REQUEST);
		}
		User user1 = userRepository.getOne(user.getId());
		if(user1.getDesignation().equals("ADMIN")){
			return new ResponseEntity<String>("Designation is Admin can not be Change", HttpStatus.BAD_REQUEST);
		}
		user1.setDesignation(designationRepository.getOne(user.getDesignationId()));
		user1.setDesignationName(designationRepository.getOne(user.getDesignationId()).getDesignation());
		return ResponseEntity.ok(userRepository.save(user1));
	}




	//update user account for particular user
	@PostMapping("/api/user/update")
	public ResponseEntity<?> updateUser(@RequestParam String user, @RequestParam(value = "file") MultipartFile file) throws Exception {
		User user2 = new User();
		ObjectMapper mapper = new ObjectMapper();
		User user1 = mapper.readValue(user, User.class);
		if(user1.getId() == null){
			return new ResponseEntity<String>("User Id is Null", HttpStatus.BAD_REQUEST);
		}
		if(user1.getEmail()!=null){
			user2.setEmail(user1.getName());
			User user3 = userService.findUserByEmail(user1.getEmail());
			/*if(user3.getEmail() != user2.getEmail()){
				if(user3 != null){
					return new ResponseEntity<String>("Email Already Exists!", HttpStatus.BAD_REQUEST);
				}
			}*/
		}
		if(user1.getDesignationId()!=null){
			if(designationRepository.getOne(user1.getDesignationId()) == null){
				return new ResponseEntity<String>("Designation Not Found", HttpStatus.BAD_REQUEST);
			}else{
				user2.setDesignation(designationRepository.getOne(user1.getDesignationId()));
				user2.setDesignationName(designationRepository.getOne(user1.getDesignationId()).getDesignation());

			}

		}
		if(user1.getDepartmentId()!=null){
			if(departmentRepository.getOne(user1.getDepartmentId()) == null){
				return new ResponseEntity<String>("Department Not Found", HttpStatus.BAD_REQUEST);
			}else{
				user2.setDepartment(departmentRepository.getOne(user1.getDepartmentId()));
			}
		}
		if(user1.getPassword() != null){
			user2.setPassword(passwordEncoder.encode(user1.getPassword()));
		}
		 if(file != null){
			 String fileName = fileStorageService.storeFile(file);
			 user2.setImage(fileName);
		}
		if(user1.getContact() != null){
			user2.setContact(user1.getContact());
		}
		if(user1.getName() != null){
			user2.setName(user1.getName());
		}
		if(user1.getAboutDesc()!=null){
			user2.setAboutDesc(user1.getAboutDesc());
		}
		if(user1.getAddress()!=null){
			user2.setAddress(user1.getAddress());
		}
		if(user1.getAltContact()!=null){
			user2.setAltContact(user1.getAltContact());
		}
		if(user1.getAltEmail()!=null){
			user2.setAltEmail(user1.getAltEmail());
		}
		user2.setActive(1);
		return ResponseEntity.ok(userRepository.save(user2));
	}


	@GetMapping("/api/user")
	public ResponseEntity<?> getAllUsers(){
		Authentication authentication = jwtService.getCurrentUser();
		if(!userService.findUserByEmail(authentication.getPrincipal().toString()).getDesignation().getDesignation().equals("ADMIN")){
			return new ResponseEntity<String>("You Have Not Authority to view user", HttpStatus.BAD_REQUEST);
		}
		User user = userService.findUserByEmail(authentication.getPrincipal().toString());
		List<User> list= new ArrayList<>();
		if(user.getDesignation().getDesignation().toString().equals("ADMIN")){
			userService.getAllUsers().forEach(i->{
				i.setPassword(null);
				if(i.getDesignation()!=null){
					i.setDesignationId(i.getDesignation().getId());
				}
				if(i.getDepartment()!=null){
					i.setDepartmentId(i.getDepartment().getId());
				}
				list.add(i);
			});
		}
		return ResponseEntity.ok(list);
	}


	@GetMapping("/api/user/{id}/{status}")
	public ResponseEntity setUserEnable(@PathVariable Integer id, @PathVariable String status){
		if(!userRepository.existsById(id)){
			return new ResponseEntity("User Not Found", HttpStatus.BAD_REQUEST);
		}
		if(status.equals("enable")){
			userRepository.setUserEnable(id);
			return ResponseEntity.ok(userRepository.getOne(id));
		}
		else if(status.equals("disable")){
			userRepository.setUserDisable(id);
			return ResponseEntity.ok(userRepository.getOne(id));
		}
		else return null;
	}



	//@GetMapping("/api/downloadfile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {


		// Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) { }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);

    }
    @PostMapping("/api/user/api/uploadfile")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = fileStorageService.storeFile(file);



        String fileDownloadUri = ServletUriComponentsBuilder.
                fromCurrentContextPath().
                path("/downloadfile/")
                .path(fileName).toUriString();



        return new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @GetMapping("/api/user/downloadfile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile1(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        System.out.println();
        Resource resource = fileStorageService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {

        }

        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }



	
	
	/*@PostMapping("/api/user/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {

		if(user == null) {
			return new ResponseEntity<String>("Fields Empty", HttpStatus.BAD_REQUEST);
		}
		
		if(userService.findUserByEmail(user.getEmail()) != null){
			return new ResponseEntity<String>("Email Already exists", HttpStatus.BAD_REQUEST);
		}
        else
        {
            ConfirmationToken confirmationToken = new ConfirmationToken(user);

          //  tokenRepo.save(confirmationToken);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(user.getEmail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("");
            mailMessage.setText("To confirm your account, please click here : "
            +"http://localhost:3400/confirm-account?token="+confirmationToken);

            serviceImpl.sendEmail(mailMessage);

        }

        return ResponseEntity.ok().build();
    }
*/





}
