-- dev 환경에서 사용되는 데이터

-- calendar 생성
INSERT INTO calendar (year_and_month, weekly_study_date, user_id) VALUES ('2023-11-01', 6, 1);


-- daily_schedule 생성
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-01', 14, 75, 680, 'Java1', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-02', 12, 65, 580, 'Java2', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-03', 13, 85, 780, 'Java3', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-04', 12, 100, 740, 'Java4', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-05', 10, 85, 400, 'Java5', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-06', 10, 85, 400, 'Java6', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-07', 10, 85, 400, 'Java7', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-08', 10, 85, 400, 'Java8', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-09', 10, 85, 400, 'Java9', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-10', 10, 85, 800, 'Java10', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-11', 10, 85, 800, 'Java11', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-12', 10, 85, 800, 'Java15', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-13', 10, 85, 800, 'Java25', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-14', 10, 85, 800, 'Java53', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-15', 10, 85, 800, 'Java54', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name,  calendar_id, user_id) VALUES ('2023-11-16', 10, 85, 800, 'Java55', 1, 1);
INSERT INTO daily_schedule (today, total_difficulty_score, today_done_percent, total_learning_time, main_tag_name, emoji, review,  calendar_id, user_id) VALUES ('2023-11-30', 10, 85, 400, 'Java55', 'GOOD', 'fidsnfoidsmfiodsmfoidsamfiewmfiedsakdnwdnjsadnsjdndnskdadsnadoiwdnwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww', 1, 1);


-- goal 생성
INSERT INTO goal (name, user_id) VALUES ('목표1', 1);INSERT INTO goal (name, user_id) VALUES ('목표2', 1);INSERT INTO goal (name, user_id) VALUES ('목표3', 1);


-- sub_goal 생성
INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표1', 1);INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표2', 1);
INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표1', 2);INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표2', 2);INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표3', 2);
INSERT INTO sub_goal (name, goal_id) VALUES ('서브 목표1', 3);


-- calendar_goal 생성
INSERT INTO calendar_goal (calendar_id, goal_id) VALUES (1, 1);
INSERT INTO calendar_goal (calendar_id, goal_id) VALUES (1, 2);
INSERT INTO calendar_goal (calendar_id, goal_id) VALUES (1, 3);


-- tag 생성
INSERT INTO tag (name, user_id) VALUES ('태그', 1);
INSERT INTO tag (name, user_id) VALUES ('Java', 1);
INSERT INTO tag (name, user_id) VALUES ('Spring', 1);
INSERT INTO tag (name, user_id) VALUES ('Mysql', 1);
INSERT INTO tag (name, user_id) VALUES ('Http', 1);
INSERT INTO tag (name, user_id) VALUES ('태그2', 1);
INSERT INTO tag (name, user_id) VALUES ('태그3', 1);
INSERT INTO tag (name, user_id) VALUES ('태그4', 1);
INSERT INTO tag (name, user_id) VALUES ('태그5', 1);


