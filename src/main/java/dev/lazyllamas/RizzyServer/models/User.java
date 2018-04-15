package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
public class User
{
	public User(UUID id)
	{
		this.id = id;
	}

	@Getter
	@Id
	private UUID id;

	@Getter
	@Setter
	private String email;

	@Getter
	@Setter
	private byte[] password;

	@Getter
	@Setter
	@OneToOne
	private Profile profile;
}
