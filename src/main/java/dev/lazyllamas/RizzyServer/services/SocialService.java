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
		User u = em.find(User.class, id);
		if(u == null)
			return null;

		return u.getProfile();
	}

	public List<Profile> getPeopleNearby(Profile issuer)
	{
		if(issuer.getLatitude() == null || issuer.getLongitude() == null)
			return null;

		TypedQuery<Profile> profilesQuery = em
				.createQuery("SELECT p FROM Profile AS p WHERE latlon_distance(:myLatitude, :myLongitude, p.latitude, p.longitude) <= :nearbyRadius AND :myId != p.storage_id", Profile.class)
				.setParameter("myLatitude", issuer.getLatitude())
				.setParameter("myLongitude", issuer.getLongitude())
				.setParameter("myId", issuer.getStorage_id())
				.setParameter("nearbyRadius", 5.0);

		return profilesQuery.getResultList();
	}

	public List<Profile> getMatches(Profile issuer)
	{
		if(issuer.getLatitude() == null || issuer.getLongitude() == null)
			return null;

		TypedQuery<Profile> profilesQuery = em
				.createQuery("SELECT storage_id AS s_id, name, age, description, latitude, longitude " +
								"FROM matches(:id_issuer, :age_issuer, :lat_issuer, :lon_issuer, :nearby_radius) " +
								"WHERE NOT EXISTS(  SELECT 1 " +
													"FROM friendship " +
													"WHERE (person_1 = s_id AND person_2 = :id_issuer) OR (person_1 = :id_issuer AND person_2 = s_id) AND status = 1 )", Profile.class)
				.setParameter("age_issuer", issuer.getAge())
				.setParameter("lat_issuer", issuer.getLatitude())
				.setParameter("lon_issuer", issuer.getLongitude())
				.setParameter("id_issuer", issuer.getStorage_id())
				.setParameter("nearby_radius", 5.0);

		return profilesQuery.getResultList();
	}

	@Transactional
	public void save(Profile issuer)
	{
		em.merge(issuer);
	}
}
