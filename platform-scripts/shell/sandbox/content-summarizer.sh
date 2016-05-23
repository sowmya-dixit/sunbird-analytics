#!/usr/bin/env bash

export SPARK_HOME=/home/ec2-user/spark-1.5.2-bin-hadoop2.3

## Job to run daily
cd /mnt/data/analytics/scripts
endDate=$(date --date yesterday "+%Y-%m-%d")

cs_config='{"search":{"type":"s3","queries":[{"bucket":"sandbox-data-store","prefix":"ss/","endDate":"'$endDate'","delta":0}]},"model":"org.ekstep.analytics.model.ContentActivitySummary","modelParams":{"modelVersion":"1.0","modelId":"ContentSummarizer"},"output":[{"to":"console","params":{"printEvent": false}}],"parallelization":8,"appName":"Content Summarizer","deviceMapping":false}'

nohup $SPARK_HOME/bin/spark-submit --master local[*] --jars /mnt/data/analytics/models/analytics-framework-0.5.jar --class org.ekstep.analytics.job.ContentActivitySummarizer /mnt/data/analytics/models/batch-models-1.0.jar --config "$cs_config" > "logs/$endDate-content-summmary.log" 2>&1&