SET foreign_key_checks = 0;

drop table if exists learning_time cascade;
drop table if exists sub_task cascade;
drop table if exists task cascade;
drop table if exists daily_schedule cascade;
drop table if exists calendar cascade;
drop table if exists calendar_goal cascade;
drop table if exists goal cascade;
drop table if exists sub_goal cascade;

SET foreign_key_checks = 1;

create table daily_schedule (
   id bigint not null auto_increment,
   today date,
   review text,
   emoji varchar(255) DEFAULT 'NOT_BAD',
   calendar_id bigint,
   primary key (id)
) engine=InnoDB;

create table sub_task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_task_status varchar(255) DEFAULT 'TODO',
   task_id bigint,
   primary key (id)
) engine=InnoDB;

create table task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   priority varchar(255) DEFAULT 'SECOND',
   importance varchar(255) DEFAULT 'SECOND',
   difficulty varchar(255) DEFAULT 'NORMAL',
   task_status varchar(255) DEFAULT 'TODO',
   daily_schedule_id bigint,
   primary key (id)
) engine=InnoDB;

create table learning_time (
   id bigint not null auto_increment,
   start_time timestamp not null,
   end_time timestamp,
   subject varchar(255),
   task_id bigint,
   sub_task_id bigint,
   daily_schedule_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table calendar (
   id bigint not null auto_increment,
   year_and_month date not null,
   weekly_study_date tinyint,
   primary key (id)
) engine=InnoDB;

create table calendar_goal (
   id bigint not null auto_increment,
   calendar_id bigint not null,
   goal_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table goal (
   id bigint not null auto_increment,
   name varchar(255) not null,
   goal_staus varchar(255) DEFAULT 'TODO',
   primary key (id)
) engine=InnoDB;

create table sub_goal (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_goal_staus varchar(255) DEFAULT 'TODO',
   goal_id bigint not null,
   primary key (id)
) engine=InnoDB;

alter table daily_schedule
   add constraint
   foreign key (calendar_id)
   references calendar (id);

alter table sub_task
   add constraint
   foreign key (task_id)
   references task (id);

alter table task
   add constraint
   foreign key (daily_schedule_id)
   references daily_schedule (id);

alter table learning_time
   add constraint
   foreign key (task_id)
   references task (id);

alter table learning_time
   add constraint
   foreign key (sub_task_id)
   references sub_task (id);

alter table learning_time
   add constraint
   foreign key (daily_schedule_id)
   references daily_schedule (id);

alter table calendar_goal
   add constraint
   foreign key (calendar_id)
   references calendar (id);

alter table calendar_goal
   add constraint
   foreign key (goal_id)
   references goal (id);

alter table sub_goal
   add constraint
   foreign key (goal_id)
   references goal (id);
