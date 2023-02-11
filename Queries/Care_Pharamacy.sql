create table EXTR_ORDR
(
  sr_no     NUMBER not null,
  extr_code VARCHAR2(20),
  item      VARCHAR2(255),
  brand     VARCHAR2(100),
  productid VARCHAR2(10)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table EXTR_ORDR
  add constraint PK_EXTR_ORD_SRNO primary key (SR_NO);

create table TAKE_EXTR_ORDERS
(
  code        VARCHAR2(20),
  qty         NUMBER,
  customerid  NUMBER(6) default 125,
  bookingid   NUMBER default 15,
  uploaddate  DATE default SYSDATE,
  bookingdate DATE default SYSDATE,
  userid      NUMBER default 15
);

create or replace trigger trg_after_TakeExternalOrders
  after insert
  on TAKE_EXTR_ORDERS 
  for each row

declare
  -- local variables here
  vMappingProductid varchar2(6);
  vID Number;
begin
 select productid into   vMappingProductid 
 from EXTR_ORDR
 where EXTR_code=:NEW.CODE;
 
 Select Nvl(Max(ID),0)+1 into vID
 FROM PRODUCTSALE
 where Nvl(datasource,'D')='D';
 
 insert into PRODUCTSALE
(ID,BOOKING_DATE,UPLOAD_DATE,CUSTOMERID,PRODUCTID,QTY,STATUS,USERID,DATASOURCE)
VALUES (vID,:NEW.BOOKINGDATE,:NEW.UPLOADDATE,:NEW.CUSTOMERID,vMappingProductid,:NEW.QTY,'U',:NEW.USERID,'D');
 
end trg_after_TakeExternalOrders;
