create keyspace
    if not exists messenger
    with replication = {'class':'NetworkTopologyStrategy', 'replication_factor':1}
        and durable_writes = false;

use messenger;

create table if not exists messages
(
    companions_composite_key text,
    id                       timeuuid,
    sender_id                uuid,
    recipient_id             uuid,
    date                     timestamp,
    content                  text,
    tag_name                 text,
    tag_external_id          uuid,
    PRIMARY KEY (companions_composite_key, id)
) with clustering order by (id desc);

create table if not exists users
(
    id         uuid,
    companions set<uuid>,
    PRIMARY KEY (id)
);

