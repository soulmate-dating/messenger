create keyspace
    if not exists messenger
    with replication = {'class':'NetworkTopologyStrategy', 'replication_factor':1}
        and durable_writes = false;

use messenger;

create table if not exists messages(
    id uuid,
    sender_id uuid,
    recipient_id uuid,
    date timestamp,
    content text,
    tag_name text,
    tag_external_id uuid,
    PRIMARY KEY ( id, date )
) with clustering order by (date DESC);
