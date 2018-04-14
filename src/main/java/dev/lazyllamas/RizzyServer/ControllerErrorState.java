package dev.lazyllamas.RizzyServer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@AllArgsConstructor
public class ControllerErrorState
{
	@Getter
	@Setter
	int error_code;

	@Getter
	@Setter
	String error_message;

	public static ResponseEntity<ControllerErrorState> compileResponse(int error_code, String error_message)
	{
		ControllerErrorState ces = new ControllerErrorState(error_code, error_message);
		ResponseEntity<ControllerErrorState> response = new ResponseEntity<ControllerErrorState>(ces, HttpStatus.BAD_REQUEST);
		return response;
	}
}
