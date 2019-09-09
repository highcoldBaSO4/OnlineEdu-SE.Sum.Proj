/*use onlineStudy;
drop view if exists satistics;
create view satistics as 
(select avg(time_in_minute-pause_time)as average,user_id as id,sum(time_in_minute-pause_time) as total_time,
std(time_in_minute-pause_time) as standard,sum(change_speed_time+jump_times)as unconcentrate
from study_record 
group by user_id);
*/
DELIMITER $$
drop procedure if exists update_report $$
create procedure update_report()

BEGIN

declare average_study_time double default 0;
declare average_std double default 0;
declare tmp_average,tmp_std,tmp_total double default 0;
declare tmp_id,tmp_uncon long default 0;
declare tmp1 double default 0;
declare tmp2 int default 100;
DECLARE done boolean default 0;
DECLARE cs CURSOR FOR SELECT * FROM satistics;
DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

select avg(average),avg(standard) from satistics into average_study_time,average_std;
open cs;
	repeat 
		fetch cs into tmp_average,tmp_id,tmp_total,tmp_std,tmp_uncon;
        if done!=1 then
			set tmp1 = (tmp_std-average_std)/average_std - 0.2;
			if(tmp1<0) then set tmp1=0;
            end if;
            
			set tmp1 = 300*(tmp_average-average_study_time)/average_study_time+80-tmp1/100;
			
            if(tmp1>100) then set tmp1=100; 
            end if;
            
			if(tmp1<0) then set tmp1=0;
            end if;
            
		    set tmp2 = 100 - tmp_uncon;
			
            if(tmp2<0) then set tmp2 = 0;
            end if;
            
			delete from study_report where id = tmp_id;
			insert into study_report values (tmp_id,tmp2,tmp1,tmp_total);
        end if;
	until done end repeat;
    close cs;
END
$$
DELIMITER ;

