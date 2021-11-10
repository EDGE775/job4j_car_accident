CREATE TABLE types
(
    id   serial primary key,
    name varchar(255)
);

CREATE TABLE rules
(
    id   serial primary key,
    name varchar(255)
);

CREATE TABLE accidents
(
    id      serial primary key,
    name    varchar(2000)             not null,
    text    varchar(2000)             not null,
    address varchar(2000)             not null,
    type_id int references types (id) not null,
    created timestamp                 not null
);

CREATE TABLE accidents_rules
(
    id          serial primary key,
    rule_id     int references rules (id)     not null,
    accident_id int references accidents (id) not null
);

INSERT INTO types(name)
VALUES ('Две машины'),
       ('Машина и человек'),
       ('Машина и велосипед');
INSERT INTO rules(name)
VALUES ('Статья. 1'),
       ('Статья. 2'),
       ('Статья. 3');