package org.registration.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.registration.persistence.AdminEntity;
import org.registration.persistence.dao.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("adminDetailsService")
public class AdminDetailService implements UserDetailsService {

	@Autowired
	AdminRepository adminRepository;
	
	public AdminDetailService() {
		super();
	}
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AdminEntity admin = adminRepository.findByUsername(username);
		
		if (admin == null) 
			throw new UsernameNotFoundException("Admin with username " + username + " does not exist!");
		
		
		
		return new User(username, admin.getPassword(), true, true, true, true, getGrantedAuthorities(new ArrayList<>()));
	}
	

    public static final List<GrantedAuthority> getGrantedAuthorities(final List<String> roleNames) {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String role : roleNames) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


}
