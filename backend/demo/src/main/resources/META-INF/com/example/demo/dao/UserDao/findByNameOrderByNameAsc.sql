select
    *
from users
where deleted = false
  and name like /* @infix(name) */''
order by name asc