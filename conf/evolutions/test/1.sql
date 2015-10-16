# Images schema

# --- !Ups

CREATE TABLE image (
    id        integer NOT NULL AUTO_INCREMENT,
    image_url varchar(512) NOT NULL,
    user_id   varchar(255),
    PRIMARY KEY (id)
);
 
# --- !Downs
 
DROP TABLE image;
