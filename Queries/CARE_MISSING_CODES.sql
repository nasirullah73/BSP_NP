select * from TAKE_EXTRA_ORDERS_TST a where not exists(
select 1 from EXTR_ORDR b where a.code=b.EXTR_code );
