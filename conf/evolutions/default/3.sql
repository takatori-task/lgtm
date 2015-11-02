# Favorites shema

# --- !Ups

CREATE TABLE favorite (
       user_id        varchar(255) NOT NULL,
       id             integer NOT NULL,
       PRIMARY KEY (user_id, id)
);


# --- !Downs

DROP TABLE favorite;
