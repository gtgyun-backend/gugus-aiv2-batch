-- Make model_no column nullable in appraisals table
-- First, drop the foreign key constraint
ALTER TABLE appraisals 
DROP FOREIGN KEY appraisals_models_FK;

-- Modify the column to allow NULL values
ALTER TABLE appraisals 
MODIFY COLUMN model_no BIGINT NULL;

-- Re-add the foreign key constraint
ALTER TABLE appraisals 
ADD CONSTRAINT appraisals_models_FK 
FOREIGN KEY (model_no) REFERENCES models(model_no) 
ON DELETE CASCADE ON UPDATE CASCADE;
