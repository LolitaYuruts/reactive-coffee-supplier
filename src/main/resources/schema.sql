DROP TABLE IF EXISTS coffee;

CREATE TABLE coffee (id BIGINT auto_increment primary key,
                       title VARCHAR(255),
                       beans VARCHAR(255),
                       aroma VARCHAR(255),
                       roast VARCHAR(255),
                       price DECIMAL(19,2),
                       caffeine_content DOUBLE);