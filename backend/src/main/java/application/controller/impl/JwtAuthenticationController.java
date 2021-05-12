package application.controller.impl;

import application.controller.IJwtAuthenticationController;
import application.model.JwtRequest;
import application.model.JwtResponse;
import application.model.UserDto;
import application.service.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import application.utils.JwtTokenUtil;

import java.util.Date;
import java.util.Objects;

@RestController
@CrossOrigin
public class JwtAuthenticationController implements IJwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService jwtInMemoryUserDetailsService;


	private final UserRepository repository;

	public JwtAuthenticationController(UserRepository repository) {
		this.repository = repository;
	}

	@PostMapping("/create")
	public ResponseEntity<?> createUser(@RequestBody JwtRequest loginRequest) {
		loginRequest.setPassword(passwordEncoder().encode(loginRequest.getPassword()));
		if(repository.existsByUsername(loginRequest.getUsername())) {
			return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
		}
		JwtRequest userSaved = repository.save(loginRequest);
		return ResponseEntity.ok(new UserDto(userSaved.getUsername(), userSaved.getId()));
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final UserDetails userDetails = jwtInMemoryUserDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		final Date tokenExpirationDate = jwtTokenUtil.getExpirationDateFromToken(token);

		return ResponseEntity.ok(new JwtResponse(token, tokenExpirationDate));
	}

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private void authenticate(String username, String password) throws Exception {
		Objects.requireNonNull(username);
		Objects.requireNonNull(password);

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
