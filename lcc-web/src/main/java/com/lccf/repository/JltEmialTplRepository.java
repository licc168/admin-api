package com.lccf.repository;

import com.lccf.domain.JltEmailTpl;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 *
 * @author lichangchao
 * @version 1.0.0
 * @date 2017/12/4 17:02
 * @see
 */
public interface JltEmialTplRepository extends JpaRepository<JltEmailTpl, Long> {
  List<JltEmailTpl> findByCodeAndDeleteFlag(String code, boolean deleteFlag);
}
