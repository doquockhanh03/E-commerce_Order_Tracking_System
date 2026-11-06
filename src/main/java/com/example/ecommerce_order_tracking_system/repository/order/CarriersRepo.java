package com.example.ecommerce_order_tracking_system.repository.order;

import com.example.ecommerce_order_tracking_system.common.enums.ShippingZone;
import com.example.ecommerce_order_tracking_system.entity.order.Carriers;
import com.example.ecommerce_order_tracking_system.entity.order.ShippingConfigs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface CarriersRepo extends JpaRepository<Carriers, String> {

    @Query("""
    SELECT s FROM ShippingConfigs s WHERE s.zoneName = :shippingZone
      AND s.price = ( SELECT MIN(s2.price) FROM ShippingConfigs s2
      WHERE s2.zoneName = :shippingZone)""")
    List<ShippingConfigs> findByMinFee(@Param("shippingZone") ShippingZone shippingZone);
}
