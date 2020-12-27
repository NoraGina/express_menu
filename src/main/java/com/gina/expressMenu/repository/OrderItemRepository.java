package com.gina.expressMenu.repository;

import com.gina.expressMenu.model.OrderCustomer;
import com.gina.expressMenu.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("Select o from OrderItem o where orderCustomer.idOrderCustomer=:idOrderCustomer ")
    List<OrderItem> findAllByOrderCustomerId(@Param("idOrderCustomer")Long idOrderCustomer);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "delete from Orders_Item where id_order_item = ?1", nativeQuery = true)
    void deleteById(Long idOrderItem);


    @Query("Select o from OrderItem o where product.idProduct=:idProduct")
    OrderItem findByIdProduct(@Param("idProduct") Long idProduct);

    @Query("Select o from OrderItem o where product.idProduct=:idProduct")
    List<OrderItem> findAllByIdProduct(@Param("idProduct") Long idProduct);

    @Query("Select orderCustomer from OrderItem oi where oi.idOrderItem=:idOrderItem")
    OrderCustomer findOrderCustomerByIdOrderItem(@Param("idOrderItem") Long idOrderItem);
}
