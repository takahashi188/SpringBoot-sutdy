select
    *
from users
where deleted = false

order by
/*%if sortColumn.equals("id") */
    id
/*%elseif sortColumn.equals("name") */
    name
/*%end*/

/*%if direction.equals("asc") */
    asc
/*%else*/
    desc
/*%end*/