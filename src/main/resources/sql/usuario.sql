/**
 * Author:  pedro
 * Created: 25 may 2026
 */

DROP USER IF EXISTS 'admin_pizzeria'@'%' identified by '12345';
CREATE USER 'admin_pizzeria'@'%' identified by '12345';
GRANT ALL PRIVILEGES ON pizzeria.* TO 'admin_pizzeria'@'%';