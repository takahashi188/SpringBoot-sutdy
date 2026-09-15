select
    *
from users
where deleted = false
/*%if name != null && name.length() > 1 */
  and name like /* @infix(name) */''
/*%end */
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