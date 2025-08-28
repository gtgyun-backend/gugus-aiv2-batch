-- Add created_by column to appraisal_history_point_images table
ALTER TABLE appraisal_history_point_images 
ADD COLUMN created_by BIGINT NULL AFTER created_at;

-- Add foreign key constraint to users table
ALTER TABLE appraisal_history_point_images 
ADD CONSTRAINT fk_appraisal_history_point_images_created_by 
FOREIGN KEY (created_by) REFERENCES users(user_no) 
ON DELETE SET NULL ON UPDATE CASCADE;
