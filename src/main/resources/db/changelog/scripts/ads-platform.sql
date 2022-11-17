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
    price           decimal(17, 2)  NOT NULL CHECK (price > 0::decimal)
);

-- создать таблицу "Изображения"
CREATE TABLE ads_images
(
    id              bigserial   PRIMARY KEY,
    id_ads          bigint      REFERENCES ads (id),
    image           bytea       NOT NULL
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

-- changeSet alexadler:2

-- переименовать поле "email -> username" в таблице "Пользователи" и добавить ограничение для него
ALTER TABLE users
RENAME COLUMN email TO username;

ALTER TABLE users
ADD CONSTRAINT username_unique UNIQUE (username);

-- удалить поле "role" в таблице "Пользователи"
ALTER TABLE users
DROP COLUMN IF EXISTS role;

-- добавить поле "enabled" в таблицу "Пользователи"
ALTER TABLE users
ADD enabled boolean NOT NULL DEFAULT true;

-- добавить таблицу "Authorities"
CREATE TABLE authorities
(
    username    text            NOT NULL,
    authority   varchar(16)     NOT NULL,
    CONSTRAINT authorities_unique UNIQUE (username, authority)
);

-- changeSet alexadler:3

-- удалить лишние ограничения в таблице "Пользователи"

ALTER TABLE users
ALTER COLUMN first_name DROP NOT NULL;

ALTER TABLE users
ALTER COLUMN last_name DROP NOT NULL;

ALTER TABLE users
ALTER COLUMN phone DROP NOT NULL;

-- changeSet alexadler:4

-- добавить каскадное удаление для таблицы "Отзывы"

alter table ads_comments
    drop constraint ads_comments_id_ads_fkey;

alter table ads_comments
    add foreign key (id_ads) references ads
        on delete cascade;

alter table ads_comments
    drop constraint ads_comments_id_author_fkey;

alter table ads_comments
    add foreign key (id_author) references users
        on delete cascade;

-- добавить каскадное удаление для таблицы "Изображения"

alter table ads_images
    drop constraint ads_images_id_ads_fkey;

alter table ads_images
    add foreign key (id_ads) references ads
        on delete cascade;

-- добавить каскадное удаление для таблицы "Объявления"

alter table ads
    drop constraint ads_id_author_fkey;

alter table ads
    add foreign key (id_author) references users
        on delete cascade;