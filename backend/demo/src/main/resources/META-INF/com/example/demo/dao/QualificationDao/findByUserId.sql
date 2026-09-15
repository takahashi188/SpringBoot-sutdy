select
	uq.id,
	uq.user_id,
	uq.qualification_id,
	uq.acquisition_date
from user_qualifications uq
left join qualification_master qm
on qm.id = uq.qualification_id
where user_id = /* userId */1