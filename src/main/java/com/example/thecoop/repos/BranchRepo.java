package com.example.thecoop.repos;

import com.example.thecoop.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author iveshtard
 * @since 9/5/2018
 */
public interface BranchRepo extends JpaRepository<Branch, Long> {

    Branch findByName(String name);

    Branch findByNameAndDialogIsFalse(String name);

    List<Branch> findBranchByDialogIsFalse();

    List<Branch> findBranchByDialogIsTrue();

}
