CREATE TABLE `parse_slow_query` (
  `time` varchar(64) DEFAULT NULL,
  `user` varchar(64) DEFAULT NULL,
  `query_time` varchar(64) DEFAULT NULL,
  `is_internal` varchar(16) DEFAULT NULL,
  `prepared` varchar(64) DEFAULT NULL,
  `plan_from_cache` varchar(64) DEFAULT NULL,
  `backoff_total` varchar(64) DEFAULT NULL,
  `sql` longtext,
  `table_names` varchar(4096) DEFAULT NULL,
  `db` varchar(64) DEFAULT NULL,
  `show_create_table` varchar(4096) DEFAULT NULL,
  `source_sql` longtext
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

select distinct name from (
select
	b.help_topic_id + 1 as id,
	substring_index(substring_index(a.name, ';', b.help_topic_id + 1) ,';', -1) as name
from
	(select show_create_table as name from parse_slow_query where is_internal = 'false' ) a join mysql.help_topic b
	on
	b.help_topic_id < LENGTH(a.name) - LENGTH(REPLACE(a.name,';','')) + 1
    ) t;