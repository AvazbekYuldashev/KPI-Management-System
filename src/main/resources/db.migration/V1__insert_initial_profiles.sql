
-- Ma'lumot kiritish: PROFILE
INSERT INTO profile (
    id, name, surname, username, password, status, visible, created_date
) VALUES (
             'cf120912-a989-47bb-8d70-0e915212e147',
             'Avazbek',
             'Yuldashev',
             'telegram.vzlom@mail.ru',
             '$2a$10$Hr8DfylXLWK9JkR/3YLx4e1gcJZSikcKKsUnX94DTp99dQN2ZcFcK',
             'ACTIVE',
             true,
             now()
         );

-- Ma'lumot kiritish: PROFILE_ROLE
INSERT INTO profile_role (
    id, profile_id, role, created_date, visible
) VALUES
      ('cf120912-a989-47bb-8d70-1e915212e147', 'cf120912-a989-47bb-8d70-0e915212e147', 'ROLE_USER', now(), true),
      ('cf120913-a989-47bb-8d70-0e915212e147', 'cf120912-a989-47bb-8d70-0e915212e147', 'ROLE_ADMIN', now(), true);
