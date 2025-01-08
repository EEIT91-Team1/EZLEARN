package org.ezlearn.repository;

import org.ezlearn.model.WishList;
import org.ezlearn.model.WishListId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface WishListRepository extends JpaRepository<WishList,WishListId> {

}
