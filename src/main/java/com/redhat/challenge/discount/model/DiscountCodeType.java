package com.redhat.challenge.discount.model;

import org.infinispan.protostream.annotations.ProtoEnumValue;

public enum DiscountCodeType {
   @ProtoEnumValue(number = 1)
   PERCENT,
   @ProtoEnumValue(number = 2)
   VALUE
}
