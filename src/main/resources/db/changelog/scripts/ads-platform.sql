-- liquibase formatted sql

-- changeSet alexadler:1

-- создать таблицу "Пользователи"
CREATE TABLE users
(
    id              bigserial    PRIMARY KEY,
    first_name      text         NOT NULL,
    last_name       text         NOT NULL,
    phone           varchar(16)  NOT NULL,
    email           text         NOT NULL,
    password        text         NOT NULL,
    role            varchar(16)  NOT NULL DEFAULT 'USER'
);

-- создать таблицу "Объявления"
CREATE TABLE ads
(
    id              bigserial       PRIMARY KEY,
    id_author       bigint          REFERENCES users (id),
    title           text            NOT NULL,
    description     text            NOT NULL,
    price           decimal(17, 2)  NOT NULL CHECK (price > 0::decimal),
    image           bytea
);

-- создать таблицу "Отзывы"
CREATE TABLE ads_comments
(
    id              bigserial    PRIMARY KEY,
    id_author       bigint       REFERENCES users (id),
    id_ads          bigint       REFERENCES ads (id),
    comment_time    timestamp    NOT NULL,
    comment_text    text         NOT NULL
);