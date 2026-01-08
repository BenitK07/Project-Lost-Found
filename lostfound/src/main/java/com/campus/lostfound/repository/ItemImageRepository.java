package com.campus.lostfound.repository;

import com.campus.lostfound.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    List<ItemImage> findByItemTypeAndItemId(String itemType, Long itemId);
}
