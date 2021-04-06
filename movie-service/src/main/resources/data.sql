drop table if exists movie;

create table movie as
    select *
    from csvread('classpath:movie.csv');