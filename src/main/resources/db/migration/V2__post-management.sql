CREATE TABLE social_media.post (
    id BIGSERIAL PRIMARY KEY,
    content TEXT NOT NULL,
    user_id UUID NOT NULL
);
