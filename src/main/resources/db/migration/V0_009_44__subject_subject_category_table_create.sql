create table if not exists subject_subject_category
(
    subject_id          bigint not null,
    subject_category_id bigint not null,
    constraint subject_subject_category_subject_category_fk foreign key (subject_category_id) references subject_category (id),
    constraint subject_subject_category_subject_fk foreign key (subject_id) references subject (id)
);