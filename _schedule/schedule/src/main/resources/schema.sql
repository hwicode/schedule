drop table if exists sub_task cascade;
drop table if exists task cascade;
drop table if exists daily_to_do_list cascade;

create table daily_to_do_list (
   id bigint not null auto_increment,
   primary key (id)
) engine=InnoDB;

create table sub_task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   sub_task_status varchar(255),
   task_id bigint,
   primary key (id)
) engine=InnoDB;

create table task (
   id bigint not null auto_increment,
   name varchar(255) not null,
   priority varchar(255),
   importance varchar(255),
   difficulty varchar(255),
   task_status varchar(255),
   daily_to_do_list_id bigint,
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
