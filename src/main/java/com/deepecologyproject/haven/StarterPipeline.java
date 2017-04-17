/*
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.deepecologyproject.haven;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.io.PubsubIO;
import com.google.cloud.dataflow.sdk.io.datastore.DatastoreIO;
import com.google.cloud.dataflow.sdk.options.DataflowPipelineOptions;
import com.google.cloud.dataflow.sdk.options.PipelineOptionsFactory;
import com.google.cloud.dataflow.sdk.runners.DataflowPipelineRunner;
import com.google.cloud.dataflow.sdk.transforms.Create;
import com.google.cloud.dataflow.sdk.transforms.DoFn;
import com.google.cloud.dataflow.sdk.transforms.ParDo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A starter example for writing Google Cloud Dataflow programs.
 *
 * <p>The example takes two strings, converts them to their upper-case
 * representation and logs them.
 *
 * <p>To run this starter example locally using DirectPipelineRunner, just
 * execute it without any additional parameters from your favorite development
 * environment.
 *
 * <p>To run this starter example using managed resource in Google Cloud
 * Platform, you should specify the following command-line options:
 *   --project=<YOUR_PROJECT_ID>
 *   --stagingLocation=<STAGING_LOCATION_IN_CLOUD_STORAGE>
 *   --runner=BlockingDataflowPipelineRunner
 */
public class StarterPipeline {
  private static final Logger LOG = LoggerFactory.getLogger(StarterPipeline.class);

  public static void main(String[] args) {
//    Pipeline p = Pipeline.create(PipelineOptionsFactory.fromArgs(args).withValidation().create());
    // Create a DataflowPipelineOptions object. This object lets us set various execution
    // options for our pipeline, such as the associated Cloud Platform project and the location
    // in Google Cloud Storage to stage files.

    DataflowPipelineOptions options = PipelineOptionsFactory.create().as(DataflowPipelineOptions.class);
    options.setRunner(DataflowPipelineRunner.class);
    options.setProject("ethereal-brace-122116");
    options.setStreaming(true);
    options.setTempLocation("gs://haven-pubsub-dev/temp");
    options.setStagingLocation("gs://haven-pubsub-dev/staging");

    // Create the Pipeline object with the options we defined above.
    Pipeline p = Pipeline.create(options);

    p.apply(PubsubIO.Read.subscription("projects/ethereal-brace-122116/subscriptions/particles"))
      .apply(ParDo.of(new DoFn<String, Void>() {
        @Override
        public void processElement(ProcessContext c)  {
          LOG.info(c.element());
        }
      }));

    p.run();
  }
}
