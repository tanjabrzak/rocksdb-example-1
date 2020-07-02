## Little Scala/RocksDB example

`git clone git@github.com:tanjabrzak/rocksdb-example-1.git`

`cd rocksdb-example-1`

`sbt clean compile`

`sbt run`

This will create database `~/testing_rocksdb/rocksdb1.db`

To reproduce lock, while program is running, open another terminal

`cd rocksdb-example-1`

`sbt run`

You will get

```$xslt
[error] (run-main-0) org.rocksdb.RocksDBException: While lock file: ~/testing_rocksdb/rocksdb1.db/LOCK: Resource temporarily unavailable
[error] org.rocksdb.RocksDBException: While lock file: ~/testing_rocksdb/rocksdb1.db/LOCK: Resource temporarily unavailable
[error] 	at org.rocksdb.RocksDB.open(Native Method)
[error] 	at org.rocksdb.RocksDB.open(RocksDB.java:231)
.
.
.
```
It's also possible to reproduce error in IntelliJ:
 - open `Main.scala`, and run it (right click/Run Main) 
 - in Toolbar click `Edit Configurations`
 - choose `Allow parallel` run for Main
 - run Main from Toolbar twice