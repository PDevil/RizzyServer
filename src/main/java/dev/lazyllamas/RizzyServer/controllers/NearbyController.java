package dev.lazyllamas.RizzyServer.controllers;

import dev.lazyllamas.RizzyServer.ControllerErrorState;
import dev.lazyllamas.RizzyServer.models.Profile;
import dev.lazyllamas.RizzyServer.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/nearby")
public class NearbyController
{
	@Autowired
	private SocialService socialService;

	@GetMapping("/{id}")
	public ResponseEntity getNearbyPeople(@PathVariable UUID id)
	{
		Profile p = socialService.find(id);
		if(p == null)
		{
			return ControllerErrorState.compileResponse(69, "User ID is not correct!");
		}

		return ResponseEntity.ok(socialService.getPeopleNearby(p));
	}
}
