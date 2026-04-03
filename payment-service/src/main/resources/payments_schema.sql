create schema payment_service AUTHORIZATION ms_user;

-- Дать права на использование схемы
GRANT USAGE ON SCHEMA payment_service TO ms_user;

create table payment_service.payments
(
    id          bigint primary key,
    order_id    bigint,
    status      varchar(16)    not null,
    method      varchar(16)    not null,
    amount      decimal(12, 2) not null,
    created_at  timestamp
);

insert into payment_service.payments (
    id,
    order_id ,
    status,
    method,
    amount,
    created_at)
values (
        1,
        1,
        'PENDING',
        'CARD',
        20.0,
        '2026-03-27'
       );

commit;

select * from payment_service.payments;
