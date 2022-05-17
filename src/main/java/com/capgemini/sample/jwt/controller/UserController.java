package com.capgemini.sample.jwt.controller;

import com.capgemini.sample.jwt.domain.AppRole;
import com.capgemini.sample.jwt.domain.AppUser;
import com.capgemini.sample.jwt.dto.RoleToUserDto;
import com.capgemini.sample.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	/**
	 * @see <a href="http://localhost:8080/jwt/api/users">Open</a>
	 */
	@GetMapping("/users")
	public List<AppUser> getUsers() {
		return this.userService.getUsers();
	}

	/**
	 * @see <a href="http://localhost:8080/jwt/user/Jan">Open</a>
	 */
	@GetMapping("/user/{name}")
	public AppUser saveUser(@PathVariable("name") final String name) {
		return this.userService.getUser(name);
	}

	/**
	 * @see <a href="http://localhost:8080/jwt/user/save">Open</a>
	 */
	@PostMapping("/user/save")
	public ResponseEntity<AppUser> saveUser(@RequestBody final AppUser user) {
		final AppUser appUser = this.userService.saveUser(user);
		final URI uri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/{name}").build(appUser.getName());
		return ResponseEntity.created(uri).body(appUser);
	}

	/**
	 * @see <a href="http://localhost:8080/jwt/role/save">Open</a>
	 */
	@PostMapping("/role/save")
	public ResponseEntity<AppRole> saveRole(@RequestBody final AppRole role) {
		final AppRole appRole = this.userService.saveRole(role);
		return ResponseEntity.ok().body(appRole);
	}

	/**
	 * @see <a href="http://localhost:8080/jwt/role/add-to-user">Open</a>
	 */
	@PostMapping("/role/add-to-user")
	public void addRoleToUser(@RequestBody final RoleToUserDto roleToUserDto) {
		this.userService.addRoleToUser(roleToUserDto.getUserName(), roleToUserDto.getRoleName());
	}

}
