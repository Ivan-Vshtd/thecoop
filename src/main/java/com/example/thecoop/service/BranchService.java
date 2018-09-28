package com.example.thecoop.service;

import com.example.thecoop.domain.Branch;
import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import com.example.thecoop.repos.BranchRepo;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author iveshtard
 * @since 9/14/2018
 */
@Slf4j
@Service
public class BranchService {

    @Autowired
    BranchRepo branchRepo;

    public boolean addBranch(Branch branch) {
        Branch branchFromDb = branchRepo.findByName(branch.getName());
        if (branchFromDb != null) {
            return false;
        } else {
            branchRepo.save(branch);

            log.debug(branch.getName() + " has been successfully added to db");
        }
        return true;
    }

    public Branch addMessageToBranch(String branchName, User user, Message message){
        Branch branch = branchRepo.findByNameAndDialogIsFalse(branchName);
        message.setAuthor(user);
        message.setBranch(branch);

        if (branch.isDialog()) {
            message.setDialog(true);
        }
        return branch;
    }

    public void editBranch(Branch branch, User currentUser, String name, String description) {
        if (branch != null && currentUser.isAdmin()) {
            if (!Strings.isEmpty(name)) {
                branch.setName(name);
            }
            if (!Strings.isEmpty(description)) {
                branch.setDescription(description);
            }
            branchRepo.save(branch);
        }
    }
}
