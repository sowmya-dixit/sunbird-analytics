package org.ekstep.analytics.job.summarizer

import org.ekstep.analytics.model.SparkSpec
import org.ekstep.analytics.framework.JobConfig
import org.ekstep.analytics.framework.Fetcher
import org.ekstep.analytics.framework.Query
import org.ekstep.analytics.framework.Dispatcher
import org.ekstep.analytics.framework.util.JSONUtils
import com.datastax.spark.connector.cql.CassandraConnector

class TestDeviceRecommendationTrainingJob extends SparkSpec(null) {
  
    ignore should "execute the job and shouldn't throw any exception" in {
        
        CassandraConnector(sc.getConf).withSessionDo { session =>
            session.execute("TRUNCATE device_db.device_usage_summary;");
            session.execute("TRUNCATE device_db.device_specification;");
            session.execute("TRUNCATE device_db.device_content_summary_fact;");
            session.execute("TRUNCATE content_db.content_usage_summary_fact;");
            session.execute("TRUNCATE content_db.content_to_vector;");
            session.execute("INSERT INTO device_db.device_usage_summary(device_id, avg_num_launches, avg_time, end_time, last_played_content, last_played_on, mean_play_time, mean_play_time_interval, num_contents, num_days, num_sessions, play_start_time, start_time, total_launches, total_play_time, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a036', 0.01, 0.07, 1475731808000, 'domain_68601', 1475731808000, 10, 0, 2, 410, 1, 1452038407000, 1475731808000, 3, 10, 30);");
            session.execute("INSERT INTO device_db.device_usage_summary(device_id, avg_num_launches, avg_time, end_time, last_played_content, last_played_on, mean_play_time, mean_play_time_interval, num_contents, num_days, num_sessions, play_start_time, start_time, total_launches, total_play_time, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a043', 0.01, 0.07, 1475731808000, '', 1452038407000, 10, 0, 2, 410, 1, 1475731808000, 1452038407000, 3, 10, 30);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a036', 'domain_68601', null, 1452038407000, false, null, 1475731808000, 0, 0, 1, 1, 1459641600000, 10, 10);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a036', 'domain_63844', null, 1475731808000, true, null, 1452038407000, 1, 10, 20, 100, 1452038407000, 1534, 1234);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a043', 'domain_68601', null, 1475731808000, false, null, 1452038407000, 0, 0, 1, 1, 1459641600000, 10, 20);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a044', 'domain_63844', null, 1452038407000, true, null, 1475731808000, 1, 10, 20, 100, 1459641600000, 1534, 124);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc48a084', 'domain_63333', null, 1475731808000, false, null, 1452038407000, 0, 0, 1, 1, 1459641600000, 10, 20);");
            session.execute("INSERT INTO device_db.device_content_summary_fact(device_id, content_id, avg_interactions_min, download_date, downloaded, game_ver, last_played_on, mean_play_time_interval, num_group_user, num_individual_user, num_sessions, start_time, total_interactions, total_timespent) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a044', 'domain_70615', null, 1452038407000, true, null, 1475731808000, 1, 10, 20, 100, 1459641600000, 1534, 124);");
            session.execute("INSERT INTO device_db.device_specification(device_id, os, screen_size, capabilities, cpu, device_local_name, device_name, external_disk, internal_disk, make, memory, num_sims, primary_secondary_camera) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a036', 'Android 4.4.2', 3.89, [''], 'abi: armeabi-v7a  ARMv7 Processor rev 4 (v7l)', '', '', 1.13, 835.78, 'Micromax Micromax A065', -1, 1, '5.0,1.0');");
            session.execute("INSERT INTO device_db.device_specification(device_id, os, screen_size, capabilities, cpu, device_local_name, device_name, external_disk, internal_disk, make, memory, num_sims, primary_secondary_camera) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a043', 'Android 5.0.1', 5.7, [''], 'abi: armeabi-v7a  ARMv7 Processor rev 4 (v7l)', '', '', 1.13, 835.78, 'Samsung S685', -1, 1, ' ');");
            session.execute("INSERT INTO device_db.device_specification(device_id, os, screen_size, capabilities, cpu, device_local_name, device_name, external_disk, internal_disk, make, memory, num_sims, primary_secondary_camera) VALUES ('9ea6702483ff7d4fcf9cb886d0ff0e1ebc25a044', 'Android 5.0.1', 5.7, [''], 'abi: armeabi-v7a  ARMv7 Processor rev 4 (v7l)', '', '', 1.13, 835.78, 'Samsung S685', -1, 1, '5.0');");
            session.execute("INSERT INTO device_db.device_specification(device_id, os, screen_size, capabilities, cpu, device_local_name, device_name, external_disk, internal_disk, make, memory, num_sims, primary_secondary_camera) VALUES ('8ea6702483ff7d4fcf9cb886d0ff0e1ebc25a044', 'Android 5.0.1', 5.7, [''], 'abi: armeabi-v7a  ARMv7 Processor rev 4 (v7l)', '', '', 1.13, 835.78, 'Samsung S685', -1, 1, ' , ');");
            session.execute("INSERT INTO content_db.content_usage_summary_fact(d_period, d_tag, d_content_id, m_avg_interactions_min, m_avg_sess_device, m_avg_ts_session, m_device_ids, m_last_gen_date, m_last_sync_date, m_publish_date, m_total_devices, m_total_interactions, m_total_sessions, m_total_ts) VALUES (0, 'all' ,'domain_63844', 0, 0, 0, bigintAsBlob(3), 1459641600, 1452038407000, 1452038407000, 4, 0, 0, 20);");
            session.execute("INSERT INTO content_db.content_usage_summary_fact(d_period, d_tag, d_content_id, m_avg_interactions_min, m_avg_sess_device, m_avg_ts_session, m_device_ids, m_last_gen_date, m_last_sync_date, m_publish_date, m_total_devices, m_total_interactions, m_total_sessions, m_total_ts) VALUES (0, 'all' ,'domain_68601', 0, 0, 0, bigintAsBlob(3), 1459641600, 1459641600000, 1459641600000, 4, 0, 0, 20);");
            session.execute("INSERT INTO content_db.content_usage_summary_fact(d_period, d_tag, d_content_id, m_avg_interactions_min, m_avg_sess_device, m_avg_ts_session, m_device_ids, m_last_gen_date, m_last_sync_date, m_publish_date, m_total_devices, m_total_interactions, m_total_sessions, m_total_ts) VALUES (2016731, 'dff9175fa217e728d86bc1f4d8f818f6d2959303' ,'domain_63844', 0, 0, 0, bigintAsBlob(3), 1459641600, 1475731808000, 1475731808000, 4, 0, 0, 20);");
            session.execute("INSERT INTO content_db.content_to_vector(content_id, tag_vec, text_vec) VALUES ('domain_63844', [-0.002815, -0.00077, 0.00783, -0.003143, -0.008894, -0.003984, -0.001336, -0.005424, -0.000627, -0.000348, -0.000123, 0.009205, 0.003591, -0.001231, -0.008066, -0.006406, 0.006921, 0.007899, -0.003827, 0.00711, -0.009358, 0.006422, 0.001727, -0.006776, 0.00748, -0.00924, -0.008639, -0.000556, -0.005417, 0.008865, -0.000105, -0.002611, -0.009687, 0.006844, 0.008259, 0.007043, -0.002233, 0.009259, -0.004022, 0.006698, -0.003153, -0.002884, 0.006946, 0.003135, 0.007184, -0.00031, -0.003099, -0.003129, 0.008441, -0.005023] ,[-0.002815, -0.00077, 0.00783, -0.003143, -0.008894, -0.003984, -0.001336, -0.005424, -0.000627, -0.000348, -0.000123, 0.009205, 0.003591, -0.001231, -0.008066, -0.006406, 0.006921, 0.007899, -0.003827, 0.00711, -0.009358, 0.006422, 0.001727, -0.006776, 0.00748, -0.00924, -0.008639, -0.000556, -0.005417, 0.008865, -0.000105, -0.002611, -0.009687, 0.006844, 0.008259, 0.007043, -0.002233, 0.009259, -0.004022, 0.006698, -0.003153, -0.002884, 0.006946, 0.003135, 0.007184, -0.00031, -0.003099, -0.003129, 0.008441, -0.005023]);");
        }
        
        val config = JobConfig(Fetcher("local", None, Option(Array(Query(None, None, None, None, None, None, None, None, None, Option("src/test/resources/sample_telemetry.log"))))), None, None, "org.ekstep.analytics.model.DeviceRecommendationModel", Option(Map("saveDataFrame" -> true.asInstanceOf[AnyRef],"live_content_limit" -> Int.box(1000), "trainRatio" -> Double.box(0.5), "testRatio" -> Double.box(0.5), "libfm.executable_path" -> "src/test/resources/device-recos-training/")), Option(Array(Dispatcher("console", Map("printEvent" -> false.asInstanceOf[AnyRef])))), Option(10), Option("TestDeviceRecommendationJob"), Option(false))
        DeviceRecommendationTrainingJob.main(JSONUtils.serialize(config))(Option(sc));
    }
}