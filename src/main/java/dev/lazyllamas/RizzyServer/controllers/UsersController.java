package dev.lazyllamas.RizzyServer.controllers;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import dev.lazyllamas.RizzyServer.ControllerErrorState;
import dev.lazyllamas.RizzyServer.models.NewUser;
import dev.lazyllamas.RizzyServer.models.User;
import dev.lazyllamas.RizzyServer.services.UserService;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.naming.ldap.Control;
import javax.validation.constraints.Null;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UsersController
{
	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<ControllerErrorState> registerUser(@RequestBody NewUser user, UriComponentsBuilder uriBuilder)
	{
		try
		{
			user.getEmail();
			user.getPassword();
		}
		catch(NullPointerException e)
		{
			return ControllerErrorState.compileResponse(100, "Email and/or password is missing.");
		}

		if(userService.userExists(user.getEmail()))
		{
			return ControllerErrorState.compileResponse(101, "Account with this email address already exists!");
		}

		if(user.getPassword().equals(""))
		{
			return ControllerErrorState.compileResponse(102, "Password not given.");
		}

		byte[] pass = user.getPassword().getBytes();
		IMessageDigest hasher = HashFactory.getInstance("Whirlpool");
		hasher.update(pass, 0, pass.length);

		User newUser = new User(UUID.randomUUID());
		newUser.setEmail(user.getEmail());
		newUser.setPassword(hasher.digest());

		userService.save(newUser);

		URI location = uriBuilder.path("/users/{id}").buildAndExpand(newUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
