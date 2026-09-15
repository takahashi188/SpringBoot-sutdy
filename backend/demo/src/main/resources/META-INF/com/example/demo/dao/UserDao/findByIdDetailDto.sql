select
	u.id,
	u.name,
	u.email,
	p.nickname,
	p.birthday,
	uq.qualification_id as qualificationId,
	uq.acquisition_date as acquisitionDate,
	mq.qualification_name as qualificationName
from users u
left join profiles p on p.user_id = u.id
left join user_qualifications uq on uq.user_id = u.id
left join qualification_master mq on mq.id = uq.qualification_id
where u.id = /* id */0
	and u.deleted = false