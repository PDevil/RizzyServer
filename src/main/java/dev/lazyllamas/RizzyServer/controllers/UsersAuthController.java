package dev.lazyllamas.RizzyServer.controllers;

import dev.lazyllamas.RizzyServer.ControllerErrorState;
import dev.lazyllamas.RizzyServer.models.NewUser;
import dev.lazyllamas.RizzyServer.models.User;
import dev.lazyllamas.RizzyServer.services.UserService;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class UsersAuthController
{
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseEntity<ControllerErrorState> login(@RequestBody NewUser user, UriComponentsBuilder uriBuilder)
	{
		if(user.getEmail() == null || user.getPassword() == null)
		{
			return ControllerErrorState.compileResponse(100, "Email and/or password is missing.");
		}

		if(!userService.userCanLogin(user.getEmail(), user.getPassword()))
		{
			return ControllerErrorState.compileResponse(104, "Email and/or password is incorrect!");
		}

		UUID userId = userService.userFindIdByEmail(user.getEmail());
		URI location = uriBuilder.path("/profile/{id}").buildAndExpand(userId).toUri();
		return ResponseEntity.accepted().location(location).build();
	}

	@PostMapping("/register")
	public ResponseEntity<ControllerErrorState> register(@RequestBody NewUser user, UriComponentsBuilder uriBuilder)
	{
		if(user.getEmail() == null || user.getPassword() == null)
		{
			return ControllerErrorState.compileResponse(100, "Email and/or password is missing.");
		}

		if(!EmailValidator.getInstance(false).isValid(user.getEmail()))
		{
			return ControllerErrorState.compileResponse(101, "Invalid email address!");
		}

		if(userService.userExists(user.getEmail()))
		{
			return ControllerErrorState.compileResponse(102, "Account with this email address already exists!");
		}

		if(user.getPassword().equals(""))
		{
			return ControllerErrorState.compileResponse(103, "Password not given.");
		}

		byte[] pass = user.getPassword().getBytes();
		IMessageDigest hasher = HashFactory.getInstance("Whirlpool");
		hasher.update(pass, 0, pass.length);

		User newUser = new User(UUID.randomUUID());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(hasher.digest());
		newUser.setStorage_id(UUID.randomUUID());

		userService.save(newUser);

		URI location = uriBuilder.path("/profile/{id}").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
