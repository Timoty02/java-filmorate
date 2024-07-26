MERGE INTO "genres" AS target
USING (VALUES ('Комедия'), ('Драма'), ('Мультфильм'), ('Триллер'), ('Документальный'), ('Боевик')) AS source (genre_name)
ON target."name" = source.genre_name
WHEN NOT MATCHED THEN
  INSERT ("name") VALUES (source.genre_name);

MERGE INTO "rating" AS target
USING (VALUES ('G'), ('PG'), ('PG-13'), ('R'), ('NC-17')) AS source (rating_name)
ON target."name" = source.rating_name
WHEN NOT MATCHED THEN
  INSERT ("name") VALUES (source.rating_name);
