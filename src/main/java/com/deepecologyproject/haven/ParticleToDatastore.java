package com.deepecologyproject.haven;


import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;


import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by johndonich on 4/16/17.
 */
public class ParticleToDatastore {
    private static final Logger LOG = LoggerFactory.getLogger(ParticleToDatastore.class);

    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().create();
        Pipeline particlePipeline = Pipeline.create(options);

        // Create the Pipeline object with the options we defined above.
        Pipeline p = Pipeline.create(options);

        p.apply(PubsubIO.read().subscription("projects/ethereal-brace-122116/subscriptions/particles"))
                .apply(ParDo.of(new DoFn<String, Void>() {
                    @ProcessElement
                    public void processElement(ProcessContext c)  {
                        LOG.info(c.element());
                    }
                }));

        p.run();


    }
}
