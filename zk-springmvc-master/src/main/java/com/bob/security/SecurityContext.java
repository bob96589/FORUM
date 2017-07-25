package com.bob.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {

	@SuppressWarnings("unchecked")
	public static <T extends ForumUserDetails> T getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return (T) auth.getPrincipal();
	}

	public static int getId() {
		ForumUserDetails user = getUser();
		return user.getId();
	}

	public static String getAccount() {
		ForumUserDetails user = getUser();
		return user.getUsername();
	}

}
