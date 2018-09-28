package com.example.thecoop.repos;

import com.example.thecoop.domain.Message;
import com.example.thecoop.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author iveshtard
 * @since 7/31/2018
 */
public interface MessageRepo extends JpaRepository<Message, Long> {

    List<Message> findByTagAndDialogIsFalseOrderByDateDesc(String tag);

    Page<Message> findAllByBranchIdOrderByDateDesc(Long id, Pageable pageable);

    List<Message> findAllByBranchIdOrderByDateDesc(Long id);

    List<Message> findAllByAnswerMessage(Message message);

    Page<Message> findMessageByDialogIsFalseOrderByDateDesc(Pageable pageable);

    Page<Message> findMessageByAuthorOrderByDateDesc(User user, Pageable pageable);

    Page<Message> findMessageByDialogIsFalseAndAuthorOrderByDateDesc(User user, Pageable pageable);
}
