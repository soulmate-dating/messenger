create keyspace
    if not exists messenger
    with durable_writes = true
    and replication = {
        'class': 'SimpleStrategy',
        'replication_factor': 1
    };

use messenger;

create table if not exists messages(
    id uuid,
    sender_id uuid,
    recipient_id uuid,
    date timestamp,
    content text,
    PRIMARY KEY ( id, date )
) with clustering order by (date DESC);
