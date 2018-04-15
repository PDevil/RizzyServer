package dev.lazyllamas.RizzyServer.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "friends")
@Getter
@Setter
public class Friendship
{
	@EmbeddedId
	FriendshipId id;
	int status;
}

@Embeddable
class FriendshipId implements Serializable
{
	UUID person_1;
	UUID person_2;
}