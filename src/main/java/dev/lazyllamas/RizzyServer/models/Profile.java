package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.net.URI;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@Getter
@Setter
public class Profile
{
	public Profile(UUID id)
	{
		this.storage_id = id;
	}

	@Id
	private UUID storage_id;
	private String name;
	private Date age;
	private String description;
	private Double latitude;
	private Double longitude;
	private String avatar;
}
