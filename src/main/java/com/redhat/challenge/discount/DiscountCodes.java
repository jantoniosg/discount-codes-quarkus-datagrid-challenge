package com.redhat.challenge.discount;

import com.redhat.challenge.discount.model.DiscountCode;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.util.List;

@RegisterForReflection
public class DiscountCodes {
   private long totalCount;
   private List<DiscountCode> discountCodesList;

   public DiscountCodes() {
   }

   public DiscountCodes(List<DiscountCode> discountCodesList, long totalCount) {
      this.discountCodesList = discountCodesList;
      this.totalCount = totalCount;
   }

   public List<DiscountCode> getDiscountCodesList() {
      return discountCodesList;
   }

   public void setDiscountCodesList(List<DiscountCode> discountCodesList) {
      this.discountCodesList = discountCodesList;
   }

   public long getTotalCount() {
      return totalCount;
   }

   public void setTotalCount(long totalCount) {
      this.totalCount = totalCount;
   }
}
