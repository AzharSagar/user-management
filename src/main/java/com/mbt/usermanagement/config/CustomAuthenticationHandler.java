package com.mbt.usermanagement.config;



import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationHandler implements AuthenticationSuccessHandler {

//	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication auth) throws IOException, ServletException {
		
		/*if(auth.getName().equals("ADMIN")) {
	        redirectStrategy.sendRedirect(arg0, arg1, "/admin");
	        return;
	    }
        redirectStrategy.sendRedirect(arg0, arg1, "/product/add");*/
	}

}