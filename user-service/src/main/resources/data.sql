drop table if exists user;

create table user as
    select *
    from csvread('classpath:user.csv');