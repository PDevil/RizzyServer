CREATE OR REPLACE FUNCTION latlon_distance(lat1 DOUBLE PRECISION, lon1 DOUBLE PRECISION, lat2 DOUBLE PRECISION, lon2 DOUBLE PRECISION) RETURNS DOUBLE PRECISION AS $$
DECLARE                                                   
    x float = 111.12 * (lat2 - lat1);                           
    y float = 111.12 * (lon2 - lon1) * cos(lat1 / 92.215);        
BEGIN                                                     
    RETURN sqrt(x * x + y * y);                               
END  
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION matches(my_id UUID, age_issuer INTEGER, lat_issuer DOUBLE PRECISION, lon_issuer DOUBLE PRECISION, nearby_radius DOUBLE PRECISION)
	RETURNS TABLE (
		rank DOUBLE PRECISION,
		storage_id UUID,
		age INTEGER,
		description VARCHAR,
		latitude DOUBLE PRECISION,
		longitude DOUBLE PRECISION,
		name VARCHAR
	)
AS $$
BEGIN
	RETURN QUERY
		SELECT CAST(((1 + pi() * abs(age_issuer - p.age) / age_issuer) * latlon_distance(lat_issuer, lon_issuer, p.latitude, p.longitude)) AS DOUBLE PRECISION), *
		FROM profiles AS p
		WHERE 
			latlon_distance(lat_issuer, lon_issuer, p.latitude, p.longitude) <= nearby_radius
			AND 
			my_id != p.storage_id
		ORDER BY
			1 ASC;
END; $$ LANGUAGE plpgsql;
