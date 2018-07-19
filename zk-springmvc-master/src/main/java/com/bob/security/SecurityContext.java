package com.bob.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContext {

	@SuppressWarnings("unchecked")
	public static <T extends UserDetails> T getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			return null;
		}
		Object obj = auth.getPrincipal();
		if (!(obj instanceof UserDetails)) {
			return null;
		} 
		return (T) obj;
	}

	public static Integer getId() {
		ForumUserDetails user = getUser();
		if (user == null) {
			return null;
		}
		return user.getId();
	}

	public static String getUsername() {
		ForumUserDetails user = getUser();
		if (user == null) {
			return null;
		}
		return user.getUsername();
	}

}
