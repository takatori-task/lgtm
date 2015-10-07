# Images schema

# --- !Ups
INSERT INTO image (image_url, user_id) VALUES ('test', 'test');

# --- !Downs
DELETE FROM image;



