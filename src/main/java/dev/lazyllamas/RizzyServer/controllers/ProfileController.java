package dev.lazyllamas.RizzyServer.controllers;

import dev.lazyllamas.RizzyServer.ControllerErrorState;
import dev.lazyllamas.RizzyServer.RizzyServerApplication;
import dev.lazyllamas.RizzyServer.models.Profile;
import dev.lazyllamas.RizzyServer.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

@RestController
@RequestMapping("/profile")
public class ProfileController
{
	@Autowired
	private SocialService socialService;

	@GetMapping("/{id}")
	public ResponseEntity getProfile(@PathVariable UUID id)
	{
		Profile p = socialService.find(id);
		if(p == null)
		{
			return ControllerErrorState.compileResponse(69, "User ID is not correct!");
		}

		return ResponseEntity.ok(p);
	}

	@PutMapping("/{id}")
	public ResponseEntity updateProfile(@PathVariable UUID id, @RequestBody Profile profile)
	{
		Profile p = socialService.find(id);
		if(p == null)
		{
			return ControllerErrorState.compileResponse(69, "User ID is not correct!");
		}

		if(profile.getName() == null || profile.getDescription() == null || profile.getAge() == null)
		{
			return ControllerErrorState.compileResponse(110, "Profile description is missing arguments.");
		}

		p.setName(profile.getName());
		p.setDescription(profile.getDescription());
		p.setAge(profile.getAge());

		socialService.save(p);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}")
	public ResponseEntity postPosition(@PathVariable UUID id, @RequestBody Profile profile)
	{
		Profile p = socialService.find(id);
		if(p == null)
		{
			return ControllerErrorState.compileResponse(69, "User ID is not correct!");
		}

		if(profile.getLatitude() == null || profile.getLongitude() == null)
		{
			return ControllerErrorState.compileResponse(110, "Latitude and/or longitude is missing!");
		}

		p.setLatitude(profile.getLatitude());
		p.setLongitude(profile.getLongitude());

		socialService.save(p);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/avatar/{id}")
	public ResponseEntity uploadAvatar(@PathVariable UUID id, @RequestPart(value = "file", required = true) MultipartFile file, UriComponentsBuilder uriBuilder) throws IOException
	{
		Profile p = socialService.find(id);
		if(p == null)
		{
			return ControllerErrorState.compileResponse(69, "User ID is not correct!");
		}

		String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
		String newName = id.toString() + "." + ext;

		URL absolute = RizzyServerApplication.class.getClassLoader().getResource("");
		File newFile = new File( absolute.getPath() + "public/images/user-avatars/" + newName);
		newFile.createNewFile();

		file.transferTo(newFile);
		URI location = uriBuilder.path("/images/user-avatars/{id}." + ext).buildAndExpand(id).toUri();

		p.setAvatar(location.getPath());
		socialService.save(p);
		return ResponseEntity.created(location).build();
	}
}
