CREATE USER ms_user WITH PASSWORD 'ms_user';
/*
CREATE DATABASE microservices
    OWNER = ms_user
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1;
*/
-- Дать права на подключение к базе
GRANT CONNECT ON DATABASE my_pg TO ms_user;

create schema order_service AUTHORIZATION ms_user;

-- Дать права на использование схемы
GRANT USAGE ON SCHEMA order_service TO ms_user;

-----------------------

create table order_service.customers (
                           id bigint primary key,
                           first_name varchar(255),
                           last_name varchar(255),
                           email varchar(255)
);

create table order_service.orders (
                                      id bigint primary key,
                                      status varchar(16) not null,
                                      price decimal(12,2) not null,
                                      street varchar(255) not null,
                                      city varchar(255) not null,
                                      state varchar(255),
                                      zipcode varchar(255),
                                      country varchar(255) not null,
                                      created_at timestamp,
                                      customer_id bigint,
--                        order_line_id bigint,
                                      CONSTRAINT fk_customer_id
                                          FOREIGN KEY(customer_id)
                                              REFERENCES order_service.customers(id)
/**
                        CONSTRAINT fk_order_line_id
                            FOREIGN KEY(order_line_id)
                                REFERENCES order_service.order_lines(id)
 **/
);

create table order_service.order_lines (
                             id bigint primary key,
                             product_name varchar(255),
                             quantity int,
                             price decimal,
                             order_id bigint,
                             CONSTRAINT fk_order_id
                                 FOREIGN KEY(order_id)
                                     REFERENCES order_service.orders(id)

);

-----------------------

insert into order_service.customers (
    id,
    first_name,
    last_name,
    email
)
values (
        1,
        'aaa',
        'qqq',
        'aaa@gmail.com'
       );

insert into order_service.orders (
    id,
    status,
    price,
    street,
    city,
    state,
    zipcode,
    country,
    created_at,
    customer_id
)
/*
                        CONSTRAINT fk_customer_id
                            FOREIGN KEY(customer_id)
                                REFERENCES order_service.customers(id),
                        CONSTRAINT fk_order_line_id
                            FOREIGN KEY(order_line_id)
                                REFERENCES order_service.order_lines(id)
 */
values (
           1,
           'NEW',
           10.0,
           'street_1',
           'city_1',
           'state_1',
           'zipcode_1',
           'country_1',
           '2026-03-26',
           1
       );

insert into order_service.order_lines (
                                       id,
                                       product_name,
                                       quantity,
                                       price,
                                       order_id
)
        values (
                1,
                'prod_1',
                1,
                10.0,
                1
               );

commit;

select * from order_service.customers;

select * from order_service.orders;

select * from order_service.order_lines;
