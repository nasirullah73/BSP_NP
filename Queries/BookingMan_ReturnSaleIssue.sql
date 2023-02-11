merge into return_detail a
using (
select a.*,b.bookingid from 
(select srno, r_srno,ref_transno,company_id from return_detail
where bookingid in (21,18,19,5,14)
and company_id not in ('004','012') 
and ref_transno is not null )
a join (
select srno, bookingid,company_id,transno from sale_detail 
where company_id not in ('004','012')
and bookingid not in  (21,18,19,5,14)
and transno in (select ref_transno from return_detail
where bookingid in (21,18,19,5,14)
and company_id not in ('004','012') 
and ref_transno is not null )) b
on a.ref_transno=b.transno and a.company_id=b.company_id and a.r_srno=b.srno
) b
on (a.srno=b.srno) 
when   matched then
  update set a.bookingid=b.bookingid
  
  
