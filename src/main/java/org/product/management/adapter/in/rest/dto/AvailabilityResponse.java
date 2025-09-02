package org.product.management.adapter.in.rest.dto;

public class AvailabilityResponse {
    public int productId;
    public int requestedCount;
    public boolean available;

    public AvailabilityResponse(int productId, int requestedCount, boolean available) {
        this.productId = productId;
        this.requestedCount = requestedCount;
        this.available = available;
    }
}
