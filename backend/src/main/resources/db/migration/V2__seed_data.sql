-- Seed permissions
INSERT INTO permissions (codename, name, description) VALUES
    ('product.view', 'View products', 'Can view product listings'),
    ('product.add', 'Add products', 'Can create new products'),
    ('product.change', 'Change products', 'Can edit existing products'),
    ('product.delete', 'Delete products', 'Can delete products'),
    ('category.view', 'View categories', 'Can view category listings'),
    ('category.add', 'Add categories', 'Can create new categories'),
    ('category.change', 'Change categories', 'Can edit existing categories'),
    ('category.delete', 'Delete categories', 'Can delete categories'),
    ('form.view', 'View forms', 'Can view form definitions'),
    ('form.add', 'Add forms', 'Can create form definitions'),
    ('form.change', 'Change forms', 'Can edit form definitions'),
    ('form.submit', 'Submit forms', 'Can submit form responses'),
    ('workflow.view', 'View workflows', 'Can view workflow definitions and instances'),
    ('workflow.add', 'Add workflows', 'Can create workflow definitions'),
    ('workflow.execute', 'Execute workflows', 'Can start and transition workflow instances'),
    ('user.view', 'View users', 'Can view user listings'),
    ('user.add', 'Add users', 'Can create new users'),
    ('user.change', 'Change users', 'Can edit existing users');

-- Seed groups
INSERT INTO user_groups (id, name, description) VALUES
    ('a0000000-0000-0000-0000-000000000001', 'Administrators', 'Full system access'),
    ('a0000000-0000-0000-0000-000000000002', 'Editors', 'Can manage products, categories, forms, and workflows'),
    ('a0000000-0000-0000-0000-000000000003', 'Viewers', 'Read-only access');

-- Administrators get all permissions
INSERT INTO group_permissions (group_id, permission_id)
SELECT 'a0000000-0000-0000-0000-000000000001', id FROM permissions;

-- Editors get view + add + change permissions
INSERT INTO group_permissions (group_id, permission_id)
SELECT 'a0000000-0000-0000-0000-000000000002', id FROM permissions
WHERE codename LIKE '%.view' OR codename LIKE '%.add' OR codename LIKE '%.change'
   OR codename = 'form.submit' OR codename = 'workflow.execute';

-- Viewers get view permissions only
INSERT INTO group_permissions (group_id, permission_id)
SELECT 'a0000000-0000-0000-0000-000000000003', id FROM permissions
WHERE codename LIKE '%.view';

-- Seed admin user (password: admin123)
INSERT INTO app_users (id, email, password, first_name, last_name, is_active, is_staff)
VALUES (
    'b0000000-0000-0000-0000-000000000001',
    'admin@boilerworks.dev',
    '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
    'Admin',
    'User',
    TRUE,
    TRUE
);

INSERT INTO user_group_membership (user_id, group_id)
VALUES ('b0000000-0000-0000-0000-000000000001', 'a0000000-0000-0000-0000-000000000001');

-- Seed categories
INSERT INTO categories (name, slug, description, sort_order) VALUES
    ('Electronics', 'electronics', 'Electronic devices and components', 1),
    ('Software', 'software', 'Software licenses and subscriptions', 2),
    ('Services', 'services', 'Professional services', 3);
