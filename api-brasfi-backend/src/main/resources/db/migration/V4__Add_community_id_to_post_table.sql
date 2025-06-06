ALTER TABLE post ADD COLUMN community_id BIGINT NULL;

ALTER TABLE post
ADD CONSTRAINT fk_post_community
FOREIGN KEY (community_id) REFERENCES community(id); 