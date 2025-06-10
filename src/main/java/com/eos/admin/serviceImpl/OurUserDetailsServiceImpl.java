package com.eos.admin.serviceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.eos.admin.entity.OurUsers;
import com.eos.admin.repository.UsersRepository;
@Service
public class OurUserDetailsServiceImpl implements UserDetailsService {


	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return usersRepository.findByEmail(username).orElseThrow();
	}

	public List<String> findEmailByProceeName(String processName) {
       List<OurUsers> response = usersRepository.findByUniqueCode(processName);
		
		return response.stream()
				.map(OurUsers :: getOfficeEmail)
				.filter(email -> email != null && !email.isEmpty())
				.collect(Collectors.toList());
	}
//	public String findEmailByProceeName(String processName) {
//	    List<OurUsers> response = usersRepository.findByUniqueCode(processName);
//
//	    return response.stream()
//	            .map(OurUsers::getOfficeEmail)
//	            .filter(email -> email != null && !email.isEmpty())
//	            .findFirst()
//	            .orElse(null); // or use "" if you prefer
//	}

}
