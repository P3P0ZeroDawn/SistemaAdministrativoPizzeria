/**
 * Author:  pedro
 * Created: 25 may 2026
 */

CREATE USER 'admin_pizzeria'@'%' identified by '12345';
GRANT ALL PRIVILEGES ON pizzeria.* TO 'admin_pizzeria'@'%';