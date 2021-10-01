package com.redhat.challenge.discount.model;

import org.infinispan.protostream.SerializationContextInitializer;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(schemaPackageName = "dc_monitoring", includeClasses = {DiscountCode.class, DiscountCodeType.class})
public interface MonitoringAppContextInitializer extends SerializationContextInitializer {
}