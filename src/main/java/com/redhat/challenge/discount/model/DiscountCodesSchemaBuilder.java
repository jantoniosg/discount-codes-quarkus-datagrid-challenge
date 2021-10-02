package com.redhat.challenge.discount.model;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;

@AutoProtoSchemaBuilder(schemaPackageName = "developer_games",
        includeClasses = {DiscountCode.class, DiscountCodeType.class})
public interface DiscountCodesSchemaBuilder extends GeneratedSchema {
}
