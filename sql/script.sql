create table if not exists image
(
    id          bigserial
        primary key,
    image_bytes bytea,
    name        varchar(255) not null,
    post_id     bigint,
    user_id     bigint
);

alter table image
    owner to postgres;

create table if not exists person
(
    id           bigserial
        primary key,
    bio          text,
    created_date timestamp,
    email        varchar(255) not null
        constraint uk_fwmwi44u55bo4rvwsv0cln012
            unique,
    password     varchar(3000),
    username     varchar(255) not null
        constraint uk_n0i6d7rc1hqkjivk494g8j2qd
            unique
);

alter table person
    owner to postgres;

create table if not exists post
(
    id           bigserial
        primary key,
    caption      varchar(2000),
    created_date timestamp,
    likes        integer,
    location     varchar(255),
    title        varchar(255),
    person_id    bigint
        constraint fkkenxtm1pl4w6rchuhelil8lf4
            references person
            on delete cascade
);

alter table post
    owner to postgres;

create table if not exists role
(
    person_id bigint not null
        constraint fkp74rfg21c55d8eebfl00w5451
            references person
            on delete cascade,
    roles     integer
);

alter table role
    owner to postgres;

create table if not exists people_liked_post
(
    post_id   bigint not null
        constraint fk7rsmgjfd4358sortcsk4ebx48
            references post
            on delete cascade,
    person_id bigint not null
        constraint fkapt2ypbegegup530wrr8gbnk6
            references person
            on delete cascade,
    primary key (post_id, person_id)
);

alter table people_liked_post
    owner to postgres;

create table if not exists comment
(
    id           bigserial
        primary key,
    created_date timestamp,
    message      text not null,
    person_id    bigint
        constraint fk285svcgstjk3k94kv412gce6x
            references person,
    post_id      bigint
        constraint fks1slvnkuemjsq2kj4h3vhx7i1
            references post
);

alter table comment
    owner to postgres;


