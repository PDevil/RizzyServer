package dev.lazyllamas.RizzyServer.services;

import dev.lazyllamas.RizzyServer.models.Profile;
import dev.lazyllamas.RizzyServer.models.User;
import gnu.crypto.hash.HashFactory;
import gnu.crypto.hash.IMessageDigest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.net.URI;
import java.util.UUID;

@Service
public class UserService
{
	@Autowired
	EntityManager em;

	public boolean userExists(UUID id)
	{
		return em.find(User.class, id) != null;
	}

	public boolean userExists(String email)
	{
		return em
			.createQuery("FROM User AS u WHERE u.email = :email")
			.setParameter("email", email)
			.getResultList()
			.size() > 0;
	}

	public boolean userCanLogin(String email, String password)
	{
		byte[] pass = password.getBytes();
		IMessageDigest hasher = HashFactory.getInstance("Whirlpool");
		hasher.update(pass, 0, pass.length);

		return em
			.createQuery("FROM User AS u WHERE u.email = :email AND u.password = :password")
			.setParameter("email", email)
			.setParameter("password", hasher.digest())
			.getResultList()
			.size() > 0;
	}

	public UUID userFindIdByEmail(String email)
	{
		UUID id = null;
		try
		{
			id = (UUID) em
				.createQuery("SELECT u.id FROM User AS u WHERE email = :email")
				.setParameter("email", email)
				.getSingleResult();
		}
		catch(NoResultException e) {}

		return id;
	}

	@Transactional
	public void save(User user)
	{
		if(userExists(user.getId()))
		{
			em.merge(user);
		}
		else
		{
			em.persist(user);
		}
	}

	@Transactional
	public void createProfile(User user)
	{
		Profile p = new Profile(UUID.randomUUID());
		p.setName("");
		p.setDescription("");
		user.setProfile(p);
		em.persist(p);
	}
}
