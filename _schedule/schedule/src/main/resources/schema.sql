drop table if exists sub_task cascade;
drop table if exists task cascade;
drop table if exists daily_checklist cascade;

create table daily_checklist (
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
   difficulty varchar(255),
   name varchar(255) not null,
   task_status varchar(255),
   daily_checklist_id bigint,
   primary key (id)
) engine=InnoDB;

alter table sub_task
   add constraint
   foreign key (task_id)
   references task (id);

alter table task
   add constraint
   foreign key (daily_checklist_id)
   references daily_checklist (id);
