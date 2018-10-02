package com.example.thecoop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @author iveshtard
 * @since 7/31/2018
 */

@Getter @Setter
@Entity
@Table
@NoArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message too long (more than 2kB)")
    private String text;
    @Length(max = 255, message = "Message too long (more than 255)")
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User author;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    private Branch branch;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_message_id")
    private Message answerMessage;

    private boolean dialog;

    private boolean deleted;

    private String filename;

    private Date date = new Date();

    private Date updates;

    public Message(String text, String tag, User user) {
        this.author = user;
        this.text = text;
        this.tag = tag;
    }

    public String getAuthorName() {
        return author.getUsername();
    }

    public String getAuthorAvatar() {
        return author.getAvatarFilename();
    }

    public String getBranchName(){
        return branch.getName();
    }

    public long getAuthorMessagesCount(){
        return author
                .getMessages()
                .stream()
                .filter(message -> !message.isDialog())
                .count(); // returns count only of public messages
    }

    public Date getAuthorRegisterDate(){
        return author.getDate();
    }

    public boolean getAuthorStatus(){
       return author.isOnline();
    }

    public String getAnswer() {
        return String.format("%s said: at %s <br/>\"%s\"",
                getAnswerMessage().getAuthorName(),
                getAnswerMessage().getDate(),
                getAnswerMessage().getText());
    }
}