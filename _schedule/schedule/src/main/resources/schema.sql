SET foreign_key_checks = 0;

drop table if exists learning_time cascade;
drop table if exists sub_task cascade;
drop table if exists task cascade;
drop table if exists daily_to_do_list cascade;

SET foreign_key_checks = 1;

create table daily_to_do_list (
   id bigint not null auto_increment,
   today timestamp,
   review text,
   emoji varchar(255) DEFAULT 'NOT_BAD',
   primary key (id)
) engine=InnoDB;

create table sub_task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_task_status varchar(255) DEFAULT 'TODO',
   task_id bigint not null,
   primary key (id)
) engine=InnoDB;

create table task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   priority varchar(255) DEFAULT 'SECOND',
   importance varchar(255) DEFAULT 'SECOND',
   difficulty varchar(255) DEFAULT 'NORMAL',
   task_status varchar(255) DEFAULT 'TODO',
   daily_to_do_list_id bigint,
   primary key (id)
) engine=InnoDB;

create table learning_time (
   id bigint not null auto_increment,
   start_time timestamp not null,
   end_time timestamp,
   subject varchar(255),
   task_id bigint,
   sub_task_id bigint,
   daily_to_do_list_id bigint not null,
   primary key (id)
) engine=InnoDB;

alter table sub_task
   add constraint
   foreign key (task_id)
   references task (id);

alter table task
   add constraint
   foreign key (daily_to_do_list_id)
   references daily_to_do_list (id);

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
   foreign key (daily_to_do_list_id)
   references daily_to_do_list (id);
