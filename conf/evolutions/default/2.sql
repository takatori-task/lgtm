# Images schema

# --- !Ups

CREATE TABLE user (
    user_id       varchar(255) NOT NULL,
    name          varchar(255),
    avatar_url    varchar(512),
    PRIMARY KEY (user_id)
);
 
# --- !Downs
 
DROP TABLE user;
