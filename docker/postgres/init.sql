-- Runs only on the first start of the container (empty volume)

-- Shipment Service: own user, owns the "shipments" database
CREATE USER shipment_user WITH PASSWORD 'shipment_pass';
ALTER DATABASE shipments OWNER TO shipment_user;

-- Dispatch Service: own user and own database
CREATE USER dispatch_user WITH PASSWORD 'dispatch_pass';
CREATE DATABASE dispatch OWNER dispatch_user;

-- Each user can connect only to its own database
REVOKE CONNECT ON DATABASE shipments FROM PUBLIC;
REVOKE CONNECT ON DATABASE dispatch FROM PUBLIC;
GRANT CONNECT ON DATABASE shipments TO shipment_user;
GRANT CONNECT ON DATABASE dispatch TO dispatch_user;