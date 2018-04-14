package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@NoArgsConstructor
@Getter
@Setter
public class Profile
{
	@Id
	private UUID storage_id;
	private String name;
	private int age;
	private String description;
	private Double latitude;
	private Double longitude;

	// TODO: More information
}
