package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

	// TODO: More information
}