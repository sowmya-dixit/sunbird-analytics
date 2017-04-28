package org.ekstep.analytics.updater

import org.ekstep.analytics.model.SparkSpec
import org.ekstep.analytics.framework.DerivedEvent
import com.datastax.spark.connector._
import com.datastax.spark.connector.cql.CassandraConnector
import org.ekstep.analytics.util.Constants

class TestUpdateContentSnapshotDB extends SparkSpec(null) {

    it should "update the content snapshot updater db and check the updated fields" in {

        CassandraConnector(sc.getConf).withSessionDo { session =>
            session.execute("TRUNCATE content_db.content_snapshot_summary");
        }

        val rdd = loadFile[DerivedEvent]("src/test/resources/content-snapshot-updater/test_data1.json");
        val rdd1 = UpdateContentSnapshotDB.execute(rdd, None);
        
        val snapshotData1 = sc.cassandraTable[ContentSnapshotSummary](Constants.CONTENT_KEY_SPACE_NAME, Constants.CONTENT_SNAPSHOT_SUMMARY).collect

        // Check for DAY record
        val record1 = snapshotData1.filter { x => ("all".equals(x.d_author_id)) && ("all".equals(x.d_partner_id)) && (20170425 == x.d_period) }.last
        record1.total_author_count should be(2)
        record1.total_author_count_start should be(record1.total_author_count)
        record1.active_author_count should be(0)
        record1.active_author_count_start should be(record1.active_author_count)
        record1.total_content_count should be(4)
        record1.total_content_count_start should be(record1.total_content_count)
        record1.live_content_count should be(1)
        record1.live_content_count_start should be(record1.live_content_count)
        record1.review_content_count should be(0)
        record1.review_content_count_start should be(record1.review_content_count)
        
        // Check for WEEK record
        val record2 = snapshotData1.filter { x => ("290".equals(x.d_author_id)) && ("all".equals(x.d_partner_id)) && (2017717 == x.d_period) }.last
        record2.total_author_count should be(0)
        record2.total_author_count_start should be(record2.total_author_count)
        record2.active_author_count should be(0)
        record2.active_author_count_start should be(record2.active_author_count)
        record2.total_content_count should be(3)
        record2.total_content_count_start should be(record2.total_content_count)
        record2.live_content_count should be(1)
        record2.live_content_count_start should be(record2.live_content_count)
        record2.review_content_count should be(0)
        record2.review_content_count_start should be(record2.review_content_count)
        
        val rdd2 = loadFile[DerivedEvent]("src/test/resources/content-snapshot-updater/test_data2.json");
        val rdd3 = UpdateContentSnapshotDB.execute(rdd2, None);
        
        val snapshotData2 = sc.cassandraTable[ContentSnapshotSummary](Constants.CONTENT_KEY_SPACE_NAME, Constants.CONTENT_SNAPSHOT_SUMMARY).collect
        
        // Check for same DAY record
        val record3 = snapshotData2.filter { x => ("all".equals(x.d_author_id)) && ("all".equals(x.d_partner_id)) && (20170425 == x.d_period) }.last
        record3.total_author_count should be(2)
        record3.total_author_count_start should be(record3.total_author_count)
        record3.active_author_count should be(0)
        record3.active_author_count_start should be(record3.active_author_count)
        record3.total_content_count should be(4)
        record3.total_content_count_start should be(record3.total_content_count)
        record3.live_content_count should be(1)
        record3.live_content_count_start should be(record3.live_content_count)
        record3.review_content_count should be(0)
        record3.review_content_count_start should be(record3.review_content_count)
        
        // Check for next DAY record
        val record4 = snapshotData2.filter { x => ("all".equals(x.d_author_id)) && ("all".equals(x.d_partner_id)) && (20170426 == x.d_period) }.last
        record4.total_author_count should be(10)
        record4.total_author_count_start should be(record4.total_author_count)
        record4.active_author_count should be(2)
        record4.active_author_count_start should be(record4.active_author_count)
        record4.total_content_count should be(6)
        record4.total_content_count_start should be(record4.total_content_count)
        record4.live_content_count should be(3)
        record4.live_content_count_start should be(record4.live_content_count)
        record4.review_content_count should be(1)
        record4.review_content_count_start should be(record4.review_content_count)
        
        // Check for same WEEK record
        val record5 = snapshotData2.filter { x => ("290".equals(x.d_author_id)) && ("all".equals(x.d_partner_id)) && (2017717 == x.d_period) }.last
        record5.total_author_count should be(0)
        record5.total_author_count_start should be(0)
        record5.active_author_count should be(0)
        record5.active_author_count_start should be(0)
        record5.total_content_count should be(6)
        record5.total_content_count_start should be(3)
        record5.live_content_count should be(3)
        record5.live_content_count_start should be(1)
        record5.review_content_count should be(1)
        record5.review_content_count_start should be(0)
        
    }
}