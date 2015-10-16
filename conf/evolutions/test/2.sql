# Images schema

# --- !Ups
INSERT INTO image (image_url, user_id)
VALUES ('http://placeimg_1000_180_nature.jpg', 'test'),
       ('http://placeimg_300_300_tech.jpg', 'takatori'),
       ('http://placeimg_320_180_arch.jpg', NULL),
       ('http://placeimg_640_480_any.jpg', NULL),              
       ('http://placeimg_200_300_people.jpg', 'test');
# --- !Downs
DELETE FROM image;



