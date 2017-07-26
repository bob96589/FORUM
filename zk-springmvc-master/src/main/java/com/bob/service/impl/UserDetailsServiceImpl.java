package com.bob.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.bob.security.ForumUserDetails;
import com.bob.service.ForumService;

public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private ForumService forumService;

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserDetails user = null;
		try {
			com.bob.model.User dbUser = forumService.findUserByAccount(username);
			user = new ForumUserDetails(dbUser.getId(), dbUser.getAccount(), dbUser.getPassword().toLowerCase(), true,
					true, true, true, new ArrayList<GrantedAuthority>());
		} catch (Exception e) {
			throw new UsernameNotFoundException("Error in retrieving user");
		}
		return user;
	}
}
