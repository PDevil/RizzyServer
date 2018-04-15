package dev.lazyllamas.RizzyServer.controllers;

import dev.lazyllamas.RizzyServer.ControllerErrorState;
import dev.lazyllamas.RizzyServer.models.Profile;
import dev.lazyllamas.RizzyServer.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

	/*@PostMapping("/avatar/{id}")
	public ResponseEntity uploadAvatar(@PathVariable UUID id, @RequestPart(value = "file", required = true) MultipartFile file)
	{
		file.getName();
		return null;
	}*/
}
