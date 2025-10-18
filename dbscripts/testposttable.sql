-- Create table: posts
CREATE TABLE posts (
                       id SERIAL PRIMARY KEY,
                       user_id UUID NOT NULL,
                       content TEXT NOT NULL,
                       created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
                       location GEOGRAPHY(Point, 4326) -- Stores latitude/longitude in WGS 84
);

-- Optional: create a spatial index for faster location queries
CREATE INDEX idx_posts_location ON posts USING GIST (location);



INSERT INTO posts (user_id, content, location)
VALUES
    ('550e8400-e29b-41d4-a716-446655440000', 'Enjoying Montego Bay beach!', ST_SetSRID(ST_MakePoint(-77.9163, 18.4762), 4326)),
    ('660e8400-e29b-41d4-a716-446655440000', 'Kingston vibes all night long!', ST_SetSRID(ST_MakePoint(-76.7920, 17.9970), 4326)),
    ('770e8400-e29b-41d4-a716-446655440000', 'At Dunn’s River Falls, Ocho Rios!', ST_SetSRID(ST_MakePoint(-77.1040, 18.4040), 4326)),
    ('880e8400-e29b-41d4-a716-446655440000', 'Sunset in Negril is stunning!', ST_SetSRID(ST_MakePoint(-78.3373, 18.2686), 4326)),
    ('990e8400-e29b-41d4-a716-446655440000', 'Port Antonio jerk chicken is the best!', ST_SetSRID(ST_MakePoint(-76.5350, 18.1760), 4326));