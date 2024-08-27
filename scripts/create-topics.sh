echo "Waiting for kafka to come online..."

cub kafka-ready -b kafka:9092 1 20

# create topics

kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic collected-energy \
  --replication-factor 1 \
  --partitions 4 \
  --create

kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic remaining-energy \
  --replication-factor 1 \
  --partitions 4 \
  --create

kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic consumed-energy \
  --replication-factor 1 \
  --partitions 4 \
  --create

kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic consumed-total-energy \
  --replication-factor 1 \
  --partitions 4 \
  --create

sleep infinity

