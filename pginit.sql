create table public.test_table
(
    id      uuid        primary key     not null,
    value   text                        not null,
    created timestamp                   not null
);