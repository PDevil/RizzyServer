package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class NewUser
{
	private String email;
	private String password;
}
