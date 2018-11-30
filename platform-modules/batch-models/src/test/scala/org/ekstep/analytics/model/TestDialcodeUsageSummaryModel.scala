import org.ekstep.analytics.framework.V3Event
import org.ekstep.analytics.framework.util.JSONUtils
import org.ekstep.analytics.model.{DialcodeUsageSummaryModel, SparkSpec}

case class DialUsageMetric(total_dial_scans: Int, first_scan: Long, last_scan: Long)

class TestDialcodeUsageSummaryModel extends SparkSpec(null) {

  "DialcodeUsageSummaryModel" should "aggregate based on channel and dialcode" in {
    val rdd1 = loadFile[V3Event]("src/test/resources/dialcode-usage-summary/withSameDialcodeAndChannel.log")
    val me0 = DialcodeUsageSummaryModel.execute(rdd1, None)
    val events = me0.collect()
    val me_event = JSONUtils.deserialize[DialUsageMetric](JSONUtils.serialize(events.last.edata.eks))

    me0.count() should be(1)
    me_event.first_scan should be(1542175922000L)
    me_event.last_scan should be(1542175922111L)
    me_event.total_dial_scans should be(2)
    events.last.dimensions.dial_code.getOrElse("") should be("2Q8WDW")
    events.last.dimensions.channel.getOrElse("") should be("01235953109336064029413")
  }

  it should "aggregate dialcodes from different events based on channel" in {
    val rdd1 = loadFile[V3Event]("src/test/resources/dialcode-usage-summary/withSameDialcodeInDifferentEvents.log")
    val me0 = DialcodeUsageSummaryModel.execute(rdd1, None)
    val events = me0.collect()
    val deserialize = (x: AnyRef) => JSONUtils.deserialize[DialUsageMetric](JSONUtils.serialize(x))


    me0.count() should be(3)

    val event1 = events.filter(_.dimensions.dial_code.getOrElse("") == "2Q8WDE").head
    val event1Metric = deserialize(event1.edata.eks)
    event1Metric.first_scan should be(1542175922142L)
    event1Metric.last_scan should be(1542175922142L)
    event1Metric.total_dial_scans should be(1)
    event1.dimensions.dial_code.getOrElse("") should be("2Q8WDE")
    event1.dimensions.channel.getOrElse("") should be("01235953109336064029452")

    val event2 = events.filter(_.dimensions.dial_code.getOrElse("") == "2Q8WDR").head
    val event2Metric = deserialize(event2.edata.eks)
    event2Metric.first_scan should be(1542175922141L)
    event2Metric.last_scan should be(1542175922143L)
    event2Metric.total_dial_scans should be(2)
    event2.dimensions.dial_code.getOrElse("") should be("2Q8WDR")
    event2.dimensions.channel.getOrElse("") should be("01235953109336064029451")

    val event3 = events.filter(_.dimensions.dial_code.getOrElse("") == "2Q8WDW").head
    val event3Metric = deserialize(event3.edata.eks)
    event3Metric.first_scan should be(1542175922142L)
    event3Metric.last_scan should be(1542175922142L)
    event3Metric.total_dial_scans should be(1)
    event3.dimensions.dial_code.getOrElse("") should be("2Q8WDW")
    event3.dimensions.channel.getOrElse("") should be("01235953109336064029452")
  }

  it should "handle null/empty list/string values in dialcodes from raw events" in {
    val rdd1 = loadFile[V3Event]("src/test/resources/dialcode-usage-summary/emptyNullValuesInDialcodes.log")
    val me0 = DialcodeUsageSummaryModel.execute(rdd1, None)
    val events = me0.collect()

    val deserialize = (x: AnyRef) => JSONUtils.deserialize[DialUsageMetric](JSONUtils.serialize(x))

    val event1 = events.filter(_.dimensions.dial_code.getOrElse("") == "123456").head
    val event1Metric = deserialize(event1.edata.eks)
    event1Metric.total_dial_scans should be(1)
    event1Metric.first_scan should be(1542175922148L)
    event1Metric.last_scan should be(1542175922148L)

    me0.count() should be(1)
  }
}