package dev.lazyllamas.RizzyServer.services;

import dev.lazyllamas.RizzyServer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
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
}
