package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.Collections;
import java.util.List;

@RegisterForReflection
public class DiscountCodes {
   private long totalCount;
   private List<DiscountCode> discountCodesList;

   public DiscountCodes(List<DiscountCode> discountCodesList, long totalCount) {
      this.discountCodesList = Collections.unmodifiableList(discountCodesList);
      this.totalCount = totalCount;
   }

   public List<DiscountCode> getDiscountCodesList() {
      return Collections.unmodifiableList(discountCodesList);
   }

   public void setDiscountCodesList(List<DiscountCode> discountCodesList) {
      this.discountCodesList = Collections.unmodifiableList(discountCodesList);
   }

   public long getTotalCount() {
      return totalCount;
   }

   public void setTotalCount(long totalCount) {
      this.totalCount = totalCount;
   }
}