-- daily_tag 생성
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (1, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (2, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (3, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (4, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (5, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (6, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (7, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (8, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (9, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (10, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (11, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (12, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (13, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (14, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (15, 1);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (16, 1);

INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 2);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 3);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 4);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 5);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 6);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 7);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 8);
INSERT INTO daily_tag (daily_schedule_id, tag_id) VALUES (17, 9);


-- memo 생성
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjwqndjidsokfmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww;j;j;wwwwwwwwwwwwwwwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdkwnjdsmdkwndjdsmdmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndwwwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdkwndjdsmdkwndjdsmdkwndsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndkwndjdsmdkwndjdsmdkwndjwqndjidsokfmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjwqndjidsokfmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwkkk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdndjdsmdkwndjdsdjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjwqndjidsokfmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwkkk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnsifejsmfkdsmdndjdsmdkwndwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjdsmdkwndjwqndjidsokfmdskfmakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwkkk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('ifnakdnwqiwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwkkk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java1', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java2', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java3', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java4', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java5', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java6', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java7', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('Java8', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkk''sdzcxwwwdwswwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkkadsdk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkk''wzcwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkkadsdk''wwwwdn', 1, 1);
INSERT INTO memo (text, daily_schedule_id, user_id) VALUES ('wwwwkkk''wzcwwwdn', 1, 1);


-- memo_tag 생성
INSERT INTO memo_tag (memo_id, tag_id) VALUES (1, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (2, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (3, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (4, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (5, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (6, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (7, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (8, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (9, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (10, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (11, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (12, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (13, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (14, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (15, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (16, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (17, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (18, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (19, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (20, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (21, 1);
INSERT INTO memo_tag (memo_id, tag_id) VALUES (22, 1);


-- task 생성
INSERT INTO task (name, priority, importance, difficulty, task_status, daily_schedule_id, user_id) VALUES ('에러 페이지 만들기', 'FIRST', 'SECOND', 'EASY', 'DONE', 17, 1);
INSERT INTO task (name, priority, importance, difficulty, task_status, daily_schedule_id, user_id) VALUES ('쿼리 API 만들기', 'THIRD', 'THIRD', 'EASY', 'PROGRESS', 17, 1);
INSERT INTO task (name, priority, importance, difficulty, daily_schedule_id, user_id) VALUES ('반복되는 코드 자바 반복문으로 리팩토링하기, 반복되는 코드 자바 반복문으로 리팩토링하기', 'SECOND', 'FIRST', 'HARD', 17, 1);
INSERT INTO task (name, priority, importance, difficulty, daily_schedule_id, user_id) VALUES ('Mysql 인덱스 안타는 쿼리 개선하기', 'FIRST', 'SECOND', 'NORMAL', 17, 1);
INSERT INTO task (name, priority, importance, difficulty, daily_schedule_id, user_id) VALUES ('무중단 배포 만들기', 'FIRST', 'THIRD', 'NORMAL', 17, 1);
INSERT INTO task (name, priority, importance, difficulty, daily_schedule_id, user_id) VALUES ('스프링 DTO에 유효성 검사 추가하기', 'FIRST', 'SECOND', 'EASY', 17, 1);


-- sub_task 생성
INSERT INTO sub_task (name, sub_task_status, task_id, user_id) VALUES ('404 페이지 만들기', 'TODO', 1, 1);
INSERT INTO sub_task (name, sub_task_status, task_id, user_id) VALUES ('400 페이지 만들기', 'DONE', 1, 1);
INSERT INTO sub_task (name, sub_task_status, task_id, user_id) VALUES ('401 페이지 만들기', 'PROGRESS', 1, 1);
INSERT INTO sub_task (name, sub_task_status, task_id, user_id) VALUES ('if, while 학습하기', 'PROGRESS', 3, 1);
INSERT INTO sub_task (name, sub_task_status, task_id, user_id) VALUES ('반복되는 코드 추출하기', 'PROGRESS', 3, 1);


-- learning_time 생성
INSERT INTO learning_time (start_time, end_time, task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 13:00:10', '2023-11-30 13:40:38', 1, 17, 1);
INSERT INTO learning_time (start_time, end_time, sub_task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 14:00:10', '2023-11-30 14:40:38', 1, 17, 1);
INSERT INTO learning_time (start_time, end_time, subject, daily_schedule_id, user_id) VALUES ('2023-11-30 15:00:10', '2023-11-30 15:40:38', 'rkskek=====', 17, 1);
INSERT INTO learning_time (start_time, end_time, task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 16:00:10', '2023-11-30 16:40:38', 1, 17, 1);
INSERT INTO learning_time (start_time, end_time, task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 20:00:10', '2023-11-30 20:40:38', 3, 17, 1);
INSERT INTO learning_time (start_time, end_time, sub_task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 17:00:10', '2023-11-30 17:40:38', 1, 17, 1);
INSERT INTO learning_time (start_time, end_time, task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 18:00:10', '2023-11-30 18:40:38', 1, 17, 1);
INSERT INTO learning_time (start_time, end_time, task_id, daily_schedule_id, user_id) VALUES ('2023-11-30 19:00:10', '2023-11-30 19:40:38', 2, 17, 1);


-- review_cycle 생성
INSERT INTO review_cycle (name, review_cycle_dates, user_id) VALUES ('복습 주기 1번', '1,4,7,10,12', 1);
INSERT INTO review_cycle (name, review_cycle_dates, user_id) VALUES ('복습 주기 2번', '1,4,7,10,12', 1);
