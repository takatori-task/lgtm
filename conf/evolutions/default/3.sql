# Favorites shema

# --- !Ups

CREATE TABLE favorite (
       user_id        varchar(255) NOT NULL,
       id             integer NOT NULL,
       PRIMARY KEY (user_id, id),
       FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE,
       FOREIGN KEY (id) REFERENCES image(id) ON DELETE CASCADE
);


# --- !Downs

DROP TABLE favorite;
