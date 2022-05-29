
-- TODO: Fijarse que esto corra después de Hibernate, por si la DB está vacía y no encuentra las tablas

CREATE OR REPLACE VIEW public_debates AS
    SELECT d.debateid, category, created_date, description, name, status, creatorid, imageid, opponentid, COUNT(DISTINCT userid) AS subscribedcount
    FROM debates2 d LEFT JOIN subscribed2 s ON d.debateid = s.debateid
    GROUP BY d.debateid;