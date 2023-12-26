package ru.patseev.authenticationservice.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.patseev.authenticationservice.domain.UserCredential;
import ru.patseev.authenticationservice.repository.UserCredentialRepository;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
	private final UserCredentialRepository userCredentialRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserCredential credential = userCredentialRepository
				.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		return new CustomUserDetails(credential);
	}
}