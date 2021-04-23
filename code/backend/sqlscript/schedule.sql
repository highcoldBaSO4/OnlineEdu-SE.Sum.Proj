use onlineStudy;
drop view if exists satistics;
create view satistics as 
(select avg(time_in_minute-pause_time)as average,user_id as id,sum(time_in_minute-pause_time) as total_time,
std(time_in_minute-pause_time) as standard,sum(change_speed_time+jump_times)as unconcentrate
from study_record 
group by user_id);

call update_report();