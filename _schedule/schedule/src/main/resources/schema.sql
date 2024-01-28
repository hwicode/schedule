SET foreign_key_checks = 0;

drop table if exists learning_time cascade;
drop table if exists sub_task cascade;
drop table if exists task cascade;
drop table if exists daily_schedule cascade;
drop table if exists calendar cascade;
drop table if exists calendar_goal cascade;
drop table if exists goal cascade;
drop table if exists sub_goal cascade;
drop table if exists review_date cascade;
drop table if exists review_date_task cascade;
drop table if exists review_cycle cascade;
drop table if exists tag cascade;
drop table if exists memo cascade;
drop table if exists daily_tag cascade;
drop table if exists memo_tag cascade;
drop table if exists user cascade;

SET foreign_key_checks = 1;

create table daily_schedule (
   id bigint not null auto_increment,
   today timestamp,
   total_difficulty_score smallint not null DEFAULT 0,
   today_done_percent tinyint not null DEFAULT 0,
   total_learning_time smallint not null DEFAULT 0,
   main_tag_name varchar(255),
   review text,
   emoji varchar(255) DEFAULT 'NOT_BAD',
   calendar_id bigint,
   user_id bigint,
   primary key (id)
) engine=InnoDB;

CREATE INDEX idx_today
   ON daily_schedule (today);

create table sub_task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_task_status varchar(255) DEFAULT 'TODO',
   user_id bigint,
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
   user_id bigint,
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
   user_id bigint,
   daily_schedule_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table calendar (
   id bigint not null auto_increment,
   year_and_month timestamp not null,
   weekly_study_date tinyint,
   primary key (id)
) engine=InnoDB;

CREATE INDEX idx_year_and_month
   ON calendar (year_and_month);

create table calendar_goal (
   id bigint not null auto_increment,
   calendar_id bigint not null,
   goal_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table goal (
   id bigint not null auto_increment,
   name varchar(255) not null,
   goal_status varchar(255) DEFAULT 'TODO',
   primary key (id)
) engine=InnoDB;

create table sub_goal (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_goal_status varchar(255) DEFAULT 'TODO',
   goal_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table review_date (
   id bigint not null auto_increment,
   date timestamp not null,
   primary key (id)
) engine=InnoDB;

CREATE INDEX idx_date
   ON review_date (date);

create table review_date_task (
   id bigint not null auto_increment,
   task_id bigint not null,
   review_date_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table review_cycle (
   id bigint not null auto_increment,
   name varchar(255) not null,
   review_cycle_dates text not null,
   primary key (id)
) engine=InnoDB;

create table tag (
   id bigint not null auto_increment,
   name varchar(255) not null,
   user_id bigint,
   primary key (id)
) engine=InnoDB;

CREATE INDEX idx_name
   ON tag (name);

create table memo (
   id bigint not null auto_increment,
   text text not null,
   daily_schedule_id bigint,
   user_id bigint,
   primary key (id)
) engine=InnoDB;

create table daily_tag (
   id bigint not null auto_increment,
   daily_schedule_id bigint not null,
   tag_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table memo_tag (
   id bigint not null auto_increment,
   memo_id bigint not null,
   tag_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table user (
   id bigint not null auto_increment,
   name varchar(255) not null,
   email varchar(255) not null unique,
   oauth_provider varchar(255),
   primary key (id)
) engine=InnoDB;

CREATE INDEX idx_today
   ON user (email);

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

alter table review_date_task
   add constraint
   foreign key (task_id)
   references task (id);

alter table review_date_task
   add constraint
   foreign key (review_date_id)
   references review_date (id);

alter table memo
   add constraint
   foreign key (daily_schedule_id)
   references daily_schedule (id);

alter table daily_tag
   add constraint
   foreign key (daily_schedule_id)
   references daily_schedule (id);

alter table daily_tag
   add constraint
   foreign key (tag_id)
   references tag (id);

alter table memo_tag
   add constraint
   foreign key (memo_id)
   references memo (id);

alter table memo_tag
   add constraint
   foreign key (tag_id)
   references tag (id);
