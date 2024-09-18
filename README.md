build from source:

mvn clean package 

run:

java -cp target/slow-query-parse-1.0-SNAPSHOT.jar com.pingcap.slowqueryparse.ParseSlowQuery SLOW_QUERY_DIR IP_PORT USER PASSWORD DB_NAME TABLE_NAME