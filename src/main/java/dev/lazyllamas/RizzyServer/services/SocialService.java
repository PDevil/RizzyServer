package dev.lazyllamas.RizzyServer.services;

import dev.lazyllamas.RizzyServer.models.Profile;
import dev.lazyllamas.RizzyServer.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.UUID;

@Service
public class SocialService
{
	@Autowired
	EntityManager em;

	public Profile find(UUID id)
	{
		UUID storageId = em.find(User.class, id).getStorage_id();
		return em.find(Profile.class, storageId);
	}

	public List<Profile> getPeopleNearby(Profile issuer)
	{
		if(issuer.getLatitude() == null || issuer.getLongitude() == null)
			return null;

		TypedQuery<Profile> usersQuery = em
				.createQuery("SELECT p FROM Profile AS p WHERE latlon_distance(:myLatitude, :myLongitude, p.latitude, p.longitude) <= :nearbyRadius", Profile.class)
				.setParameter("myLatitude", issuer.getLatitude())
				.setParameter("myLongitude", issuer.getLongitude())
				.setParameter("nearbyRadius", 3.0);

		return usersQuery.getResultList();
	}

	@Transactional
	public void save(Profile issuer)
	{
		em.merge(issuer);
	}
}
