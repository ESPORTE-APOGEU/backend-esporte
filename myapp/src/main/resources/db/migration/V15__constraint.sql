ALTER TABLE friendships
  DROP CONSTRAINT IF EXISTS chk_friendship_user_order;

ALTER TABLE friendships
  ADD CONSTRAINT chk_friendship_user_order
  CHECK (user1_id COLLATE "C" < user2_id COLLATE "C");