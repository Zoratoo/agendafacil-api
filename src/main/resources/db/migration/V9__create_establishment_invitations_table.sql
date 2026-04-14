CREATE TABLE establishment_invitations (
    id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    establishment_id UUID        NOT NULL REFERENCES establishments(id),
    invited_by       UUID        NOT NULL REFERENCES users(id),
    invited_user_id  UUID        NOT NULL REFERENCES users(id),
    role             VARCHAR(30) NOT NULL,
    status           VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP   NOT NULL DEFAULT NOW(),
    CONSTRAINT unique_pending_invitation
       UNIQUE (establishment_id, invited_user_id, role)
);