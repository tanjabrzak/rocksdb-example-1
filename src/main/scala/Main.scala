import java.io.File
import org.rocksdb._

object Main extends App {
  val UTF8  : String = "UTF-8"

  RocksDB.loadLibrary()
  val path = System.getProperty("user.home") + "/testing_rocksdb/rocksdb1.db"
  new File(path).mkdirs()

  val bloomFilter = new BloomFilter(10, false)
  val tableOptions = new BlockBasedTableConfig()
  tableOptions
    .setFilter(bloomFilter)

  var options = new Options()
    .setCreateIfMissing(true)
    .setTableFormatConfig(tableOptions)
    .setStatistics(new Statistics())
    .setMaxBackgroundCompactions(4)
    .setMaxBackgroundFlushes(2)
    .setBytesPerSync(1048576)
    .setLevelCompactionDynamicLevelBytes(true)

  var db = RocksDB.open(options, path)

  for (i <- 1 until 5000000) {
    System.out.println("put key: " + i)
    put(i.toString, "value " + i)
    // print some stats
    if (i % 1000 == 0) {
      getStats()
    }
  }

  for (i <- 1 until 5000000) {
    System.out.println("get key: " + i)
    val record = get(i.toString)
    System.out.println("record: " + record)
  }

  shutdown()

  def put(k:String , v:String) : Unit = {
    db.put(
      k.getBytes(UTF8),
      v.getBytes(UTF8)
    )
  }

  def get(k:String) : Option[String] = {
    try {
      val v = db.get(k.getBytes(UTF8))
      Some(new String(v, UTF8))
    } catch {
      case e:java.lang.NullPointerException => None
    }
  }

  def getStats() : Unit = {
    val stats1 = db.getProperty("rocksdb.cur-size-all-mem-tables")
    val stats2 = db.getProperty("rocksdb.estimate-num-keys")
    val stats3 = db.getProperty("rocksdb.stats")

    System.out.println("Stats cur-size-all-mem-tables: " + stats1)
    System.out.println("Stats estimate-num-keys: " + stats2)
    System.out.println("Stats all:" + stats3)
  }

  def shutdown() : Unit = {
    db.close()
  }
}
