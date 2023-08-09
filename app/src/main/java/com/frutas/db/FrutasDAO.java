package com.frutas.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.frutas.db.entity.CartEntity;

import java.util.List;

@Dao
public interface FrutasDAO {

    @Insert(entity = CartEntity.class, onConflict = OnConflictStrategy.REPLACE)
    Long insetCart(CartEntity cartEntity);

    @Query("SELECT * FROM cart")
    List<CartEntity> getAllProductFromCart();

    @Query("DELETE FROM cart WHERE productId = :id")
    int deleteProductByID(int id);

    @Query("DELETE FROM cart")
    void clearCart();
}
