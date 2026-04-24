create schema delivery_service AUTHORIZATION ms_user;

-- Дать права на использование схемы
GRANT USAGE ON SCHEMA delivery_service TO ms_user;

--------------------

create table delivery_service.deliveries (
                                      id serial primary key,
                                      order_id bigint not null,
                                      status varchar(16) not null,
                                      street varchar(255) not null,
                                      city varchar(255) not null,
                                      state varchar(255),
                                      zipcode varchar(255),
                                      country varchar(255) not null,
                                      delivery_date date,
                                      start_time time,
                                      end_time time,
                                      tracking_number varchar
);

insert into delivery_service.deliveries (
                                        id,
                                        order_id,
                                        status,
                                        street,
                                        city,
                                        state,
                                        zipcode,
                                        country,
                                        delivery_date,
                                        start_time,
                                        end_time,
                                        tracking_number)
values (
        1,
        1,
        'CREATED',
        'Baker street',
        'Indiannopolis',
        'IN',
        '111222',
        'USA',
        '2026-03-31',
        '08:00:00',
        '09:00:00',
        '123123'
       );

commit;

select * from delivery_service.deliveries;
