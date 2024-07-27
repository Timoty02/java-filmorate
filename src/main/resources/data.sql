
ALTER TABLE "likes" ADD CONSTRAINT IF NOT EXISTS "fk_user_id" FOREIGN KEY ("user_id") REFERENCES "users" ("id") ON DELETE CASCADE;
ALTER TABLE "likes" ADD CONSTRAINT IF NOT EXISTS "fk_film_id" FOREIGN KEY ("film_id") REFERENCES "films" ("id") ON DELETE CASCADE;
ALTER TABLE "friends" ADD CONSTRAINT IF NOT EXISTS "fk_requested_user_id" FOREIGN KEY ("requested_user_id") REFERENCES "users" ("id") ON DELETE CASCADE;
ALTER TABLE "friends" ADD CONSTRAINT IF NOT EXISTS "fk_receiving_user_id" FOREIGN KEY ("receiving_user_id") REFERENCES "users" ("id") ON DELETE CASCADE;


MERGE INTO "genres" AS target
USING (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS source (genre_name)
ON target."name" = source.genre_name
WHEN NOT MATCHED THEN
  INSERT ("name") VALUES (source.genre_name);

--INSERT INTO "films" ("id", "name", "description", "release_date", "duration", "rating_id")
--SELECT 0, 'Остров проклятых', 'Описание фильма', '2010-01-01', 120, 1
--WHERE NOT EXISTS (SELECT 1 FROM "films" WHERE "id" = 0);

MERGE INTO "rating" AS target
USING (VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')) AS source (rating_name)
ON target."name" = source.rating_name
WHEN NOT MATCHED THEN
  INSERT ("name") VALUES (source.rating_name);
