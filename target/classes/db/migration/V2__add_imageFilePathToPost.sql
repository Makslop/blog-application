-- Add the new column
ALTER TABLE post ADD COLUMN image_file_path VARCHAR(255);

-- Update the existing records
UPDATE post SET image_file_path = 'pexels-adrien-olichon-16059681.jpg' WHERE id = 1;
UPDATE post SET image_file_path = 'pexels-adrien-olichon-16059681.jpg' WHERE id = 2;
